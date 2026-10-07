package com.autoecole.controller;

import com.autoecole.dto.SeanceCoursRequest;
import com.autoecole.dto.SeanceCoursResponse;
import com.autoecole.service.SeanceCoursService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
public class SeanceCoursController {

    private final SeanceCoursService service;

    @PostMapping
    public ResponseEntity<SeanceCoursResponse> create(@Valid @RequestBody SeanceCoursRequest req) {
        return new ResponseEntity<>(service.create(req), HttpStatus.CREATED);
    }

    @GetMapping
    public List<SeanceCoursResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SeanceCoursResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PatchMapping("/{id}/statut")
    public SeanceCoursResponse updateStatut(@PathVariable Long id, @RequestParam String statut) {
        return service.updateStatut(id, statut);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}