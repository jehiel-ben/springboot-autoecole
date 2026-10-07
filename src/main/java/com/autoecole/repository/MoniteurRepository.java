package com.autoecole.repository;

import com.autoecole.entity.Moniteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoniteurRepository extends JpaRepository<Moniteur, Long> {

    Optional<Moniteur> findByNumPermis(String numPermis);
    boolean existsByNumPermis(String numPermis);
    List<Moniteur> findByStatutDispoTrue();

    // ⬇️ NOUVELLES MÉTHODES — CORBEILLE
    List<Moniteur> findBySupprimeFalse();
    List<Moniteur> findBySupprimeTrue();
}