package com.autoecole.service;

import com.autoecole.dto.VehiculeRequest;
import com.autoecole.entity.Vehicule;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.mapper.VehiculeMapper;
import com.autoecole.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeService {

    private final VehiculeRepository repo;
    private final VehiculeMapper mapper;

    public Vehicule create(VehiculeRequest req) {
        if (repo.existsByImmatriculation(req.getImmatriculation())) {
            throw new IllegalArgumentException("Immatriculation déjà utilisée : " + req.getImmatriculation());
        }
        return repo.save(mapper.toEntity(req));
    }

    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        return repo.findBySupprimeFalse();
    }

    @Transactional(readOnly = true)
    public List<Vehicule> findCorbeille() {
        return repo.findBySupprimeTrue();
    }

    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        return getEntity(id);
    }

    public Vehicule update(Long id, VehiculeRequest req) {
        Vehicule v = getEntity(id);
        mapper.updateEntity(v, req);
        return repo.save(v);
    }

    public void softDelete(Long id) {
        Vehicule v = getEntity(id);
        v.setSupprime(true);
        repo.save(v);
    }

    public void restore(Long id) {
        Vehicule v = getEntity(id);
        v.setSupprime(false);
        repo.save(v);
    }

    public void hardDelete(Long id) {
        repo.delete(getEntity(id));
    }

    public Vehicule getEntity(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Véhicule introuvable : id=" + id));
    }
}