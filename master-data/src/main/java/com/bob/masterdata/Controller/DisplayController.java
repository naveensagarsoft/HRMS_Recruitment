package com.bob.masterdata.Controller;

import com.bob.masterdata.Model.GetCompleteDataResponse;
import com.bob.masterdata.Service.DisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class DisplayController {
    @Autowired
    private DisplayService displayService;

    @GetMapping("/all")
    public GetCompleteDataResponse getCompleteData2(){
        return displayService.getAllData3();
    }

    @GetMapping("/file")
    public String getAllCities(@RequestParam String filePath) throws Exception {
        try {
            Path path = Paths.get(filePath);
            return Files.readString(path);
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }
}
