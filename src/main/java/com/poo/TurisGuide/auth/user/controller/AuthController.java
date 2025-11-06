package com.poo.TurisGuide.auth.user.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poo.TurisGuide.auth.user.dto.UserDTO;
import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.auth.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class AuthController {

    final AuthService authService;

    @PostMapping
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserDTO userDTO){

        UserModel newUser = new UserModel();

        BeanUtils.copyProperties(userDTO, newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.saveUser(newUser));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Erro interno: " + e.getMessage());
    }
}