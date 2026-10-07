package com.autoecole.service;

import com.autoecole.dto.EleveRequest;
import com.autoecole.dto.EleveResponse;
import com.autoecole.entity.Eleve;
import com.autoecole.exception.EmailAlreadyExistsException;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.mapper.EleveMapper;
import com.autoecole.repository.EleveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EleveService {

    private final EleveRepository repo;
    private final EleveMapper mapper;

    public EleveResponse create(EleveRequest req) {
        if (repo.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyExistsException("Email déjà utilisé : " + req.getEmail());
        }
        Eleve eleve = mapper.toEntity(req);
        return mapper.toResponse(repo.save(eleve));
    }

    @Transactional(readOnly = true)
    public List<EleveResponse> findAll() {
        return repo.findBySupprimeFalse().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EleveResponse> findCorbeille() {
        return repo.findBySupprimeTrue().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EleveResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public EleveResponse update(Long id, EleveRequest req) {
        Eleve eleve = getEntity(id);
        if (!eleve.getEmail().equals(req.getEmail()) && repo.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyExistsException("Email déjà utilisé : " + req.getEmail());
        }
        mapper.updateEntity(eleve, req);
        return mapper.toResponse(repo.save(eleve));
    }

    public void desactiver(Long id) {
        Eleve e = getEntity(id);
        e.setStatut("INACTIF");
        repo.save(e);
    }

    /** Met l'élève à la corbeille (soft delete). */
    public void softDelete(Long id) {
        Eleve e = getEntity(id);
        e.setSupprime(true);
        repo.save(e);
    }

    /** Restaure un élève depuis la corbeille. */
    public void restore(Long id) {
        Eleve e = getEntity(id);
        e.setSupprime(false);
        repo.save(e);
    }

    /** Supprime définitivement (hard delete). */
    public void hardDelete(Long id) {
        repo.delete(getEntity(id));
    }

    public Eleve getEntity(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Élève introuvable : id=" + id));
    }
}