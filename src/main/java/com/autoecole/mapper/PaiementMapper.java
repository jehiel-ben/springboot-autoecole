package com.autoecole.mapper;

import com.autoecole.dto.PaiementResponse;
import com.autoecole.entity.Paiement;
import org.springframework.stereotype.Component;

@Component
public class PaiementMapper {

    public PaiementResponse toResponse(Paiement p) {
        return PaiementResponse.builder()
                .id(p.getId())
                .montant(p.getMontant())
                .datePaiement(p.getDatePaiement())
                .modePaiement(p.getModePaiement())
                .typeFormule(p.getTypeFormule())
                .eleveId(p.getEleve().getId())
                .eleveNomComplet(p.getEleve().getPrenom() + " " + p.getEleve().getNom())
                .build();
    }
}