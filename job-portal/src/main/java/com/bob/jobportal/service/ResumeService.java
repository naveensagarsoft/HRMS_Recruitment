package com.bob.jobportal.service;

import com.bob.db.dto.BulkResumeUploadDto;
import com.bob.db.entity.BulkResumeUploadEntity;
import com.bob.db.mapper.BulkResumeUploadMapper;
import com.bob.db.repository.BulkResumeUploadRepository;
import com.bob.jobportal.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.bob.commonutil.service.FileService;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    private static final Logger logger = LoggerFactory.getLogger(ResumeService.class);

    @Value("${resume.upload.dir}")
    private String uploadDir;

    @Value("${resume.http.url}")
    private String resumeHTTPUrl;

    @Autowired
    private FileService fileService;

    @Autowired
    private ResumeBatchService resumeBatchService;

    @Autowired
    private BulkResumeUploadRepository bulkResumeUploadRepository;

    @Autowired
    private BulkResumeUploadMapper bulkResumeUploadMapper;

    @Autowired
    private SecurityUtils securityUtils;

    public void testFolderAccess(String folderPath) throws Exception {
            fileService.createLocalFolder(folderPath);
    }

    public String uploadResume(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            logger.warn("Attempted to upload empty or null file");
            throw new IllegalArgumentException("File is empty or not provided");
        }

        String userId = Optional.ofNullable(securityUtils.getCurrentUserId())
                .map(Object::toString)
                .filter(id -> !id.isEmpty())
                .orElseThrow(() -> new Exception("User not authenticated"));

        Path filePath;
        try {
            filePath = fileService.uploadFile(file, userId+"_"+UUID.randomUUID(), uploadDir, userId);
        } catch (Exception e) {
            logger.error("File upload failed for user {}: {}", userId, e.getMessage());
            throw new Exception("File upload failed", e);
        }

        String fileUrl = filePath.toAbsolutePath().toString();
        String fileHTTPUrl = resumeHTTPUrl+fileUrl.replace(uploadDir,"").replace("\\","/");

        BulkResumeUploadEntity resumeUpload = new BulkResumeUploadEntity();
        resumeUpload.setOriginalFilename(file.getOriginalFilename());
        resumeUpload.setOriginalFilepath(fileUrl);
        resumeUpload.setFileUrl(fileHTTPUrl);
        resumeUpload.setStatus(BulkResumeUploadEntity.Status.UPLOADED);

        bulkResumeUploadRepository.save(resumeUpload);
        logger.info("File uploaded successfully for user {} with ID {}", userId, resumeUpload.getResumeId());
        return "File uploaded successfully with ID: " + resumeUpload.getResumeId();
    }

    public String startBatchProcess() {
        String userId = securityUtils.getCurrentUserToken();
        resumeBatchService.runBatchProcess(userId);
        logger.info("Batch process started for user: {}", userId);
        return "Batch process started for user: " + userId;
    }

    public List<BulkResumeUploadDto> getResumesByUserId() {
        String userId = securityUtils.getCurrentUserToken();
        List<BulkResumeUploadEntity> resumes = bulkResumeUploadRepository.findAllByCreatedByOrderByCreatedDateDesc(userId);
        return resumes.stream()
                .map(bulkResumeUploadMapper::toDTO)
                .collect(Collectors.toList());
    }

    public String deleteBulkResumeUpload(UUID id) throws Exception {
        Optional<BulkResumeUploadEntity> optional = bulkResumeUploadRepository.findById(id);
        if (!optional.isPresent()) {
            logger.warn("BulkResumeUploadEntity not found for id: {}", id);
            throw new Exception("BulkResumeUploadEntity not found for id: " + id);
        }
        BulkResumeUploadEntity entity = optional.get();
        String filePath = entity.getOriginalFilepath();
        boolean fileDeleted = false;
        if (filePath != null && !filePath.isEmpty()) {
            try {
                java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(filePath));
                fileDeleted = true;
            } catch (Exception e) {
                logger.error("Failed to delete file at {}: {}", filePath, e.getMessage());
                throw new Exception("Failed to delete file: " + e.getMessage(), e);
            }
        }
        bulkResumeUploadRepository.deleteById(id);
        logger.info("Deleted BulkResumeUploadEntity with id: {} and fileDeleted: {}", id, fileDeleted);
        return "Deleted BulkResumeUploadEntity with id: " + id + (fileDeleted ? " and file deleted." : ". File not found or already deleted.");
    }
}
