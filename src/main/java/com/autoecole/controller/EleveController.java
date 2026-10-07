package com.autoecole.controller;

import com.autoecole.dto.EleveRequest;
import com.autoecole.dto.EleveResponse;
import com.autoecole.service.EleveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eleves")
@RequiredArgsConstructor
public class EleveController {

    private final EleveService service;

    @PostMapping
    public ResponseEntity<EleveResponse> create(@Valid @RequestBody EleveRequest req) {
        return new ResponseEntity<>(service.create(req), HttpStatus.CREATED);
    }

    @GetMapping
    public List<EleveResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EleveResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public EleveResponse update(@PathVariable Long id, @Valid @RequestBody EleveRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiver(@PathVariable Long id) {
        service.desactiver(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}