package com.poo.TurisGuide.catalog.model;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.Data;
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
    @JsonSubTypes.Type(value = Passeio.class, name = "PASSEIO")
})
@Data
public abstract class ListingModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String titulo;
    private String descricao;
    private String localizacao;
    private double valor;

    // Mudança: Relação direta com objeto em vez de apenas UUID
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ProviderModel prestador;

    // Método abstrato que obriga as filhas a implementarem regras específicas
    public abstract double calcularTaxaServico();
}