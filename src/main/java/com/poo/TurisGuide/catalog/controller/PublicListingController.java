package com.poo.TurisGuide.catalog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/services")
public class PublicListingController {

    @GetMapping
    public ResponseEntity<List<ServiceResponseDTO>> getAllServices() {
        // Dados mockados temporariamente para testar a interface
        List<ServiceResponseDTO> services = new ArrayList<>();

        // Hospedagens
        services.add(new ServiceResponseDTO(
            "1",
            "Hotel Praia Azul",
            "Hotel à beira-mar com vista panorâmica para o oceano. Quartos espaçosos e confortáveis com varanda privativa.",
            "Florianópolis, SC",
            "HOSPEDAGEM",
            350.00,
            8,
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "2",
            "Pousada Serra Verde",
            "Pousada aconchegante nas montanhas com clima serrano e natureza exuberante.",
            "Campos do Jordão, SP",
            "HOSPEDAGEM",
            280.00,
            6,
            "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "3",
            "Resort Tropical Paradise",
            "Resort all-inclusive com piscinas, spa e entretenimento para toda a família.",
            "Porto de Galinhas, PE",
            "HOSPEDAGEM",
            550.00,
            12,
            "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=400"
        ));

        // Passeios
        services.add(new ServiceResponseDTO(
            "4",
            "City Tour Centro Histórico",
            "Conheça os principais pontos turísticos do centro histórico com guia especializado.",
            "Salvador, BA",
            "PASSEIO",
            120.00,
            30,
            "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "5",
            "Trilha Cachoeira do Eldorado",
            "Trilha ecológica até a cachoeira com banho em águas cristalinas. Nível moderado.",
            "Chapada dos Veadeiros, GO",
            "PASSEIO",
            95.00,
            15,
            "https://images.unsplash.com/photo-1501594907352-04cda38ebc29?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "6",
            "Passeio de Barco - Ilhas Paradisíacas",
            "Tour de barco visitando 3 ilhas com parada para snorkel e almoço incluído.",
            "Angra dos Reis, RJ",
            "PASSEIO",
            180.00,
            20,
            "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=400"
        ));

        // Transportes
        services.add(new ServiceResponseDTO(
            "7",
            "Transfer Aeroporto - Hotel",
            "Transporte executivo do aeroporto até seu hotel com motorista bilíngue.",
            "Rio de Janeiro, RJ",
            "TRANSPORTE",
            85.00,
            6,
            "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "8",
            "Ônibus Turístico Panorâmico",
            "Ônibus de dois andares com tour pela cidade. Embarque e desembarque livre.",
            "São Paulo, SP",
            "TRANSPORTE",
            65.00,
            50,
            "https://images.unsplash.com/photo-1570125909232-eb263c188f7e?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "9",
            "Aluguel de Carro Compacto",
            "Carro compacto com seguro total e GPS incluídos. Diária com km livre.",
            "Curitiba, PR",
            "TRANSPORTE",
            120.00,
            5,
            "https://images.unsplash.com/photo-1552519507-cf0fbe2583f6?w=400"
        ));

        // Alimentação
        services.add(new ServiceResponseDTO(
            "10",
            "Jantar Romântico à Beira-Mar",
            "Menu degustação de frutos do mar com 5 pratos em restaurante à beira-mar.",
            "Búzios, RJ",
            "ALIMENTACAO",
            220.00,
            2,
            "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "11",
            "Café Colonial Completo",
            "Café colonial tradicional com mais de 40 opções de pães, bolos, geleias e frios.",
            "Gramado, RS",
            "ALIMENTACAO",
            75.00,
            4,
            "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "12",
            "Almoço Executivo Italiano",
            "Prato do dia com entrada, prato principal, sobremesa e bebida inclusos.",
            "Belo Horizonte, MG",
            "ALIMENTACAO",
            55.00,
            1,
            "https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=400"
        ));

        // Eventos
        services.add(new ServiceResponseDTO(
            "13",
            "Show de Samba ao Vivo",
            "Noite de samba com os melhores músicos da cidade. Couvert artístico incluído.",
            "Lapa, Rio de Janeiro, RJ",
            "EVENTO",
            80.00,
            150,
            "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "14",
            "Festival Gastronômico de Rua",
            "Festival com food trucks, música ao vivo e área kids. Entrada gratuita.",
            "Ibirapuera, São Paulo, SP",
            "EVENTO",
            0.00,
            5000,
            "https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?w=400"
        ));

        services.add(new ServiceResponseDTO(
            "15",
            "Espetáculo de Teatro Musical",
            "Musical premiado em cartaz. Elenco internacional e produção espetacular.",
            "Teatro Municipal, Porto Alegre, RS",
            "EVENTO",
            150.00,
            300,
            "https://images.unsplash.com/photo-1503095396549-807759245b35?w=400"
        ));

        return ResponseEntity.status(HttpStatus.OK).body(services);
    }

    // DTO interno
    public record ServiceResponseDTO(
        String id,
        String name,
        String description,
        String location,
        String type,
        Double price,
        int capacity,
        String imageUrl
    ) {}
}

