package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.DocumentTypesDTO;
import com.bob.db.entity.DocumentTypesEntity;
import com.bob.masterdata.Service.DocumentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypesController {

    @Autowired
    private DocumentTypesService documentTypesService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<DocumentTypesDTO>> addDocumentType(@RequestBody DocumentTypesDTO documentType) {
        DocumentTypesDTO createdDocumentType = documentTypesService.addDocumentType(documentType);
        ApiResponse<DocumentTypesDTO> response = new ApiResponse<>(true, "Document type added successfully", createdDocumentType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllDocumentTypes() {
        List<DocumentTypesDTO> documentTypes = documentTypesService.getAllDocumentTypes();
        ApiResponse<?> response = new ApiResponse<>(true, "Fetched all document types successfully", documentTypes);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<DocumentTypesDTO>> updateDocumentType(@PathVariable Long id, @RequestBody DocumentTypesDTO documentType) {
        DocumentTypesDTO updatedDocumentType = documentTypesService.updateDocumentType(id, documentType);
        ApiResponse<DocumentTypesDTO> response = new ApiResponse<>(true, "Document type updated successfully", updatedDocumentType);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<DocumentTypesDTO>> deleteDocumentType(@PathVariable Long id) {
        DocumentTypesDTO deletedDocumentType = documentTypesService.deleteDocumentType(id);
        ApiResponse<DocumentTypesDTO> response = new ApiResponse<>(true, "Document type deleted successfully", deletedDocumentType);
        return ResponseEntity.ok(response);
    }

}
