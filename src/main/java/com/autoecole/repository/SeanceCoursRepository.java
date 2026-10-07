package com.autoecole.repository;

import com.autoecole.entity.SeanceCours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeanceCoursRepository extends JpaRepository<SeanceCours, Long> {

    List<SeanceCours> findByEleveId(Long eleveId);
    List<SeanceCours> findByMoniteurId(Long moniteurId);
    List<SeanceCours> findByVehiculeId(Long vehiculeId);
    List<SeanceCours> findByTypeCours(String typeCours);
    List<SeanceCours> findByStatut(String statut);

    // ⬇️ NOUVELLES MÉTHODES — CORBEILLE
    List<SeanceCours> findBySupprimeFalse();
    List<SeanceCours> findBySupprimeTrue();

    @Query("""
        SELECT s FROM SeanceCours s
        WHERE s.moniteur.id = :moniteurId
          AND s.statut <> 'ANNULEE'
          AND s.supprime = false
          AND (s.dateHeureDebut < :fin AND s.dateHeureFin > :debut)
    """)
    List<SeanceCours> findConflitsMoniteur(@Param("moniteurId") Long moniteurId,
                                           @Param("debut") LocalDateTime debut,
                                           @Param("fin") LocalDateTime fin);

    @Query("""
        SELECT s FROM SeanceCours s
        WHERE s.vehicule.id = :vehiculeId
          AND s.statut <> 'ANNULEE'
          AND s.supprime = false
          AND (s.dateHeureDebut < :fin AND s.dateHeureFin > :debut)
    """)
    List<SeanceCours> findConflitsVehicule(@Param("vehiculeId") Long vehiculeId,
                                           @Param("debut") LocalDateTime debut,
                                           @Param("fin") LocalDateTime fin);
}