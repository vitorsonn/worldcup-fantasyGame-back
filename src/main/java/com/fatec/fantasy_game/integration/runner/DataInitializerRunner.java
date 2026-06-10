package com.fatec.fantasy_game.integration.runner;

import com.fatec.fantasy_game.entities.FantasyTeam;
import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.enums.MatchStatus;
import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.entities.Player;
import com.fatec.fantasy_game.enums.PlayerPosition;
import com.fatec.fantasy_game.entities.Round;
import com.fatec.fantasy_game.entities.User;
import com.fatec.fantasy_game.integration.service.ApiClient;
import com.fatec.fantasy_game.repositories.FantasyTeamRepository;
import com.fatec.fantasy_game.repositories.MatchRepository;
import com.fatec.fantasy_game.repositories.NationalTeamRepository;
import com.fatec.fantasy_game.repositories.PlayerRepository;
import com.fatec.fantasy_game.repositories.RoundRepository;
import com.fatec.fantasy_game.repositories.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataInitializerRunner implements CommandLineRunner {

    private final ApiClient apiClient;
    private final NationalTeamRepository nationalTeamRepository;
    private final PlayerRepository playerRepository;
    private final FantasyTeamRepository fantasyTeamRepository;
    private final RoundRepository roundRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    public DataInitializerRunner(ApiClient apiClient, NationalTeamRepository nationalTeamRepository,
            PlayerRepository playerRepository, UserRepository userRepository, RoundRepository roundRepository,
            FantasyTeamRepository fantasyTeamRepository, MatchRepository matchRepository) {
        this.apiClient = apiClient;
        this.nationalTeamRepository = nationalTeamRepository;
        this.playerRepository = playerRepository;
        this.fantasyTeamRepository = fantasyTeamRepository;
        this.roundRepository = roundRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("Criando dados de teste do usuário e rodada...");

            User vitorUser = new User();
            vitorUser.setUsername("Vitor");
            vitorUser.setEmail("vitor@email.com");
            User savedUser = userRepository.save(vitorUser);

            FantasyTeam meuTime = new FantasyTeam();
            meuTime.setName("Vitor FC");
            meuTime.setOwner(savedUser);
            meuTime.setCash(150.0);
            fantasyTeamRepository.save(meuTime);

            Round rodada1 = new Round();
            rodada1.setRoundNumber(1);
            roundRepository.save(rodada1);
        }

        if (nationalTeamRepository.count() == 0) {
            System.out.println("Iniciando carga de dados da Copa do Mundo via API Externa...");

            try {
                var response = apiClient.getTeamData().block();

                if (response != null && response.teams() != null) {
                    Random random = new Random();

                    response.teams().forEach(teamDto -> {
                        NationalTeam team = new NationalTeam();
                        team.setName(teamDto.name());
                        team.setAttackScore(70 + random.nextDouble(21));
                        team.setMidScore(70 + random.nextDouble(21));
                        team.setDefenseScore(70 + random.nextDouble(21));
                        team.setOverallScore(
                                (team.getAttackScore() + team.getMidScore() + team.getDefenseScore()) / 3);

                        final NationalTeam savedTeam = nationalTeamRepository.save(team);

                        if (teamDto.squad() != null) {
                            teamDto.squad().forEach(playerDto -> {
                                Player player = new Player();
                                player.setName(playerDto.name());
                                player.setTeam(savedTeam);
                                player.setPosition(translatePosition(playerDto.position()));
                                player.setCurrentPrice(Double.valueOf(5 + random.nextInt(16)));
                                playerRepository.save(player);
                            });
                        }
                    });

                    System.out.println("Carga de seleções e jogadores concluída com sucesso!");

                    System.out.println("Agendando partidas de teste...");
                    List<NationalTeam> selecoes = nationalTeamRepository.findAll();

                    if (selecoes.size() >= 16) {

                        for (int roundNum = 1; roundNum <= 5; roundNum++) {
                            final int currentRoundNumber = roundNum;

                            Round currentRound = roundRepository.findByRoundNumber(currentRoundNumber)
                                    .orElseGet(() -> {
                                        Round newRound = new Round();
                                        newRound.setRoundNumber(currentRoundNumber);
                                        return roundRepository.save(newRound);
                                    });

                            System.out.println("Gerando confrontos para a Rodada " + roundNum + "...");

                            for (int jogo = 0; jogo < 8; jogo++) {
                                int homeIdx = (jogo + roundNum) % selecoes.size();
                                int awayIdx = (selecoes.size() - 1 - jogo + roundNum) % selecoes.size();

                                if (homeIdx == awayIdx) {
                                    awayIdx = (awayIdx + 1) % selecoes.size();
                                }

                                Match partida = new Match();
                                partida.setHomeTeam(selecoes.get(homeIdx));
                                partida.setAwayTeam(selecoes.get(awayIdx));
                                partida.setRound(currentRound);
                                partida.setStatus(MatchStatus.AGENDADA);
                                partida.setHomeGoals(0);
                                partida.setAwayGoals(0);

                                matchRepository.save(partida);
                            }
                        }
                        System.out.println("⚽ Calendário completo de 5 rodadas com 8 jogos cada agendado com sucesso!");

                    } else {
                        System.out.println("Aviso: É necessário ter pelo menos 16 seleções vindas da API para fechar 8 jogos por rodada.");
                    }




                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar dados da API: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private PlayerPosition translatePosition(String externalPosition) {
        if (externalPosition == null)
            return PlayerPosition.ATACANTE;
        return switch (externalPosition.toLowerCase()) {
            case "goalkeeper" -> PlayerPosition.GOLEIRO;
            case "defence" -> PlayerPosition.ZAGUEIRO;
            case "midfield" -> PlayerPosition.MEIA;
            default -> PlayerPosition.ATACANTE;
        };
    }

}
