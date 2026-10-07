package com.autoecole.controller;

import com.autoecole.dto.SeanceCoursResponse;
import com.autoecole.repository.EleveRepository;
import com.autoecole.repository.MoniteurRepository;
import com.autoecole.repository.PaiementRepository;
import com.autoecole.repository.SeanceCoursRepository;
import com.autoecole.repository.VehiculeRepository;
import com.autoecole.service.SeanceCoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final EleveRepository eleveRepository;
    private final MoniteurRepository moniteurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final SeanceCoursRepository seanceRepository;
    private final PaiementRepository paiementRepository;
    private final SeanceCoursService seanceService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("nbEleves", eleveRepository.count());
        model.addAttribute("nbMoniteurs", moniteurRepository.count());
        model.addAttribute("nbVehicules", vehiculeRepository.count());
        model.addAttribute("nbSeances", seanceRepository.count());
        model.addAttribute("nbPaiements", paiementRepository.count());

        BigDecimal total = paiementRepository.findAll().stream()
                .map(p -> p.getMontant())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalPaiements", total);

        List<SeanceCoursResponse> dernieres = seanceService.findAll().stream()
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("dernieresSeances", dernieres);

        return "dashboard";
    }
}