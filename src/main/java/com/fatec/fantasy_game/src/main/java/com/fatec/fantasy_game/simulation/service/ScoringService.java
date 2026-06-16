package com.fatec.fantasy_game.simulation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fatec.fantasy_game.entities.*;
import com.fatec.fantasy_game.enums.EventType;
import com.fatec.fantasy_game.enums.Score;
import com.fatec.fantasy_game.repositories.FantasyTeamRepository;
import org.springframework.stereotype.Service;
import com.fatec.fantasy_game.repositories.FantasyTeamPlayerRepository;
import com.fatec.fantasy_game.repositories.MatchEventRepository;

import jakarta.transaction.Transactional;

@Service
public class ScoringService {

    private final MatchEventRepository eventRepository;
    private final FantasyTeamPlayerRepository squadRepository;
    private final FantasyTeamRepository fantasyTeamRepository;

    public ScoringService(MatchEventRepository eventRepository, FantasyTeamPlayerRepository squadRepository, FantasyTeamRepository fantasyTeamRepository) {
        this.eventRepository = eventRepository;
        this.squadRepository = squadRepository;
        this.fantasyTeamRepository = fantasyTeamRepository;
    }

    @Transactional
    public void calculateRoundScores(Long roundId) {
        List<MatchEvent> roundEvents = eventRepository.findByMatchRoundId(roundId);
        Map<Long, Double> playerScores = new HashMap<>();

        for (MatchEvent event : roundEvents) {
            if (event.getPlayer() == null || event.getEventType() == null) {
                continue;
            }

            Long playerId = event.getPlayer().getId();
            Double pointsForEvent = getPointsForEventType(event.getEventType());

            playerScores.put(playerId, playerScores.getOrDefault(playerId, 0.0) + pointsForEvent);
        }

        for (Map.Entry<Long, Double> entry : playerScores.entrySet()) {
            Long playerId = entry.getKey();
            Double totalPoints = entry.getValue();

            List<FantasyTeamPlayer> deployments = squadRepository.findByPlayerIdAndRoundId(playerId, roundId);

            System.out.println("Jogador ID: " + playerId + " fez " + totalPoints + " pontos na Rodada " + roundId);
            System.out.println("Encontradas " + deployments.size() + " escalações para atualizar.");

            for (FantasyTeamPlayer deployment : deployments) {
                deployment.setRoundPoints(totalPoints);
                squadRepository.save(deployment);

                FantasyTeam team = deployment.getFantasyTeam();
                if (team != null) {
                    double oldRoundPoints = deployment.getRoundPoints() != null ? deployment.getRoundPoints() : 0.0;
                    double currentTotalPoints = team.getTotalPoints() != null ? team.getTotalPoints() : 0.0;
                    team.setTotalPoints(currentTotalPoints - oldRoundPoints + totalPoints);
                    fantasyTeamRepository.save(team);

                }

                deployment.setRoundPoints(totalPoints);
                squadRepository.save(deployment);
            }
        }

        System.out.println("Pontuação da rodada " + roundId + " calculada com sucesso para todos os times!");
    }


    private Double getPointsForEventType(EventType type) {
        return switch (type) {
            case GOAL -> Score.GOAL.getValue();
            case ASSIST -> Score.ASSIST.getValue();
            case YELLOW_CARD -> Score.YELLOW_CARD.getValue();
            case RED_CARD -> Score.RED_CARD.getValue();
            case CLEAN_SHEET -> Score.CLEAN_SHEET.getValue();
            default -> 0.0;
        };
    }

}
