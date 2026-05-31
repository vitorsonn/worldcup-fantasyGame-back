package com.fatec.fantasy_game.simulation.controller;


import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.repositories.MatchRepository;
import com.fatec.fantasy_game.repositories.NationalTeamRepository;
import com.fatec.fantasy_game.simulation.service.MatchSimulatorService;
import com.fatec.fantasy_game.simulation.service.ScoringService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final MatchSimulatorService simulationService;
    private final NationalTeamRepository nationalTeamRepository;
    private final ScoringService scoringService;
    private final MatchRepository matchRepository;


    @GetMapping("/quick-match")
    public ResponseEntity<?> simulateQuickMatch(@RequestParam Long homeId, @RequestParam Long awayId){
        NationalTeam home = nationalTeamRepository.findById(homeId).orElse(null);
        NationalTeam away = nationalTeamRepository.findById(awayId).orElse(null);

        if(home == null || away == null){
            return ResponseEntity.badRequest().body("Ambas as equipes devem ser válidas.");
        }

        Match temporaryMatch = new Match();
        temporaryMatch.setHomeTeam(home);
        temporaryMatch.setAwayTeam(away);

        Match result = simulationService.simulateMatch(temporaryMatch);

        return ResponseEntity.ok(result);

    }

    @PostMapping("/simulate-round")
    public ResponseEntity<String>simulateRound(@RequestParam Long roundId){
        List<Match> matches = matchRepository.findByRoundId(roundId);

        if (matches.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma partida encontrada para a rodada " + roundId);
        }

        for (Match match : matches) {
            simulationService.simulateMatch(match);
        }

        scoringService.calculateRoundScores(roundId);

        return ResponseEntity.ok("Rodada " + roundId + " simulada e pontuações distribuídas com sucesso!");

    }

}
