package com.autoecole.controller;

import com.autoecole.dto.MoniteurRequest;
import com.autoecole.entity.Moniteur;
import com.autoecole.service.MoniteurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moniteurs")
@RequiredArgsConstructor
public class MoniteurController {

    private final MoniteurService service;

    @PostMapping
    public ResponseEntity<Moniteur> create(@Valid @RequestBody MoniteurRequest req) {
        return new ResponseEntity<>(service.create(req), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Moniteur> findAll() {
        return service.findAll();
    }

    @GetMapping("/disponibles")
    public List<Moniteur> findDisponibles() {
        return service.findDisponibles();
    }

    @GetMapping("/{id}")
    public Moniteur findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Moniteur update(@PathVariable Long id, @Valid @RequestBody MoniteurRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}