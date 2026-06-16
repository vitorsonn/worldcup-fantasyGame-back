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
                        System.out.println("Agendando partidas clássicas personalizadas...");

                        for (int roundNum = 1; roundNum <= 5; roundNum++) {
                            final int currentRoundNumber = roundNum;
                            roundRepository.findByRoundNumber(currentRoundNumber)
                                    .orElseGet(() -> {
                                        Round newRound = new Round();
                                        newRound.setRoundNumber(currentRoundNumber);
                                        newRound.setSimulationCount(0);
                                        return roundRepository.save(newRound);
                                    });
                        }

                        Round r1 = roundRepository.findByRoundNumber(1).orElseThrow();
                        criarJogoManual("Brazil", "Spain", r1);
                        criarJogoManual("Germany", "Argentina", r1);
                        criarJogoManual("Portugal", "Netherlands", r1);
                        criarJogoManual("Uruguay", "France", r1);
                        criarJogoManual("England", "Australia", r1);
                        criarJogoManual("Belgium", "Croatia", r1);
                        criarJogoManual("Morocco", "Japan", r1);
                        criarJogoManual("United States", "Mexico", r1);

                        Round r2 = roundRepository.findByRoundNumber(2).orElseThrow();
                        criarJogoManual("Brazil", "Germany", r2);
                        criarJogoManual("Spain", "Portugal", r2);
                        criarJogoManual("Argentina", "France", r2);
                        criarJogoManual("Netherlands", "Uruguay", r2);
                        criarJogoManual("Australia", "Belgium", r2);
                        criarJogoManual("Croatia", "England", r2);
                        criarJogoManual("Japan", "United States", r2);
                        criarJogoManual("Mexico", "Morocco", r2);

                        Round r3 = roundRepository.findByRoundNumber(3).orElseThrow();
                        criarJogoManual("Brazil", "Argentina", r3);
                        criarJogoManual("Germany", "France", r3);
                        criarJogoManual("Spain", "Netherlands", r3);
                        criarJogoManual("Portugal", "Uruguay", r3);
                        criarJogoManual("England", "Belgium", r3);
                        criarJogoManual("Australia", "Croatia", r3);
                        criarJogoManual("Morocco", "United States", r3);
                        criarJogoManual("Mexico", "Japan", r3);

                        Round r4 = roundRepository.findByRoundNumber(4).orElseThrow();
                        criarJogoManual("Brazil", "France", r4);
                        criarJogoManual("Germany", "Portugal", r4);
                        criarJogoManual("Argentina", "Spain", r4);
                        criarJogoManual("Netherlands", "England", r4);
                        criarJogoManual("Uruguay", "Australia", r4);
                        criarJogoManual("Belgium", "Japan", r4);
                        criarJogoManual("Croatia", "Mexico", r4);
                        criarJogoManual("United States", "Morocco", r4);

                        Round r5 = roundRepository.findByRoundNumber(5).orElseThrow();
                        criarJogoManual("Brazil", "Portugal", r5);
                        criarJogoManual("Argentina", "Germany", r5);
                        criarJogoManual("Spain", "France", r5);
                        criarJogoManual("Netherlands", "Australia", r5);
                        criarJogoManual("Uruguay", "England", r5);
                        criarJogoManual("Belgium", "Morocco", r5);
                        criarJogoManual("Croatia", "Japan", r5);
                        criarJogoManual("Mexico", "United States", r5);

                        System.out.println("⚽ Calendário ÉPICO de 5 rodadas agendado com sucesso!");

                    } else {
                        System.out.println("Aviso: A API retornou menos de 16 seleções (" + selecoes.size() + "). O agendamento manual foi cancelado.");
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

    private void criarJogoManual(String homeName, String awayName, Round round) {
        NationalTeam home = nationalTeamRepository.findByNameIgnoreCase(homeName)
                .orElseThrow(() -> new RuntimeException("Seleção não encontrada: " + homeName));

        NationalTeam away = nationalTeamRepository.findByNameIgnoreCase(awayName)
                .orElseThrow(() -> new RuntimeException("Seleção não encontrada: " + awayName));

        Match partida = new Match();
        partida.setHomeTeam(home);
        partida.setAwayTeam(away);
        partida.setRound(round);
        partida.setStatus(MatchStatus.AGENDADA);
        partida.setHomeGoals(0);
        partida.setAwayGoals(0);

        matchRepository.save(partida);
    }

}
