package com.autoecole.repository;

import com.autoecole.entity.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {

    Optional<Eleve> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Eleve> findByStatut(String statut);
    List<Eleve> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    // ⬇️ NOUVELLES MÉTHODES — CORBEILLE
    List<Eleve> findBySupprimeFalse();
    List<Eleve> findBySupprimeTrue();
}