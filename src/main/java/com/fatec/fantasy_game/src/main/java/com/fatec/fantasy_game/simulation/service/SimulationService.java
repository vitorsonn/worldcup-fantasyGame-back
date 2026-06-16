package com.fatec.fantasy_game.simulation.service;

import com.fatec.fantasy_game.entities.*;
import com.fatec.fantasy_game.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final MatchSimulatorService matchSimulatorService;
    private final ScoringService scoringService;
    private final NationalTeamRepository nationalTeamRepository;
    private final MatchRepository matchRepository;
    private final FantasyTeamPlayerRepository fantasyTeamPlayerRepository;
    private final RoundRepository roundRepository;

    public Match simulateQuickMatch(Long homeId, Long awayId) {
        NationalTeam home = nationalTeamRepository.findById(homeId)
                .orElseThrow(() -> new RuntimeException("Time da casa não encontrado."));
        NationalTeam away = nationalTeamRepository.findById(awayId)
                .orElseThrow(() -> new RuntimeException("Time visitante não encontrado."));

        Match temporaryMatch = new Match();
        temporaryMatch.setHomeTeam(home);
        temporaryMatch.setAwayTeam(away);

        return matchSimulatorService.simulateMatch(temporaryMatch);
    }

    @Transactional
    public String simulateRound(Long roundId) {
        List<Match> matches = matchRepository.findByRoundId(roundId);
        if (matches.isEmpty()) {
            throw new RuntimeException("Nenhuma partida encontrada para a rodada " + roundId);
        }

        Round currentRound = roundRepository.findById(roundId)
                .orElseThrow(() -> new RuntimeException("Rodada " + roundId + " não encontrada!"));

        if (currentRound.getSimulationCount() >= 3) {
            throw new RuntimeException("A rodada " + roundId + " já foi simulada 3 vezes. Não é possível simular novamente.");
        }

        currentRound.setSimulationCount(currentRound.getSimulationCount() + 1);
        roundRepository.save(currentRound);

        if (roundId > 1) {
            Long previousRoundId = roundId - 1;
            List<FantasyTeamPlayer> currentSquad = fantasyTeamPlayerRepository.findByRoundId(roundId);

            if (currentSquad.isEmpty()) {
                List<FantasyTeamPlayer> previousSquad = fantasyTeamPlayerRepository.findByRoundId(previousRoundId);

                for (FantasyTeamPlayer oldContract : previousSquad) {
                    FantasyTeamPlayer newContract = new FantasyTeamPlayer();
                    newContract.setFantasyTeam(oldContract.getFantasyTeam());
                    newContract.setPlayer(oldContract.getPlayer());
                    newContract.setRound(currentRound);
                    newContract.setRoundPoints(0.0);

                    fantasyTeamPlayerRepository.save(newContract);
                }
            }
        }

        for (Match match : matches) {
            matchSimulatorService.simulateMatch(match);
        }

        scoringService.calculateRoundScores(roundId);

        return "Rodada " + roundId + " simulada com sucesso! (Tentativa " + currentRound.getSimulationCount() + "/3)";
    }

    public List<Match> getRoundResults(Long roundId) {
        return matchRepository.findByRoundId(roundId);
    }
}
