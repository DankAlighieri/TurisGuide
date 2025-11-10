package com.poo.TurisGuide.auth.provider.controllers;

import com.poo.TurisGuide.auth.provider.dtos.LoginResponseProviderDto;
import com.poo.TurisGuide.auth.provider.dtos.ProviderDTO;
import com.poo.TurisGuide.auth.provider.dtos.ProviderRegisterDto;
import com.poo.TurisGuide.auth.provider.infra.security.TokenServiceProvider;
import com.poo.TurisGuide.auth.provider.model.ProviderModel;
import com.poo.TurisGuide.auth.provider.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    @Qualifier("providerAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenServiceProvider tokenServiceProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> createProvider(@RequestBody @Valid ProviderRegisterDto data) {
        try {
            if (data.password() == null || data.password().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password é obrigatório");
            }

            if (providerService.existsByCnpj(data.cnpj())) {
                return ResponseEntity.badRequest().body("CNPJ já cadastrado");
            }

            ProviderModel providerModel = new ProviderModel(data);
            String passwordHash = passwordEncoder.encode(data.password());
            providerModel.setPassword(passwordHash);

            ProviderModel savedProvider = providerService.saveProvider(providerModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProvider);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar provider: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid ProviderDTO data) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(data.cnpj(), data.password());

            Authentication authentication = authenticationManager.authenticate(authToken);

            ProviderModel provider = (ProviderModel) authentication.getPrincipal();
            String token = tokenServiceProvider.generateToken(provider);

            return ResponseEntity.ok(new LoginResponseProviderDto(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("CNPJ ou senha inválidos");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Falha na autenticação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar login: " + e.getMessage());
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAllProviders() {
        try {
            return ResponseEntity.ok(providerService.getAllProviders());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar providers: " + e.getMessage());
        }
    }
}
