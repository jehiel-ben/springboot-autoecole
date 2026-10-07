package com.autoecole.controller;

import com.autoecole.dto.PaiementRequest;
import com.autoecole.dto.PaiementResponse;
import com.autoecole.exception.ResourceNotFoundException;
import com.autoecole.service.EleveService;
import com.autoecole.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementWebController {

    private final PaiementService paiementService;
    private final EleveService eleveService;

    @GetMapping
    public String list(Model model) {
        List<PaiementResponse> paiements = paiementService.findAll();
        BigDecimal total = paiements.stream()
                .map(PaiementResponse::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("paiements", paiements);
        model.addAttribute("total", total);
        model.addAttribute("pageTitle", "Paiements");
        model.addAttribute("activePage", "paiements");
        return "paiements/list";
    }

    @GetMapping("/corbeille")
    public String corbeille(Model model) {
        List<PaiementResponse> paiements = paiementService.findCorbeille();
        model.addAttribute("paiements", paiements);
        model.addAttribute("pageTitle", "Corbeille - Paiements");
        model.addAttribute("activePage", "paiements");
        return "paiements/corbeille";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        PaiementRequest req = PaiementRequest.builder()
                .datePaiement(LocalDate.now())
                .build();
        model.addAttribute("paiement", req);
        model.addAttribute("eleves", eleveService.findAll());
        model.addAttribute("pageTitle", "Nouveau paiement");
        model.addAttribute("activePage", "paiements");
        return "paiements/form";
    }

    @PostMapping("/nouveau")
    public String create(@ModelAttribute("paiement") PaiementRequest request,
                         RedirectAttributes redirectAttributes) {
        try {
            PaiementResponse created = paiementService.create(request);
            redirectAttributes.addFlashAttribute("success", "Paiement enregistré avec succès !");
            return "redirect:/paiements/recu/" + created.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/paiements/nouveau";
        }
    }

    @GetMapping("/recu/{id}")
    public String recu(@PathVariable Long id, Model model) {
        try {
            PaiementResponse paiement = paiementService.findAll().stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable"));
            model.addAttribute("paiement", paiement);
            model.addAttribute("pageTitle", "Reçu de paiement");
            return "paiements/recu";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/paiements";
        }
    }

    @PostMapping("/supprimer/{id}")
    public String softDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            paiementService.softDelete(id);
            redirectAttributes.addFlashAttribute("success", "Paiement mis à la corbeille !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer : " + e.getMessage());
        }
        return "redirect:/paiements";
    }

    @PostMapping("/restaurer/{id}")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            paiementService.restore(id);
            redirectAttributes.addFlashAttribute("success", "Paiement restauré !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de restaurer : " + e.getMessage());
        }
        return "redirect:/paiements/corbeille";
    }

    @PostMapping("/purger/{id}")
    public String hardDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            paiementService.hardDelete(id);
            redirectAttributes.addFlashAttribute("success", "Paiement supprimé définitivement !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de purger ce paiement.");
        }
        return "redirect:/paiements/corbeille";
    }
}