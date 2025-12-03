package com.poo.TurisGuide.auth.user.model;

import com.poo.TurisGuide.auth.model.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users_cliente")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserModel extends Usuario {
    
    private String firstName;
    private String lastName;
    private String address;
    private String DOB;

    // Campos como login, password, email e id já estão na classe pai (Usuario)
}