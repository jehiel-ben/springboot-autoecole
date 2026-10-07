package com.autoecole.service;

import com.autoecole.entity.Utilisateur;
import com.autoecole.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }

    public Utilisateur register(String username, String email, String rawPassword) {
        if (repo.existsByUsername(username)) {
            throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé");
        }
        if (repo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        Utilisateur u = Utilisateur.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role("ROLE_ADMIN")
                .build();

        return repo.save(u);
    }

    public List<Utilisateur> findAll() {
        return repo.findAll();
    }
}