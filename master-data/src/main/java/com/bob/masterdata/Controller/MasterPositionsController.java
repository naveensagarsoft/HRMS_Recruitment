package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.MasterPositionsDTO;
import com.bob.masterdata.Service.MasterPositionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-positions")
public class MasterPositionsController {

    @Autowired
    private MasterPositionsService masterPositionsService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<MasterPositionsDTO>> create(@RequestBody MasterPositionsDTO dto) {
        MasterPositionsDTO created = masterPositionsService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Created successfully", created));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<MasterPositionsDTO>> update(@PathVariable Long id, @RequestBody MasterPositionsDTO dto) {
        MasterPositionsDTO updated = masterPositionsService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Updated successfully", updated));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MasterPositionsDTO>>> getAll() {
        List<MasterPositionsDTO> list = masterPositionsService.getAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Success", list));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        masterPositionsService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deleted successfully", null));
    }
}
