package com.poo.TurisGuide.catalog.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String titulo;
    private String descricao;
    private String localizacao;
    private String tipo;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private double valor;
}