package com.autoecole.controller;

import com.autoecole.dto.MoniteurRequest;
import com.autoecole.entity.Moniteur;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.service.MoniteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/moniteurs")
@RequiredArgsConstructor
public class MoniteurWebController {

    private final MoniteurService moniteurService;

    @GetMapping
    public String list(Model model) {
        List<Moniteur> moniteurs = moniteurService.findAll();
        model.addAttribute("moniteurs", moniteurs);
        model.addAttribute("pageTitle", "Moniteurs");
        model.addAttribute("activePage", "moniteurs");
        return "moniteurs/list";
    }

    @GetMapping("/corbeille")
    public String corbeille(Model model) {
        List<Moniteur> moniteurs = moniteurService.findCorbeille();
        model.addAttribute("moniteurs", moniteurs);
        model.addAttribute("pageTitle", "Corbeille - Moniteurs");
        model.addAttribute("activePage", "moniteurs");
        return "moniteurs/corbeille";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        model.addAttribute("moniteur", new MoniteurRequest());
        model.addAttribute("pageTitle", "Nouveau moniteur");
        model.addAttribute("activePage", "moniteurs");
        model.addAttribute("isEdit", false);
        return "moniteurs/form";
    }

    @PostMapping("/nouveau")
    public String create(@ModelAttribute("moniteur") MoniteurRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            moniteurService.create(request);
            redirectAttributes.addFlashAttribute("success", "Moniteur créé avec succès !");
            return "redirect:/moniteurs";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/moniteurs/nouveau";
        }
    }

    @GetMapping("/voir/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            Moniteur moniteur = moniteurService.findById(id);
            model.addAttribute("moniteur", moniteur);
            model.addAttribute("pageTitle", "Détails moniteur");
            model.addAttribute("activePage", "moniteurs");
            return "moniteurs/view";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/moniteurs";
        }
    }

    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Moniteur m = moniteurService.findById(id);
            MoniteurRequest req = MoniteurRequest.builder()
                    .nom(m.getNom())
                    .prenom(m.getPrenom())
                    .numPermis(m.getNumPermis())
                    .telephone(m.getTelephone())
                    .email(m.getEmail())
                    .statutDispo(m.getStatutDispo())
                    .build();
            model.addAttribute("moniteur", req);
            model.addAttribute("moniteurId", id);
            model.addAttribute("pageTitle", "Modifier moniteur");
            model.addAttribute("activePage", "moniteurs");
            model.addAttribute("isEdit", true);
            return "moniteurs/form";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/moniteurs";
        }
    }

    @PostMapping("/modifier/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("moniteur") MoniteurRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            moniteurService.update(id, request);
            redirectAttributes.addFlashAttribute("success", "Moniteur modifié !");
            return "redirect:/moniteurs";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/moniteurs/modifier/" + id;
        }
    }

    @PostMapping("/supprimer/{id}")
    public String softDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            moniteurService.softDelete(id);
            redirectAttributes.addFlashAttribute("success", "Moniteur mis à la corbeille !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer : " + e.getMessage());
        }
        return "redirect:/moniteurs";
    }

    @PostMapping("/restaurer/{id}")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            moniteurService.restore(id);
            redirectAttributes.addFlashAttribute("success", "Moniteur restauré !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de restaurer : " + e.getMessage());
        }
        return "redirect:/moniteurs/corbeille";
    }

    @PostMapping("/purger/{id}")
    public String hardDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            moniteurService.hardDelete(id);
            redirectAttributes.addFlashAttribute("success", "Moniteur supprimé définitivement !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de purger : le moniteur a peut-être des séances associées.");
        }
        return "redirect:/moniteurs/corbeille";
    }
}