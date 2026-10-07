package com.autoecole.controller;

import com.autoecole.dto.PaiementRequest;
import com.autoecole.dto.PaiementResponse;
import com.autoecole.service.PaiementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService service;

    @PostMapping
    public ResponseEntity<PaiementResponse> create(@Valid @RequestBody PaiementRequest req) {
        return new ResponseEntity<>(service.create(req), HttpStatus.CREATED);
    }

    @GetMapping
    public List<PaiementResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/eleve/{eleveId}")
    public List<PaiementResponse> findByEleve(@PathVariable Long eleveId) {
        return service.findByEleve(eleveId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}