package com.fatec.fantasy_game.integration.runner;

import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.entities.Player;
import com.fatec.fantasy_game.entities.PlayerPosition;
import com.fatec.fantasy_game.integration.service.ApiClient;
import com.fatec.fantasy_game.repositories.NationalTeamRepository;
import com.fatec.fantasy_game.repositories.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class DataInitializerRunner implements CommandLineRunner {

    private final ApiClient apiClient;
    private final NationalTeamRepository nationalTeamRepository;
    private final PlayerRepository playerRepository;

    public DataInitializerRunner(ApiClient apiClient, NationalTeamRepository nationalTeamRepository, PlayerRepository playerRepository) {
        this.apiClient = apiClient;
        this.nationalTeamRepository = nationalTeamRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (nationalTeamRepository.count() == 0) {
            System.out.println("Iniciando carga de dados da Copa do Mundo via API Externa...");

            apiClient.getTeamData()
                    .subscribe(response -> {
                        if (response != null && response.teams() != null) {
                            Random random = new Random();

                            response.teams().forEach(teamDto -> {
                                NationalTeam team = new NationalTeam();
                                team.setName(teamDto.name());

                                //gerando scores aleatórios entre 70 e 90 para ataque, meio e defesa
                                team.setAttackScore(70 + random.nextDouble(21));
                                team.setMidScore(70 + random.nextDouble(21));
                                team.setDefenseScore(70 + random.nextDouble(21));
                                team.setOverallScore((team.getAttackScore() + team.getMidScore() + team.getDefenseScore()) / 3);

                                final NationalTeam savedTeam = nationalTeamRepository.save(team);

                                if (teamDto.squad() != null) {
                                    teamDto.squad().forEach(playerDto -> {
                                        Player player = new Player();
                                        player.setName(playerDto.name());
                                        player.setTeam(savedTeam);

                                        player.setPosition(translatePosition(playerDto.position()));


                                        //preço de compra dos jogadores gerado aleatoriamente tambem para teste
                                        player.setCurrentPrice(BigDecimal.valueOf(5 + random.nextInt(16)));

                                        playerRepository.save(player);
                                    });
                                }
                            });
                            System.out.println("Carga de dados concluída com sucesso!");
                        }
                    });
        }
    }

    private PlayerPosition translatePosition(String externalPosition) {
        if (externalPosition == null) return PlayerPosition.ATACANTE;
        return switch (externalPosition.toLowerCase()) {
            case "goalkeeper" -> PlayerPosition.GOLEIRO;
            case "defence" -> PlayerPosition.ZAGUEIRO;
            case "midfield" -> PlayerPosition.MEIA;
            default -> PlayerPosition.ATACANTE;
        };
    }

}
