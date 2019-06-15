package com.masiblue.backend.controller;

import com.masiblue.backend.exception.IncorrectUsabilityDataFormat;
import com.masiblue.backend.model.UsabilityData;
import com.masiblue.backend.service.UsabilityDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/metrics")
public class UsabilityDataController {

    private UsabilityDataService usabilityDataService;

    public UsabilityDataController(UsabilityDataService usabilityDataService) {
        this.usabilityDataService = usabilityDataService;
    }

    @PostMapping
    public ResponseEntity<String> addNewPosition(Authentication auth, @RequestBody UsabilityData usabilityData) {
        try {
            usabilityDataService.addUsabilityData(auth.getName(), usabilityData);
        } catch (IncorrectUsabilityDataFormat ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
        return new ResponseEntity<>("Successfully added new usability data", HttpStatus.OK);
    }

    @PostMapping(value = "/screenshot")
    public ResponseEntity<String> uploadScreenshot(Authentication auth, @RequestBody String img) {
        String fileName;
        try {
            fileName = usabilityDataService.saveScreenshot(auth.getName(), img);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect screenshot data format");
        }
        return new ResponseEntity<>(String.format("Successfully saved the screenshot as %s file", fileName), HttpStatus.OK);
    }
}
