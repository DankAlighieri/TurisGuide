package com.poo.TurisGuide.user.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class ProfileModel {
    private Long id;
    private String name;
    private String lastName;
    @Email
    private String email;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate DOB;
}
