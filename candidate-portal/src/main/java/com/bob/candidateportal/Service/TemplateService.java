package com.bob.candidateportal.Service;

import com.bob.db.dto.TemplatesDTO;
import com.bob.db.mapper.TemplatesMapper;
import com.bob.db.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplatesMapper templatesMapper;

    public List<TemplatesDTO> getAllTemplates() {
        return templatesMapper.toDtoList(templateRepository.findAll());
    }
}
