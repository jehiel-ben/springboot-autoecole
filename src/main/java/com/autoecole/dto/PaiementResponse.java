package com.autoecole.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementResponse {

    private Long id;
    private BigDecimal montant;
    private LocalDate datePaiement;
    private String modePaiement;
    private String typeFormule;

    private Long eleveId;
    private String eleveNomComplet;
}