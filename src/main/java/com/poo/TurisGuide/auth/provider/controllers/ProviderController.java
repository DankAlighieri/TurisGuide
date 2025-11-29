package com.poo.TurisGuide.auth.provider.controllers;

import com.poo.TurisGuide.auth.provider.dtos.LoginResponseProviderDto;
import com.poo.TurisGuide.auth.provider.dtos.ProviderDTO;
import com.poo.TurisGuide.auth.provider.dtos.ProviderRegisterDto;
import com.poo.TurisGuide.auth.provider.infra.security.TokenServiceProvider;
import com.poo.TurisGuide.auth.provider.model.ProviderModel;
import com.poo.TurisGuide.auth.provider.service.ProviderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
@Tag(name = "Autenticação de Provedores", description = "Endpoints para registro e login de provedores de serviços")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private TokenServiceProvider tokenServiceProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Registrar novo provedor", description = "Cria um novo provedor de serviços no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Provedor criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "CNPJ já cadastrado ou dados inválidos")
    })
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

    @Operation(summary = "Login de provedor", description = "Realiza autenticação do provedor e retorna token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "CNPJ ou senha inválidos")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid ProviderDTO data) {
        try {
            System.out.println("=== TENTATIVA DE LOGIN DO PRESTADOR ===");
            System.out.println("CNPJ recebido: " + data.cnpj());

            // Busca o provider pelo CNPJ
            ProviderModel provider = providerService.findByCnpj(data.cnpj());
            
            if (provider == null) {
                System.out.println("❌ Provider NÃO encontrado para CNPJ: " + data.cnpj());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("CNPJ ou senha inválidos");
            }

            System.out.println("✅ Provider encontrado: " + provider.getName());
            System.out.println("ID: " + provider.getId());
            System.out.println("Email: " + provider.getEmail());
            System.out.println("Verificando senha...");

            // Verifica a senha manualmente
            if (!passwordEncoder.matches(data.password(), provider.getPassword())) {
                System.out.println("❌ Senha INCORRETA!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("CNPJ ou senha inválidos");
            }

            System.out.println("✅ Senha CORRETA!");
            System.out.println("Gerando token...");

            // Gera o token
            String token = tokenServiceProvider.generateToken(provider);

            System.out.println("✅ Token gerado com sucesso");

            // Cria o objeto de resposta com token e dados do provider
            LoginResponseProviderDto.ProviderData providerData =
                    LoginResponseProviderDto.ProviderData.fromModel(provider);

            System.out.println("✅ Login realizado com sucesso para: " + provider.getName());

            return ResponseEntity.ok(new LoginResponseProviderDto(token, providerData));

        } catch (Exception e) {
            System.out.println("❌ ERRO NO LOGIN: " + e.getMessage());
            e.printStackTrace();
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

    /* 
    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderById(String id){
        try{
            return ResponseEntity.ok(providerService.getProvider(id));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar provider: " + e.getMessage());
        }
    } 
    */
    
}
