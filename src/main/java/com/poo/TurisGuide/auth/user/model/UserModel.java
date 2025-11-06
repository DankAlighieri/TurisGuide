package com.poo.TurisGuide.auth.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String address;
    @Email
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate DOB;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDate;
}