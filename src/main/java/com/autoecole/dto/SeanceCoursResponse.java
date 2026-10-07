package com.autoecole.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeanceCoursResponse {

    private Long id;
    private LocalDateTime dateHeureDebut;
    private LocalDateTime dateHeureFin;
    private String typeCours;
    private String statut;

    private Long eleveId;
    private String eleveNomComplet;

    private Long moniteurId;
    private String moniteurNomComplet;

    private Long vehiculeId;
    private String vehiculeImmatriculation;
}