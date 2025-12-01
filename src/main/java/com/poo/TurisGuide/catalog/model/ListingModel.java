package com.poo.TurisGuide.catalog.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.poo.TurisGuide.auth.provider.model.ProviderModel;

@Entity
@Table(name = "servicos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Todos serviços na mesma tabela com coluna discriminadora
@DiscriminatorColumn(name = "tipo_servico")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Hospedagem.class, name = "HOSPEDAGEM"),
    @JsonSubTypes.Type(value = Passeio.class, name = "PASSEIO"),
    @JsonSubTypes.Type(value = Transporte.class, name = "TRANSPORTE"),
    @JsonSubTypes.Type(value = Alimentacao.class, name = "ALIMENTACAO"),
    @JsonSubTypes.Type(value = Evento.class, name = "EVENTO")
})
@Data
@NoArgsConstructor
public abstract class ListingModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    private String titulo;
    private String descricao;
    private String localizacao;
    private double valor;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ProviderModel prestador;

    public abstract double calcularTaxaServico();
}
