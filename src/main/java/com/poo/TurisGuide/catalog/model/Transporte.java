package com.poo.TurisGuide.catalog.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@DiscriminatorValue("TRANSPORTE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Transporte extends ListingModel {
    private String tipoVeiculo;
    private int capacidadePassageiros;
    private boolean arCondicionado;
    private String horarioSaida;
    private String horarioChegada;
    @Override
    public double calcularTaxaServico() {
        return this.getValor() * 0.08;
    }
}
