package com.autoecole.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seances_cours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeanceCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_heure_debut", nullable = false)
    private LocalDateTime dateHeureDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "date_heure_fin", nullable = false)
    private LocalDateTime dateHeureFin;

    @NotBlank(message = "Le type de cours est obligatoire")
    @Column(name = "type_cours", nullable = false, length = 20)
    private String typeCours;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String statut = "PLANIFIEE";

    // ⬇️ NOUVEAU CHAMP — CORBEILLE
    @Column(nullable = false)
    @Builder.Default
    private Boolean supprime = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "moniteur_id", nullable = false)
    private Moniteur moniteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;
}