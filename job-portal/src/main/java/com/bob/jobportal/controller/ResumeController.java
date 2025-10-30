package com.bob.jobportal.controller;

import com.bob.db.dto.BulkResumeUploadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bob.db.dto.ApiResponse;
import com.bob.jobportal.service.ResumeService;

import io.swagger.v3.oas.annotations.Parameter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {

	@Autowired
	private ResumeService resumeService;

	private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "doc", "docx");

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<String>> uploadResume(
			@Parameter(description = "Resume file", required = true)
			@RequestParam("file") MultipartFile file) {
		try {
			String originalFilename = file.getOriginalFilename();
			if (originalFilename != null) {
				String fileExtension = "";
				int dotIndex = originalFilename.lastIndexOf('.');
				if (dotIndex > 0) {
					fileExtension = originalFilename.substring(dotIndex + 1);
				}
				if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
					ApiResponse<String> response = new ApiResponse<>(false, "Invalid file type. Only PDF, DOC, and DOCX files are allowed.", null);
					return ResponseEntity.badRequest().body(response);
				}
			}

			String result = resumeService.uploadResume(file);
			if (result == null) {
				ApiResponse<String> response = new ApiResponse<>(false, "File is empty", null);
				return ResponseEntity.badRequest().body(response);
			}
			ApiResponse<String> response = new ApiResponse<>(true, "File uploaded successfully", result);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			String stackTrace = sw.toString();
			ApiResponse<String> response = new ApiResponse<>(false, "File upload failed: " + ex.getMessage(), stackTrace);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/start-batch-process")
	public ResponseEntity<ApiResponse<String>> startBatchProcess() {
		try {
			String result = resumeService.startBatchProcess();
			if (result == null) {
				ApiResponse<String> response = new ApiResponse<>(false, "Batch process FAILED to start!", null);
				return ResponseEntity.badRequest().body(response);
			}
			ApiResponse<String> response = new ApiResponse<>(true, "Batch process started", result);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			String stackTrace = sw.toString();
			ApiResponse<String> response = new ApiResponse<>(false, "Batch process failed: " + ex.getMessage(), stackTrace);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<BulkResumeUploadDto>>> getResumesByUserId() {
		try {
			List<BulkResumeUploadDto> resumes = resumeService.getResumesByUserId();
			ApiResponse<List<BulkResumeUploadDto>> response = new ApiResponse<>(true, "Resumes fetched successfully", resumes);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			ApiResponse<List<BulkResumeUploadDto>> response = new ApiResponse<>(false, "Failed to fetch resumes: " + ex.getMessage(), null);
			return ResponseEntity.badRequest().body(response);
		}
	}

    @GetMapping("/testaccess")
    public ResponseEntity<ApiResponse<String>> testaccess(@RequestParam String folderPath){
        try {
            resumeService.testFolderAccess(folderPath);
            ApiResponse<String> response = new ApiResponse<>(true, "Folder created successfully", "");
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ApiResponse<String> response = new ApiResponse<>(false, "Folder creation failed: " + ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

	@DeleteMapping("/delete-resume/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBulkResumeUpload(@PathVariable("id") UUID id) {
        try {
            String result = resumeService.deleteBulkResumeUpload(id);
            ApiResponse<String> response = new ApiResponse<>(true, "Bulk resume upload deleted successfully", result);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ApiResponse<String> response = new ApiResponse<>(false, "Failed to delete bulk resume upload: " + ex.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }


}
