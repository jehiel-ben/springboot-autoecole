package com.autoecole.repository;

import com.autoecole.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByEleveId(Long eleveId);

    // ⬇️ NOUVELLES MÉTHODES — CORBEILLE
    List<Paiement> findBySupprimeFalse();
    List<Paiement> findBySupprimeTrue();
}