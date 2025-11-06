package com.poo.TurisGuide.auth.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserId(@PathVariable UUID userId ){
        authService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateUser(
        @RequestBody @Valid UserDTO userDTO,
        @PathVariable UUID userId) {
        
        UserModel updatedUser = new UserModel();
        BeanUtils.copyProperties(userDTO, updatedUser);
        updatedUser.setId(userId);
        
        updatedUser = this.authService.updateUser(updatedUser, userId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(authService.getUserById(userId));
    }
        
    @GetMapping
    public ResponseEntity<List<UserModel>> getUserWithPagination(
        @RequestParam(defaultValue = "0") int start, 
        @RequestParam(defaultValue = "10") int end) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getUsersWithPagination(start, end));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Erro interno: " + e.getMessage());
    }
}