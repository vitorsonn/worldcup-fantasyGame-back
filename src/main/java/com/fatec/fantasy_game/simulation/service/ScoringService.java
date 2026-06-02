package com.fatec.fantasy_game.simulation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.fatec.fantasy_game.entities.EventType;
import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import com.fatec.fantasy_game.entities.MatchEvent;
import com.fatec.fantasy_game.entities.Score;
import com.fatec.fantasy_game.repositories.FantasyTeamPlayerRepository;
import com.fatec.fantasy_game.repositories.MatchEventRepository;

import jakarta.transaction.Transactional;

@Service
public class ScoringService {

    private final MatchEventRepository eventRepository;
    private final FantasyTeamPlayerRepository squadRepository;

    public ScoringService(MatchEventRepository eventRepository, FantasyTeamPlayerRepository squadRepository) {
        this.eventRepository = eventRepository;
        this.squadRepository = squadRepository;
    }

    @Transactional
    public void calculateRoundScores(Long roundId) {
        List<MatchEvent> roundEvents = eventRepository.findByMatchRoundId(roundId);

        Map<Long, Double> playerScores = new HashMap<>();

        for (MatchEvent event : roundEvents) {
            Long playerId = event.getPlayer().getId();
            Double pointsForEvent = getPointsForEventType(event.getEventType());

            playerScores.put(playerId, playerScores.getOrDefault(playerId, 0.0) + pointsForEvent);
        }

        for (Map.Entry<Long, Double> entry : playerScores.entrySet()) {
            Long playerId = entry.getKey();
            Double totalPoints = entry.getValue();

            List<FantasyTeamPlayer> deployments = squadRepository.findByPlayerIdAndRoundId(playerId, roundId);

            for (FantasyTeamPlayer deployment : deployments) {
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
            default -> 0.0;
        };
    }

}
