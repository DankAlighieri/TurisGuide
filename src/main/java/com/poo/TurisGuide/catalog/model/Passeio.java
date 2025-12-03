package com.poo.TurisGuide.catalog.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("PASSEIO")
@Data
@EqualsAndHashCode(callSuper = true)
public class Passeio extends ListingModel {
    
    private String duracaoHoras;
    private String pontoEncontro;
    private boolean guiaIncluso;

    @Override
    public double calcularTaxaServico() {
        return this.getValor() * 0.10;
    }
}
