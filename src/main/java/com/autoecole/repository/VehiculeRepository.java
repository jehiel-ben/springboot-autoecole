package com.autoecole.repository;

import com.autoecole.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    Optional<Vehicule> findByImmatriculation(String immatriculation);
    boolean existsByImmatriculation(String immatriculation);
    List<Vehicule> findByStatutEntretiens(String statut);

    // ⬇️ NOUVELLES MÉTHODES — CORBEILLE
    List<Vehicule> findBySupprimeFalse();
    List<Vehicule> findBySupprimeTrue();
}