package com.poo.TurisGuide.auth.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.poo.TurisGuide.auth.user.dto.AuthDTO;
import com.poo.TurisGuide.auth.user.dto.LoginResponseDTO;
import com.poo.TurisGuide.auth.user.dto.RegisterDTO;
import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.auth.user.service.AuthService;
import com.poo.TurisGuide.infra.security.TokenService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid RegisterDTO registerDTO){
        UserModel newUser = new UserModel();
        BeanUtils.copyProperties(registerDTO, newUser);

        if(authService.checkExistingUser(newUser)) return ResponseEntity.badRequest().build();

        String passwordHash = new BCryptPasswordEncoder().encode(registerDTO.password());

        newUser.setPassword(passwordHash);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.saveUser(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO authDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserId(@PathVariable UUID userId ){
        authService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateUser(
        @RequestBody @Valid AuthDTO AuthDTO,
        @PathVariable UUID userId) {
        
        UserModel updatedUser = new UserModel();
        BeanUtils.copyProperties(AuthDTO, updatedUser);
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