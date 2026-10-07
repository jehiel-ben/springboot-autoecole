package com.autoecole.controller;

import com.autoecole.dto.EleveRequest;
import com.autoecole.dto.EleveResponse;
import com.autoecole.exception.EmailAlreadyExistsException;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.service.EleveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/eleves")
@RequiredArgsConstructor
public class EleveWebController {

    private final EleveService eleveService;

    @GetMapping
    public String list(Model model) {
        List<EleveResponse> eleves = eleveService.findAll();
        model.addAttribute("eleves", eleves);
        model.addAttribute("pageTitle", "Élèves");
        model.addAttribute("activePage", "eleves");
        return "eleves/list";
    }

    @GetMapping("/corbeille")
    public String corbeille(Model model) {
        List<EleveResponse> eleves = eleveService.findCorbeille();
        model.addAttribute("eleves", eleves);
        model.addAttribute("pageTitle", "Corbeille - Élèves");
        model.addAttribute("activePage", "eleves");
        return "eleves/corbeille";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        model.addAttribute("eleve", new EleveRequest());
        model.addAttribute("pageTitle", "Nouvel élève");
        model.addAttribute("activePage", "eleves");
        model.addAttribute("isEdit", false);
        return "eleves/form";
    }

    @PostMapping("/nouveau")
    public String create(@ModelAttribute("eleve") EleveRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            eleveService.create(request);
            redirectAttributes.addFlashAttribute("success", "Élève créé avec succès !");
            return "redirect:/eleves";
        } catch (EmailAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/eleves/nouveau";
        }
    }

    @GetMapping("/voir/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            EleveResponse eleve = eleveService.findById(id);
            model.addAttribute("eleve", eleve);
            model.addAttribute("pageTitle", "Détails élève");
            model.addAttribute("activePage", "eleves");
            return "eleves/view";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/eleves";
        }
    }

    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            EleveResponse eleve = eleveService.findById(id);
            EleveRequest req = EleveRequest.builder()
                    .nom(eleve.getNom())
                    .prenom(eleve.getPrenom())
                    .email(eleve.getEmail())
                    .telephone(eleve.getTelephone())
                    .dateNaissance(eleve.getDateNaissance())
                    .build();
            model.addAttribute("eleve", req);
            model.addAttribute("eleveId", id);
            model.addAttribute("pageTitle", "Modifier élève");
            model.addAttribute("activePage", "eleves");
            model.addAttribute("isEdit", true);
            return "eleves/form";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/eleves";
        }
    }

    @PostMapping("/modifier/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("eleve") EleveRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            eleveService.update(id, request);
            redirectAttributes.addFlashAttribute("success", "Élève modifié avec succès !");
            return "redirect:/eleves";
        } catch (EmailAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/eleves/modifier/" + id;
        }
    }

    @PostMapping("/desactiver/{id}")
    public String desactiver(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eleveService.desactiver(id);
            redirectAttributes.addFlashAttribute("success", "Élève désactivé !");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/eleves";
    }

    /** Met à la corbeille (soft delete). */
    @PostMapping("/supprimer/{id}")
    public String softDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eleveService.softDelete(id);
            redirectAttributes.addFlashAttribute("success", "Élève mis à la corbeille !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer : " + e.getMessage());
        }
        return "redirect:/eleves";
    }

    /** Restaure depuis la corbeille. */
    @PostMapping("/restaurer/{id}")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eleveService.restore(id);
            redirectAttributes.addFlashAttribute("success", "Élève restauré !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de restaurer : " + e.getMessage());
        }
        return "redirect:/eleves/corbeille";
    }

    /** Supprime définitivement. */
    @PostMapping("/purger/{id}")
    public String hardDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eleveService.hardDelete(id);
            redirectAttributes.addFlashAttribute("success", "Élève supprimé définitivement !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de purger : l'élève a peut-être des séances ou paiements associés.");
        }
        return "redirect:/eleves/corbeille";
    }
}