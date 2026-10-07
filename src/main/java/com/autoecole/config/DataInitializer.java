package com.autoecole.config;

import com.autoecole.entity.Utilisateur;
import com.autoecole.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crée un utilisateur admin avec le nom "M. Bahizire"
        Utilisateur admin = utilisateurRepository.findByUsername("admin").orElse(null);

        if (admin == null) {
            admin = Utilisateur.builder()
                    .username("admin")
                    .nomComplet("M. Bahizire")
                    .email("admin@autoecole.bi")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build();
            utilisateurRepository.save(admin);
            System.out.println(">>> Utilisateur admin créé : admin / admin123 (M. Bahizire)");
        } else {
            // Met à jour le nomComplet s'il est vide
            if (admin.getNomComplet() == null || admin.getNomComplet().isBlank()) {
                admin.setNomComplet("M. Bahizire");
                utilisateurRepository.save(admin);
                System.out.println(">>> Nom complet mis à jour : M. Bahizire");
            }
        }
    }
}