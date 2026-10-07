package com.autoecole.controller;

import com.autoecole.entity.Utilisateur;
import com.autoecole.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/profil")
@RequiredArgsConstructor
public class ProfilController {

    private final UtilisateurRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Utilisateur getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return repo.findByUsername(auth.getName()).orElse(null);
    }

    @GetMapping
    public String showProfil(Model model) {
        Utilisateur user = getCurrentUser();
        model.addAttribute("utilisateur", user);
        model.addAttribute("pageTitle", "Mon profil");
        return "profil";
    }

    @PostMapping
    public String updateProfil(@RequestParam(required = false) String nomComplet,
                               @RequestParam String email,
                               @RequestParam(required = false) String newPassword,
                               @RequestParam(required = false) MultipartFile photo,
                               RedirectAttributes redirectAttributes) {
        try {
            Utilisateur user = getCurrentUser();

            user.setNomComplet(nomComplet);
            user.setEmail(email);

            if (newPassword != null && !newPassword.isBlank()) {
                if (newPassword.length() < 6) {
                    redirectAttributes.addFlashAttribute("error", "Le mot de passe doit faire au moins 6 caractères");
                    return "redirect:/profil";
                }
                user.setPassword(passwordEncoder.encode(newPassword));
            }

            if (photo != null && !photo.isEmpty()) {
                // Validation du type
                String contentType = photo.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    redirectAttributes.addFlashAttribute("error", "Le fichier doit être une image");
                    return "redirect:/profil";
                }

                // Créer le dossier si inexistant
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Nom unique pour éviter les conflits
                String ext = "";
                String originalName = photo.getOriginalFilename();
                if (originalName != null && originalName.contains(".")) {
                    ext = originalName.substring(originalName.lastIndexOf("."));
                }
                String filename = UUID.randomUUID() + ext;

                Path filePath = uploadPath.resolve(filename);
                Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // URL à utiliser dans le HTML
                user.setPhotoUrl("/uploads/" + filename);

                System.out.println(">>> Photo uploadée : " + filePath);
            }

            repo.save(user);
            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès !");
            return "redirect:/profil";
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur upload photo : " + e.getMessage());
            return "redirect:/profil";
        }
    }
}