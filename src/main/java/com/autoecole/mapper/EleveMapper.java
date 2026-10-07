package com.autoecole.mapper;

import com.autoecole.dto.EleveRequest;
import com.autoecole.dto.EleveResponse;
import com.autoecole.entity.Eleve;
import org.springframework.stereotype.Component;

@Component
public class EleveMapper {

    public Eleve toEntity(EleveRequest req) {
        return Eleve.builder()
                .nom(req.getNom())
                .prenom(req.getPrenom())
                .email(req.getEmail())
                .telephone(req.getTelephone())
                .dateNaissance(req.getDateNaissance())
                .statut("ACTIF")
                .build();
    }

    public void updateEntity(Eleve eleve, EleveRequest req) {
        eleve.setNom(req.getNom());
        eleve.setPrenom(req.getPrenom());
        eleve.setEmail(req.getEmail());
        eleve.setTelephone(req.getTelephone());
        eleve.setDateNaissance(req.getDateNaissance());
    }

    public EleveResponse toResponse(Eleve e) {
        return EleveResponse.builder()
                .id(e.getId())
                .nom(e.getNom())
                .prenom(e.getPrenom())
                .email(e.getEmail())
                .telephone(e.getTelephone())
                .dateNaissance(e.getDateNaissance())
                .dateInscription(e.getDateInscription())
                .statut(e.getStatut())
                .build();
    }
}