package com.autoecole.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moniteurs", uniqueConstraints = {
        @UniqueConstraint(columnNames = "num_permis")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moniteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 80)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false, length = 80)
    private String prenom;

    @NotBlank(message = "Le numéro de permis est obligatoire")
    @Column(name = "num_permis", nullable = false, unique = true, length = 50)
    private String numPermis;

    @Column(length = 20)
    private String telephone;

    @Email(message = "Email invalide")
    @Column(length = 150)
    private String email;

    @Column(name = "statut_dispo", nullable = false)
    @Builder.Default
    private Boolean statutDispo = true;

    // ⬇️ NOUVEAU CHAMP — CORBEILLE
    @Column(nullable = false)
    @Builder.Default
    private Boolean supprime = false;

    @OneToMany(mappedBy = "moniteur", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SeanceCours> seances = new ArrayList<>();
}