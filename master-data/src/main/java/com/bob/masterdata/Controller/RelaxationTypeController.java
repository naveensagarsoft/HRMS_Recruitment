package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.RelaxationTypesDTO;
import com.bob.masterdata.Service.RelaxationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relaxation-type")
public class RelaxationTypeController {
    @Autowired
    private RelaxationTypeService relaxationTypeService;
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<RelaxationTypesDTO>> addRelaxationType(@RequestBody RelaxationTypesDTO dto) {
        RelaxationTypesDTO created = relaxationTypeService.addRelaxationType(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Relaxation Type added successfully", created));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<RelaxationTypesDTO>> updateRelaxationType(@PathVariable Integer id, @RequestBody RelaxationTypesDTO dto) {
        RelaxationTypesDTO updated = relaxationTypeService.updateRelaxationType(id,dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Relaxation Type updated successfully", updated));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RelaxationTypesDTO>>> getAllRelaxationTypes() {
        List<RelaxationTypesDTO> allRelaxationTypes = relaxationTypeService.getAllRelaxationTypes();
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all Relaxation Types", allRelaxationTypes));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRelaxationType(@PathVariable Integer id) {
        relaxationTypeService.deleteRelaxationType(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Relaxation Type deleted successfully", null));
    }

}
