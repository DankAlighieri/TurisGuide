package com.poo.TurisGuide.catalog.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("HOSPEDAGEM")
@Data
@EqualsAndHashCode(callSuper = true)
public class Hospedagem extends ListingModel {
    
    private boolean cafeDaManhaIncluso;
    private int numeroQuartos;
    private String horarioCheckIn;
    private String horarioCheckOut;

    @Override
    public double calcularTaxaServico() {
        return this.getValor() * 0.05;
    }
}
