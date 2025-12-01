package com.poo.TurisGuide.catalog.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("EVENTO")
@Data
@EqualsAndHashCode(callSuper = true)
public class Evento extends ListingModel {

    private String dataEvento;
    private String horarioInicio;
    private String horarioTermino;
    private String tipoEvento;
    private int ingressosDisponiveis;

    @Override
    public double calcularTaxaServico() {
        return this.getValor() * 0.12;
    }
}

