package com.autoecole.mapper;

import com.autoecole.dto.SeanceCoursResponse;
import com.autoecole.entity.SeanceCours;
import org.springframework.stereotype.Component;

@Component
public class SeanceCoursMapper {

    public SeanceCoursResponse toResponse(SeanceCours s) {
        return SeanceCoursResponse.builder()
                .id(s.getId())
                .dateHeureDebut(s.getDateHeureDebut())
                .dateHeureFin(s.getDateHeureFin())
                .typeCours(s.getTypeCours())
                .statut(s.getStatut())
                .eleveId(s.getEleve().getId())
                .eleveNomComplet(s.getEleve().getPrenom() + " " + s.getEleve().getNom())
                .moniteurId(s.getMoniteur().getId())
                .moniteurNomComplet(s.getMoniteur().getPrenom() + " " + s.getMoniteur().getNom())
                .vehiculeId(s.getVehicule() != null ? s.getVehicule().getId() : null)
                .vehiculeImmatriculation(s.getVehicule() != null ? s.getVehicule().getImmatriculation() : null)
                .build();
    }
}