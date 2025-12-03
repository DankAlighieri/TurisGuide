package com.poo.TurisGuide.catalog.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("ALIMENTACAO")
@Data
@EqualsAndHashCode(callSuper = true)
public class Alimentacao extends ListingModel {

    private String tipoRefeicao;
    private String tipoCozinha;
    private boolean bebidaInclusa;
    private String horarioFuncionamento;
    private boolean entregaDisponivel;

    @Override
    public double calcularTaxaServico() {
        return this.getValor() * 0.06;
    }
}

