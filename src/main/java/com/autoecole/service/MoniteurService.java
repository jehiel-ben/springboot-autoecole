package com.autoecole.service;

import com.autoecole.dto.MoniteurRequest;
import com.autoecole.entity.Moniteur;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.mapper.MoniteurMapper;
import com.autoecole.repository.MoniteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MoniteurService {

    private final MoniteurRepository repo;
    private final MoniteurMapper mapper;

    public Moniteur create(MoniteurRequest req) {
        if (repo.existsByNumPermis(req.getNumPermis())) {
            throw new IllegalArgumentException("Numéro de permis déjà utilisé : " + req.getNumPermis());
        }
        return repo.save(mapper.toEntity(req));
    }

    @Transactional(readOnly = true)
    public List<Moniteur> findAll() {
        return repo.findBySupprimeFalse();
    }

    @Transactional(readOnly = true)
    public List<Moniteur> findCorbeille() {
        return repo.findBySupprimeTrue();
    }

    @Transactional(readOnly = true)
    public List<Moniteur> findDisponibles() {
        return repo.findByStatutDispoTrue();
    }

    @Transactional(readOnly = true)
    public Moniteur findById(Long id) {
        return getEntity(id);
    }

    public Moniteur update(Long id, MoniteurRequest req) {
        Moniteur m = getEntity(id);
        mapper.updateEntity(m, req);
        return repo.save(m);
    }

    public void softDelete(Long id) {
        Moniteur m = getEntity(id);
        m.setSupprime(true);
        repo.save(m);
    }

    public void restore(Long id) {
        Moniteur m = getEntity(id);
        m.setSupprime(false);
        repo.save(m);
    }

    public void hardDelete(Long id) {
        repo.delete(getEntity(id));
    }

    public Moniteur getEntity(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Moniteur introuvable : id=" + id));
    }
}