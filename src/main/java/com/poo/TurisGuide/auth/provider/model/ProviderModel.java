package com.poo.TurisGuide.auth.provider.model;

import com.poo.TurisGuide.auth.model.Usuario;
import com.poo.TurisGuide.auth.provider.dtos.ProviderRegisterDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_provider")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProviderModel extends Usuario {

    private String cnpj;
    private String name; // Nome fantasia ou razão social
    private String services; // Descrição textual dos serviços

    public ProviderModel(ProviderRegisterDto data) {
        this.cnpj = data.cnpj();
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.services = data.services();
        this.role = "PRESTADOR";
        this.login = data.cnpj(); // Usando CNPJ como login padrão para providers
    }
}
