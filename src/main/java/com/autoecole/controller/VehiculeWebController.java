package com.autoecole.controller;

import com.autoecole.dto.VehiculeRequest;
import com.autoecole.entity.Vehicule;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.service.VehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeWebController {

    private final VehiculeService vehiculeService;

    @GetMapping
    public String list(Model model) {
        List<Vehicule> vehicules = vehiculeService.findAll();
        model.addAttribute("vehicules", vehicules);
        model.addAttribute("pageTitle", "Véhicules");
        model.addAttribute("activePage", "vehicules");
        return "vehicules/list";
    }

    @GetMapping("/corbeille")
    public String corbeille(Model model) {
        List<Vehicule> vehicules = vehiculeService.findCorbeille();
        model.addAttribute("vehicules", vehicules);
        model.addAttribute("pageTitle", "Corbeille - Véhicules");
        model.addAttribute("activePage", "vehicules");
        return "vehicules/corbeille";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        model.addAttribute("vehicule", new VehiculeRequest());
        model.addAttribute("pageTitle", "Nouveau véhicule");
        model.addAttribute("activePage", "vehicules");
        model.addAttribute("isEdit", false);
        return "vehicules/form";
    }

    @PostMapping("/nouveau")
    public String create(@ModelAttribute("vehicule") VehiculeRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            vehiculeService.create(request);
            redirectAttributes.addFlashAttribute("success", "Véhicule créé !");
            return "redirect:/vehicules";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehicules/nouveau";
        }
    }

    @GetMapping("/voir/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            Vehicule v = vehiculeService.findById(id);
            model.addAttribute("vehicule", v);
            model.addAttribute("pageTitle", "Détails véhicule");
            model.addAttribute("activePage", "vehicules");
            return "vehicules/view";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/vehicules";
        }
    }

    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Vehicule v = vehiculeService.findById(id);
            VehiculeRequest req = VehiculeRequest.builder()
                    .immatriculation(v.getImmatriculation())
                    .marque(v.getMarque())
                    .modele(v.getModele())
                    .typeBoite(v.getTypeBoite())
                    .statutEntretiens(v.getStatutEntretiens())
                    .build();
            model.addAttribute("vehicule", req);
            model.addAttribute("vehiculeId", id);
            model.addAttribute("pageTitle", "Modifier véhicule");
            model.addAttribute("activePage", "vehicules");
            model.addAttribute("isEdit", true);
            return "vehicules/form";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/vehicules";
        }
    }

    @PostMapping("/modifier/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("vehicule") VehiculeRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            vehiculeService.update(id, request);
            redirectAttributes.addFlashAttribute("success", "Véhicule modifié !");
            return "redirect:/vehicules";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehicules/modifier/" + id;
        }
    }

    @PostMapping("/supprimer/{id}")
    public String softDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehiculeService.softDelete(id);
            redirectAttributes.addFlashAttribute("success", "Véhicule mis à la corbeille !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer : " + e.getMessage());
        }
        return "redirect:/vehicules";
    }

    @PostMapping("/restaurer/{id}")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehiculeService.restore(id);
            redirectAttributes.addFlashAttribute("success", "Véhicule restauré !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de restaurer : " + e.getMessage());
        }
        return "redirect:/vehicules/corbeille";
    }

    @PostMapping("/purger/{id}")
    public String hardDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehiculeService.hardDelete(id);
            redirectAttributes.addFlashAttribute("success", "Véhicule supprimé définitivement !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Impossible de purger : le véhicule a peut-être des séances associées.");
        }
        return "redirect:/vehicules/corbeille";
    }
}