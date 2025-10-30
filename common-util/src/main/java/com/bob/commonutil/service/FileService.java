package com.bob.commonutil.service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

/**
 * Service for file operations including remote and local folder creation and file upload.
 */
@Service
public class FileService {



    @Value("${remote.host.url}")
    private String remoteHostURL;

    @Value("${remote.host.user}")
    private String remoteHostUser;

    @Value("${remote.host.password}")
    private String remoteHostPassword;

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    /**
     * Creates a folder on a remote server using SSH and JSch.
     * Retries up to 3 times if creation fails.
     *
     * @param folderPath Path to create on the remote server.
     * @throws JSchException if folder creation fails after retries.
     */
    public void createFolder(String folderPath) throws Exception {
        createLocalFolder(folderPath);
    }

    public void createFolderSSH(String folderPath) throws Exception{
        String host = remoteHostURL;
        String user = remoteHostUser;
        String password = remoteHostPassword;
        int maxRetries = 3;
        int attempt = 0;
        boolean success = false;
        Exception lastException = null;
        while (attempt < maxRetries && !success) {
            attempt++;
            Session session = null;
            ChannelExec channel = null;
            try {
                JSch jsch = new JSch();
                session = jsch.getSession(user, host, 22);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand("mkdir -p " + folderPath);
                channel.connect();
                int exitStatus = channel.getExitStatus();
                if (exitStatus == 0 || exitStatus == -1) {
                    logger.info("Folder created on remote server: {} (attempt {})", folderPath, attempt);
                    success = true;
                } else {
                    throw new JSchException("Remote mkdir failed with exit status: " + exitStatus);
                }
            } catch (Exception e) {
                lastException = e;
                logger.error("Failed to create folder on remote server (attempt {}): {}", attempt, e.getMessage());
                if (attempt < maxRetries) {
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
            } finally {
                if (channel != null) channel.disconnect();
                if (session != null) session.disconnect();
            }
        }
        if (!success && lastException != null) {
            throw new JSchException("Failed to create folder after " + maxRetries + " attempts: " + lastException.getMessage(), lastException);
        }
    }

    /**
     * Create a folder locally using Java NIO (for Windows or local Linux systems).
     *
     * @param folderPath The path to create.
     * @throws IOException if folder creation fails.
     */
    public void createLocalFolder(String folderPath) throws IOException {
        // Replace backslashes with forward slashes for path normalization
        folderPath = folderPath.replace("\\", "/");
        Path path = Paths.get(folderPath);
        try {
            Files.createDirectories(path);
            logger.info("Local folder created: {}", folderPath);
        } catch (IOException e) {
            logger.error("Failed to create local folder {}: {}", folderPath, e.getMessage());
            throw e;
        }
    }

    /**
     * Uploads a file to the user's specific upload directory.
     * Creates the remote folder if it does not exist.
     *
     * @param file   The file to upload.
     * @param userId The user ID for directory segregation.
     * @return Path to the uploaded file.
     * @throws IOException   if file upload fails.
     * @throws JSchException if remote folder creation fails.
     */
    public Path uploadFile(MultipartFile file, String fileName, String uploadDir, String userId) throws Exception {
        if (file.isEmpty()) {
            throw new IOException("Cannot upload empty file.");
        }

        String userSpecificUploadPath = uploadDir;//Paths.get(uploadDir, userId).toString();
//        createFolder(userSpecificUploadPath);

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }
        }
        String uniqueFilename = fileName + extension.toLowerCase();
        Path filePath = Paths.get(userSpecificUploadPath, uniqueFilename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            try {
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                Files.setPosixFilePermissions(filePath, perms);
            } catch (Exception e) {
                logger.error("Failed to set 777 permission on the file: "+filePath);
                logger.error(e.getMessage(), e);
            }
        }

        return filePath;
    }

    /**
     * Extracts text content from a file, supporting PDF, DOC, and DOCX formats.
     *
     * @param filePath The absolute path to the file.
     * @return The extracted text content, or null if the file type is unsupported or an error occurs.
     */
    public String extractTextFromFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            logger.warn("File path is null or empty.");
            return null;
        }

        String lowercasedFilePath = filePath.toLowerCase();
        if (lowercasedFilePath.endsWith(".pdf")) {
            return extractTextFromPdf(filePath);
        } else if (lowercasedFilePath.endsWith(".doc") || lowercasedFilePath.endsWith(".docx")) {
            return extractTextFromDoc(filePath);
        } else {
            logger.warn("Unsupported file type: {}", filePath);
            return null;
        }
    }

    private String extractTextFromPdf(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            logger.warn("PDF file not found at path: {}", filePath);
            return null;
        }

        try (PDDocument document = PDDocument.load(path.toFile())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            logger.error("Error extracting text from PDF file {}: {}", filePath, e.getMessage(), e);
            return null;
        }
    }

    private String extractTextFromDoc(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            logger.warn("File not found at path: {}", filePath);
            return null;
        }

        String lowercasedFilePath = filePath.toLowerCase();
        try (InputStream is = Files.newInputStream(path)) {
            if (lowercasedFilePath.endsWith(".docx")) {
                XWPFDocument docx = new XWPFDocument(is);
                List<XWPFParagraph> paragraphs = docx.getParagraphs();
                StringBuilder text = new StringBuilder();
                for (XWPFParagraph para : paragraphs) {
                    text.append(para.getText()).append("\n");
                }
                return text.toString();
            } else if (lowercasedFilePath.endsWith(".doc")) {
                HWPFDocument doc = new HWPFDocument(is);
                WordExtractor extractor = new WordExtractor(doc);
                return extractor.getText();
            } else {
                logger.warn("Unsupported file type: {}", filePath);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error extracting text from document file {}: {}", filePath, e.getMessage(), e);
            return null;
        }
    }
}
