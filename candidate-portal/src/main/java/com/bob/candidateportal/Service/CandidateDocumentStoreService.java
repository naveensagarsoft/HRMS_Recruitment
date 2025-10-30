package com.bob.candidateportal.Service;

import com.bob.commonutil.service.FileService;
import com.bob.db.dto.CandidateDocumentStoreDTO;
import com.bob.db.entity.CandidateDocumentStoreEntity;
import com.bob.db.mapper.CandidateDocumentStoreMapper;
import com.bob.db.repository.CandidateDocumentStoreRepository;
import com.bob.db.repository.DocumentTypesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CandidateDocumentStoreService {

    @Value("${candidate.document.upload.path}")
    private String uploadDir;

    @Value("${resume.http.url}")
    private String resumeHTTPUrl;

    @Autowired
    private CandidateDocumentStoreRepository candidateDocumentStoreRepository;

    @Autowired
    private CandidateDocumentStoreMapper candidateDocumentStoreMapper;

    @Autowired
    private DocumentTypesRepository documentTypesRepository;

    @Autowired
    private FileService fileService; // <-- Add this line

    @Transactional
    public CandidateDocumentStoreDTO uploadDocument(MultipartFile file, UUID candidateId, Long documentId,String others) throws IOException {
        // 1. Validate
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new RuntimeException("Upload directory is not configured");
        }

        // 2. Find document name (for unique filename)
        String documentName = (others == null || others.trim().equals("")) ? documentTypesRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document Type not found"))
                .getDocumentName() : others;
        String uniqueFilename = candidateId+"_"+documentName.replace(" ", "_");

        // 3. Create candidate-specific folder
        Path savedPath;
        try {
            savedPath = fileService.uploadFile(file, uniqueFilename, uploadDir, candidateId.toString());
        } catch (Exception e) {
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }

        String fileStoredPath = savedPath.toAbsolutePath().toString();
        String fileHTTPUrl = resumeHTTPUrl+"/"+fileStoredPath.replace(uploadDir,"").replace("\\","/");

        // 5. Find existing record (if any)
        CandidateDocumentStoreEntity documentEntity =
                candidateDocumentStoreRepository.findByCandidateIdAndDocumentIdAndFileName(candidateId, documentId, savedPath.getFileName().toString())
                        .orElse(null);

        //String p ="C:\\Users\\mahesh.allvar\\Desktop\\Candidate_doc\\c7f29871-4146-4479-8e4a-8849426c4b9b\\doc1.png";

        if (documentEntity == null) {
            // ---- CREATE ----
            documentEntity = new CandidateDocumentStoreEntity();
            //documentEntity.setDocumentStoreId(UUID.randomUUID());
            documentEntity.setCandidateId(candidateId);
            documentEntity.setDocumentId(documentId);
        }

        // Common fields for both create/update
        documentEntity.setFileName(savedPath.getFileName().toString());
        documentEntity.setFileUrl(fileHTTPUrl);

        // Save entity
        CandidateDocumentStoreEntity savedEntity = candidateDocumentStoreRepository.save(documentEntity);

        return candidateDocumentStoreMapper.toDTO(savedEntity);
    }



    public List<CandidateDocumentStoreDTO> getAllDocuments() {
        List<CandidateDocumentStoreEntity> entities = candidateDocumentStoreRepository.findAll();
        return candidateDocumentStoreMapper.toDTOList(entities);
    }
    @Transactional
    public CandidateDocumentStoreDTO updateDocument(UUID id, CandidateDocumentStoreDTO document) {
        CandidateDocumentStoreEntity entity = candidateDocumentStoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document Details not found"));
        entity.setCandidateId(document.getCandidateId());
        if (document.getCandidateId() != null) {
            entity.setCandidateId(document.getCandidateId());
        }
        entity.setFileUrl(document.getFileUrl());
        entity.setFileName(document.getFileName());
        entity.setFileUrl(document.getFileUrl());

        return candidateDocumentStoreMapper.toDTO(candidateDocumentStoreRepository.save(entity));

    }

    @Transactional
    public void deleteDocument(UUID documentStoreId ) throws IOException {
        CandidateDocumentStoreEntity documentEntity = candidateDocumentStoreRepository
                .findById(documentStoreId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Delete physical file
        if (documentEntity.getFileUrl() != null) {
            Path filePath = Paths.get(documentEntity.getFileUrl());
            Files.deleteIfExists(filePath);
        }

        // Delete DB record
        candidateDocumentStoreRepository.delete(documentEntity);
    }

    public List<CandidateDocumentStoreDTO> getByCandidateId(UUID candidateId) {
        List<CandidateDocumentStoreEntity> entities = candidateDocumentStoreRepository.findAllByCandidateId(
               candidateId
        );
        return candidateDocumentStoreMapper.toDTOList(entities);
    }
}
