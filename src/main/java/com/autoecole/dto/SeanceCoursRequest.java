package com.autoecole.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeanceCoursRequest {

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime dateHeureDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateHeureFin;

    @NotBlank(message = "Le type de cours est obligatoire")
    private String typeCours;

    @NotNull(message = "L'élève est obligatoire")
    private Long eleveId;

    @NotNull(message = "Le moniteur est obligatoire")
    private Long moniteurId;

    private Long vehiculeId;
}