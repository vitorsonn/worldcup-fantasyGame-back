package com.fatec.fantasy_game.simulation.service;

import com.fatec.fantasy_game.entities.EventType;
import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.entities.MatchEvent;
import com.fatec.fantasy_game.entities.MatchStatus;
import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.entities.Player;
import com.fatec.fantasy_game.repositories.MatchEventRepository;
import com.fatec.fantasy_game.repositories.MatchRepository;
import com.fatec.fantasy_game.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MatchSimulatorService {

    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final PlayerRepository playerRepository;
    private final Random random = new Random();

    public MatchSimulatorService(MatchRepository matchRepository, MatchEventRepository matchEventRepository,
            PlayerRepository playerRepository) {
        this.matchRepository = matchRepository;
        this.matchEventRepository = matchEventRepository;
        this.playerRepository = playerRepository;
    }

    public Match simulateMatch(Match match) {
        matchEventRepository.deleteByMatchId(match.getId());

        if (match.getEvents() != null) {
            match.getEvents().clear();
        }



        NationalTeam home = match.getHomeTeam();
        NationalTeam away = match.getAwayTeam();

        double homeExpectancy = (home.getAttackScore() / away.getDefenseScore()) * 1.5;
        double awayExpectancy = (away.getAttackScore() / home.getDefenseScore()) * 1.2;

        int homeGoals = drawGoals(homeExpectancy);
        int awayGoals = drawGoals(awayExpectancy);

        match.setHomeGoals(homeGoals);
        match.setAwayGoals(awayGoals);
        match.setStatus(MatchStatus.CONCLUIDA);

        Match savedMatch = matchRepository.save(match);

        if (homeGoals > 0) {
            processTeamEvents(savedMatch, home, homeGoals);
        }
        if (awayGoals > 0) {
            processTeamEvents(savedMatch, away, awayGoals);
        }

        System.out.println(String.format("SIMULAÇÃO: %s %d x %d %s",
                home.getName(), homeGoals, awayGoals, away.getName()));

        return savedMatch;

    }

    private int drawGoals(double expectancy) {
        double roll = random.nextDouble() * 100.0;

        if (expectancy > 1.3) {
            roll += 15.0; // Facilita fazer mais gols
        } else if (expectancy < 0.8) {
            roll -= 10.0; // Dificulta fazer gols
        }

        // Definimos os gols com base em faixas da rolagem
        if (roll > 90)
            return 4; // 10% de chance de goleada
        if (roll > 75)
            return 3; // 15% de chance de 3 gols
        if (roll > 45)
            return 2; // 30% de chance de 2 gols
        if (roll > 15)
            return 1; // 30% de chance de 1 gol

        return 0;

    }

    private void processTeamEvents(Match match, NationalTeam team, int goalsCount) {
        List<Player> players = playerRepository.findByTeamId(team.getId());

        if (players.isEmpty())
            return;

        for (int i = 0; i < goalsCount; i++) {
            // Sorteia um jogador aleatório da equipe para ser o autor do gol
            Player scorer = players.get(random.nextInt(players.size()));

            // Criando e salvando o Evento de Gol
            MatchEvent goalEvent = new MatchEvent();
            goalEvent.setMatch(match);
            goalEvent.setPlayer(scorer);
            goalEvent.setEventType(EventType.GOAL);
            goalEvent.setMinute(random.nextInt(90) + 1);
            matchEventRepository.save(goalEvent);

            // 70% de chance de o gol ter tido uma assistência
            if (random.nextDouble() < 0.70 && players.size() > 1) {
                Player assistant;
                // Garante que quem deu a assistência não seja o mesmo cara que fez o gol
                do {
                    assistant = players.get(random.nextInt(players.size()));
                } while (assistant.getId().equals(scorer.getId()));

                MatchEvent assistEvent = new MatchEvent();
                assistEvent.setMatch(match);
                assistEvent.setPlayer(assistant);
                assistEvent.setEventType(EventType.ASSIST);
                assistEvent.setMinute(goalEvent.getMinute()); // No mesmo minuto do gol
                matchEventRepository.save(assistEvent);
            }

        }

    }
}