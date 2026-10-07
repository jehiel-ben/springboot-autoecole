package com.autoecole.mapper;

import com.autoecole.dto.VehiculeRequest;
import com.autoecole.entity.Vehicule;
import org.springframework.stereotype.Component;

@Component
public class VehiculeMapper {

    public Vehicule toEntity(VehiculeRequest req) {
        return Vehicule.builder()
                .immatriculation(req.getImmatriculation())
                .marque(req.getMarque())
                .modele(req.getModele())
                .typeBoite(req.getTypeBoite())
                .statutEntretiens(req.getStatutEntretiens() != null ? req.getStatutEntretiens() : "DISPONIBLE")
                .build();
    }

    public void updateEntity(Vehicule v, VehiculeRequest req) {
        v.setImmatriculation(req.getImmatriculation());
        v.setMarque(req.getMarque());
        v.setModele(req.getModele());
        v.setTypeBoite(req.getTypeBoite());
        if (req.getStatutEntretiens() != null) v.setStatutEntretiens(req.getStatutEntretiens());
    }
}