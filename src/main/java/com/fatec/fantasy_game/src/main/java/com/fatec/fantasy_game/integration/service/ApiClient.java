package com.fatec.fantasy_game.integration.service;


import com.fatec.fantasy_game.integration.dto.TeamResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApiClient {

    private final WebClient webClient;

    public ApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<TeamResponseDTO> getTeamData(){
        return this.webClient.get()
                .uri("/competitions/WC/teams")
                .retrieve()
                .bodyToMono(TeamResponseDTO.class)
                .onErrorResume(error -> {
                    System.err.println("Erro na busca das seleções: " + error.getMessage());
                    return Mono.empty();
                });

    }


}
