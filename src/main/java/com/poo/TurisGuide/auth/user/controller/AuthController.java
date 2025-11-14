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
import com.poo.TurisGuide.auth.user.security.TokenService;
import com.poo.TurisGuide.auth.user.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login e cadastro de usuários")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastrar usuário", 
               description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Usuário já existe ou dados inválidos")
    })
    public ResponseEntity<UserModel> saveUser(
            @Parameter(description = "Dados do novo usuário") 
            @RequestBody @Valid RegisterDTO registerDTO){
        UserModel newUser = new UserModel();
        BeanUtils.copyProperties(registerDTO, newUser);

        if(authService.checkExistingUser(newUser)) return ResponseEntity.badRequest().build();

        String passwordHash = new BCryptPasswordEncoder().encode(registerDTO.password());

        newUser.setPassword(passwordHash);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.saveUser(newUser));
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login", 
               description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<LoginResponseDTO> login(
            @Parameter(description = "Dados de login do usuário") 
            @RequestBody @Valid AuthDTO authDTO){
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