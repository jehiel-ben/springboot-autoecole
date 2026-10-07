package com.autoecole.service;

import com.autoecole.dto.SeanceCoursRequest;
import com.autoecole.dto.SeanceCoursResponse;
import com.autoecole.entity.Eleve;
import com.autoecole.entity.Moniteur;
import com.autoecole.entity.SeanceCours;
import com.autoecole.entity.Vehicule;
import com.autoecole.exception.ConflitPlanningException;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.mapper.SeanceCoursMapper;
import com.autoecole.repository.SeanceCoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeanceCoursService {

    private final SeanceCoursRepository repo;
    private final EleveService eleveService;
    private final MoniteurService moniteurService;
    private final VehiculeService vehiculeService;
    private final SeanceCoursMapper mapper;

    public SeanceCoursResponse create(SeanceCoursRequest req) {

        if (!req.getDateHeureFin().isAfter(req.getDateHeureDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }

        Eleve eleve = eleveService.getEntity(req.getEleveId());
        Moniteur moniteur = moniteurService.getEntity(req.getMoniteurId());

        Vehicule vehicule = null;
        if (req.getVehiculeId() != null) {
            vehicule = vehiculeService.getEntity(req.getVehiculeId());
        }

        if ("CONDUITE".equalsIgnoreCase(req.getTypeCours()) && vehicule == null) {
            throw new IllegalArgumentException("Un véhicule est obligatoire pour une séance de CONDUITE");
        }

        List<SeanceCours> conflitsMoniteur = repo.findConflitsMoniteur(
                moniteur.getId(), req.getDateHeureDebut(), req.getDateHeureFin());
        if (!conflitsMoniteur.isEmpty()) {
            throw new ConflitPlanningException("Le moniteur est déjà occupé sur ce créneau");
        }

        if (vehicule != null) {
            List<SeanceCours> conflitsVehicule = repo.findConflitsVehicule(
                    vehicule.getId(), req.getDateHeureDebut(), req.getDateHeureFin());
            if (!conflitsVehicule.isEmpty()) {
                throw new ConflitPlanningException("Le véhicule est déjà utilisé sur ce créneau");
            }
        }

        SeanceCours seance = SeanceCours.builder()
                .dateHeureDebut(req.getDateHeureDebut())
                .dateHeureFin(req.getDateHeureFin())
                .typeCours(req.getTypeCours().toUpperCase())
                .statut("PLANIFIEE")
                .eleve(eleve)
                .moniteur(moniteur)
                .vehicule(vehicule)
                .build();

        return mapper.toResponse(repo.save(seance));
    }

    @Transactional(readOnly = true)
    public List<SeanceCoursResponse> findAll() {
        return repo.findBySupprimeFalse().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SeanceCoursResponse> findCorbeille() {
        return repo.findBySupprimeTrue().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SeanceCoursResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public SeanceCoursResponse updateStatut(Long id, String statut) {
        SeanceCours s = getEntity(id);
        s.setStatut(statut.toUpperCase());
        return mapper.toResponse(repo.save(s));
    }

    public void softDelete(Long id) {
        SeanceCours s = getEntity(id);
        s.setSupprime(true);
        repo.save(s);
    }

    public void restore(Long id) {
        SeanceCours s = getEntity(id);
        s.setSupprime(false);
        repo.save(s);
    }

    public void hardDelete(Long id) {
        repo.delete(getEntity(id));
    }

    public SeanceCours getEntity(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Séance introuvable : id=" + id));
    }
}