package com.bob.masterdata.Service;

import com.bob.db.dto.DocumentTypesDTO;
import com.bob.db.entity.DocumentTypesEntity;
import com.bob.db.mapper.DocumentTypesMapper;
import com.bob.db.repository.DocumentTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypesService {
    @Autowired
    private DocumentTypesRepository documentTypesRepository;

    @Autowired
    private DocumentTypesMapper documentTypesMapper;

    public DocumentTypesDTO addDocumentType(DocumentTypesDTO documentType) {
        DocumentTypesEntity entity = documentTypesMapper.toEntity(documentType);
        DocumentTypesEntity savedEntity = documentTypesRepository.save(entity);
        return documentTypesMapper.toDTO(savedEntity);
    }

    public List<DocumentTypesDTO> getAllDocumentTypes() {
        List<DocumentTypesEntity> entities = documentTypesRepository.findAllByIsActiveTrue();
        return documentTypesMapper.toDTOList(entities);
    }

    public DocumentTypesDTO updateDocumentType(Long id, DocumentTypesDTO documentType) {
        DocumentTypesEntity entity = documentTypesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document Type not found"));
        entity.setDocumentDesc(documentType.getDocumentDesc());
        entity.setDocumentName(documentType.getDocumentName());
        return documentTypesMapper.toDTO(documentTypesRepository.save(entity));
    }

    public DocumentTypesDTO deleteDocumentType(Long id) {
        DocumentTypesEntity entity = documentTypesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document Type not found"));
        entity.setActive(false);
        documentTypesRepository.save(entity);
        return documentTypesMapper.toDTO(entity);
    }
}
