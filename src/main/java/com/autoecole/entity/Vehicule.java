package com.autoecole.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicules", uniqueConstraints = {
        @UniqueConstraint(columnNames = "immatriculation")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'immatriculation est obligatoire")
    @Column(nullable = false, unique = true, length = 20)
    private String immatriculation;

    @NotBlank(message = "La marque est obligatoire")
    @Column(nullable = false, length = 50)
    private String marque;

    @Column(length = 50)
    private String modele;

    @Column(name = "type_boite", length = 20)
    private String typeBoite;

    @Column(name = "statut_entretiens", nullable = false, length = 30)
    @Builder.Default
    private String statutEntretiens = "DISPONIBLE";

    // ⬇️ NOUVEAU CHAMP — CORBEILLE
    @Column(nullable = false)
    @Builder.Default
    private Boolean supprime = false;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SeanceCours> seances = new ArrayList<>();
}