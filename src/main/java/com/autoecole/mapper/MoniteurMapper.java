package com.autoecole.mapper;

import com.autoecole.dto.MoniteurRequest;
import com.autoecole.entity.Moniteur;
import org.springframework.stereotype.Component;

@Component
public class MoniteurMapper {

    public Moniteur toEntity(MoniteurRequest req) {
        return Moniteur.builder()
                .nom(req.getNom())
                .prenom(req.getPrenom())
                .numPermis(req.getNumPermis())
                .telephone(req.getTelephone())
                .email(req.getEmail())
                .statutDispo(req.getStatutDispo() != null ? req.getStatutDispo() : true)
                .build();
    }

    public void updateEntity(Moniteur m, MoniteurRequest req) {
        m.setNom(req.getNom());
        m.setPrenom(req.getPrenom());
        m.setNumPermis(req.getNumPermis());
        m.setTelephone(req.getTelephone());
        m.setEmail(req.getEmail());
        if (req.getStatutDispo() != null) m.setStatutDispo(req.getStatutDispo());
    }
}