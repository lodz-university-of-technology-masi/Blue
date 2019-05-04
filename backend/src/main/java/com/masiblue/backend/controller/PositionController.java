package com.masiblue.backend.controller;

import com.masiblue.backend.exception.PositionAlreadyExistsException;
import com.masiblue.backend.exception.PositionNotFoundException;
import com.masiblue.backend.exception.PositionNotValidException;
import com.masiblue.backend.model.Position;
import com.masiblue.backend.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/positions")
public class PositionController {
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping
        public ResponseEntity<String> addNewPosition(@RequestBody Position position) {
        try {
            positionService.add(position);
            return new ResponseEntity<>("Successfully created new position", HttpStatus.OK);
        } catch (PositionAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There already exists this position", ex);
        }
    }

    @PreAuthorize("hasAnyRole('MODERATOR','REDACTOR','USER')")
    @GetMapping
    public ResponseEntity listPositions() {
        return new ResponseEntity<>(positionService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('MODERATOR','REDACTOR','USER')")
    @GetMapping("/{id}")
    public ResponseEntity listSinglePosition(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(positionService.findById(id), HttpStatus.OK);
        } catch (PositionNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no position with this id", ex);
        }
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity deletePosition(@PathVariable("id") long id) {
        try {
            positionService.delete(id);
            return new ResponseEntity<>("Successfully deleted position", HttpStatus.OK);
        } catch (PositionNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no position with this id", ex);
        }
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity updatePosition(@PathVariable("id") long id, @RequestBody Position newPositionData) {
        try {
            positionService.update(id, newPositionData);
            return new ResponseEntity<>("Successfully updated position", HttpStatus.OK);
        } catch (PositionAlreadyExistsException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is already position to which you want update", ex);
        } catch (PositionNotValidException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The data sent in request is not valid", ex);
        } catch (PositionNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no position with this id", ex);
        }
    }

}