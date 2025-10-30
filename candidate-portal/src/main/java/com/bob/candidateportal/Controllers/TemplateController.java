package com.bob.candidateportal.Controllers;

import com.bob.candidateportal.Service.TemplateService;
import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.TemplatesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TemplateController {

    @Autowired
    private TemplateService templateService;
    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<TemplatesDTO>>> getTemplates() throws Exception {
        List<TemplatesDTO> templatesList = templateService.getAllTemplates();
        if (templatesList.isEmpty()) {
            throw new Exception("No templates found");
        }
        return new ResponseEntity<>(
                new ApiResponse<>(true, "Templates found", templatesList),
                HttpStatus.OK);
    }
}
