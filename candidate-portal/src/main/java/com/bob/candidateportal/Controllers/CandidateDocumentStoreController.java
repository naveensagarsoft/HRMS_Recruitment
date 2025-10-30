package com.bob.candidateportal.Controllers;

import com.bob.candidateportal.Service.CandidateDocumentStoreService;
import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.CandidateDocumentStoreDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/candidate-document-store")
@Slf4j
public class CandidateDocumentStoreController {

    @Autowired
    private CandidateDocumentStoreService candidateDocumentStoreService;
    @PostMapping(value = "/upload/{candidateId}/{documentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadDocument(@RequestParam("file") MultipartFile file, @PathVariable UUID candidateId,
                                                         @PathVariable Long documentId,
                                                         @RequestParam(value = "others", required = false) String others
    ) throws IOException {

        CandidateDocumentStoreDTO uploadedDocument = candidateDocumentStoreService.uploadDocument(file, candidateId, documentId,others);
        ApiResponse<?> response = new ApiResponse<>(true, "Document uploaded successfully", uploadedDocument );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllDocuments() {
        List<CandidateDocumentStoreDTO> documents = candidateDocumentStoreService.getAllDocuments();
        ApiResponse<?> response = new ApiResponse<>(true, "Fetched all documents successfully", documents );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/documents/{candidateId}")
    public ResponseEntity<ApiResponse<?>> getByCandidateId(@PathVariable UUID candidateId) {
        List<CandidateDocumentStoreDTO> documents = candidateDocumentStoreService.getByCandidateId(candidateId);
        ApiResponse<?> response = new ApiResponse<>(true, "Fetched all documents successfully", documents );
        return ResponseEntity.ok(response);
    }

//    @DeleteMapping("/delete/{candidateId}/{documentId}")
//    public ResponseEntity<ApiResponse<?>> deleteDocument(
//            @PathVariable UUID candidateId,
//            @PathVariable Long documentId) {
//
//        try {
//            candidateDocumentStoreService.deleteDocument(candidateId, documentId);
//            return ResponseEntity.ok(new ApiResponse<>(true, "Document deleted successfully", null));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<ApiResponse<?>> updateDocument(@PathVariable UUID id, @RequestBody CandidateDocumentStoreDTO document) {
//        CandidateDocumentStoreDTO updatedDocument = candidateDocumentStoreService.updateDocument(id, document);
//        log.info("Updated Document: {}", updatedDocument.getCandidateId());
//        ApiResponse<?> response = new ApiResponse<>(true, "Document updated successfully", updatedDocument );
//        return ResponseEntity.ok(response);
//    }

    @DeleteMapping("/delete/{documentStoreId}")
    public ResponseEntity<ApiResponse<?>> deleteDocument(@PathVariable UUID documentStoreId) throws IOException {
        candidateDocumentStoreService.deleteDocument(documentStoreId);
        ApiResponse<?> response = new ApiResponse<>(true, "Document deleted successfully", null );
        return ResponseEntity.ok(response);
    }
}
