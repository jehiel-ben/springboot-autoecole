package com.autoecole.controller;

import com.autoecole.dto.SeanceCoursRequest;
import com.autoecole.dto.SeanceCoursResponse;
import com.autoecole.entity.Moniteur;
import com.autoecole.entity.Vehicule;
import com.autoecole.exception.ConflitPlanningException;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.service.EleveService;
import com.autoecole.service.MoniteurService;
import com.autoecole.service.SeanceCoursService;
import com.autoecole.service.VehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/seances")
@RequiredArgsConstructor
public class SeanceCoursWebController {

    private final SeanceCoursService seanceService;
    private final EleveService eleveService;
    private final MoniteurService moniteurService;
    private final VehiculeService vehiculeService;

    @GetMapping
    public String list(Model model) {
        List<SeanceCoursResponse> seances = seanceService.findAll();
        model.addAttribute("seances", seances);
        model.addAttribute("pageTitle", "Séances");
        model.addAttribute("activePage", "seances");
        return "seances/list";
    }

    @GetMapping("/corbeille")
    public String corbeille(Model model) {
        List<SeanceCoursResponse> seances = seanceService.findCorbeille();
        model.addAttribute("seances", seances);
        model.addAttribute("pageTitle", "Corbeille - Séances");
        model.addAttribute("activePage", "seances");
        return "seances/corbeille";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        model.addAttribute("seance", new SeanceCoursRequest());
        populateLists(model);
        model.addAttribute("pageTitle", "Nouvelle séance");
        model.addAttribute("activePage", "seances");
        model.addAttribute("isEdit", false);
        return "seances/form";
    }

    @PostMapping("/nouveau")
    public String create(@ModelAttribute("seance") SeanceCoursRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            seanceService.create(request);
            redirectAttributes.addFlashAttribute("success", "Séance planifiée avec succès !");
            return "redirect:/seances";
        } catch (ConflitPlanningException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/seances/nouveau";
        }
    }

    @GetMapping("/voir/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            SeanceCoursResponse seance = seanceService.findById(id);
            model.addAttribute("seance", seance);
            model.addAttribute("pageTitle", "Détails séance");
            model.addAttribute("activePage", "seances");
            return "seances/view";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/seances";
        }
    }

    @PostMapping("/statut/{id}")
    public String updateStatut(@PathVariable Long id,
                               @RequestParam String statut,
                               RedirectAttributes redirectAttributes) {
        try {
            seanceService.updateStatut(id, statut);
            redirectAttributes.addFlashAttribute("success", "Statut mis à jour !");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seances";
    }

    @PostMapping("/supprimer/{id}")
    public String softDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            seanceService.softDelete(id);
            redirectAttributes.addFlashAttribute("success", "Séance mise à la corbeille !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer : " + e.getMessage());
        }
        return "redirect:/seances";
    }

    @PostMapping("/restaurer/{id}")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            seanceService.restore(id);
            redirectAttributes.addFlashAttribute("success", "Séance restaurée !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de restaurer : " + e.getMessage());
        }
        return "redirect:/seances/corbeille";
    }

    @PostMapping("/purger/{id}")
    public String hardDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            seanceService.hardDelete(id);
            redirectAttributes.addFlashAttribute("success", "Séance supprimée définitivement !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de purger cette séance.");
        }
        return "redirect:/seances/corbeille";
    }

    private void populateLists(Model model) {
        model.addAttribute("eleves", eleveService.findAll());
        List<Moniteur> moniteurs = moniteurService.findAll();
        List<Vehicule> vehicules = vehiculeService.findAll();
        model.addAttribute("moniteurs", moniteurs);
        model.addAttribute("vehicules", vehicules);
    }
}