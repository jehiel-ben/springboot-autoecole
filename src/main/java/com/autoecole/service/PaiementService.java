package com.autoecole.service;

import com.autoecole.dto.PaiementRequest;
import com.autoecole.dto.PaiementResponse;
import com.autoecole.entity.Eleve;
import com.autoecole.entity.Paiement;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.mapper.PaiementMapper;
import com.autoecole.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementService {

    private final PaiementRepository repo;
    private final EleveService eleveService;
    private final PaiementMapper mapper;

    public PaiementResponse create(PaiementRequest req) {
        Eleve eleve = eleveService.getEntity(req.getEleveId());
        Paiement p = Paiement.builder()
                .montant(req.getMontant())
                .datePaiement(req.getDatePaiement())
                .modePaiement(req.getModePaiement())
                .typeFormule(req.getTypeFormule())
                .eleve(eleve)
                .build();
        return mapper.toResponse(repo.save(p));
    }

    @Transactional(readOnly = true)
    public List<PaiementResponse> findAll() {
        return repo.findBySupprimeFalse().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaiementResponse> findCorbeille() {
        return repo.findBySupprimeTrue().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaiementResponse> findByEleve(Long eleveId) {
        return repo.findByEleveId(eleveId).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public void softDelete(Long id) {
        Paiement p = getEntity(id);
        p.setSupprime(true);
        repo.save(p);
    }

    public void restore(Long id) {
        Paiement p = getEntity(id);
        p.setSupprime(false);
        repo.save(p);
    }

    public void hardDelete(Long id) {
        repo.delete(getEntity(id));
    }

    public Paiement getEntity(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Paiement introuvable : id=" + id));
    }
}