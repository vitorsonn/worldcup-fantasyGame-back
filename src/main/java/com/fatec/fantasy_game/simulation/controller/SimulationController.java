package com.fatec.fantasy_game.simulation.controller;

import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.simulation.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @GetMapping("/quick-match")
    public ResponseEntity<?> simulateQuickMatch(@RequestParam Long homeId, @RequestParam Long awayId) {
        try {
            Match result = simulationService.simulateQuickMatch(homeId, awayId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/simulate-round")
    public ResponseEntity<String> simulateRound(@RequestParam("roundId") Long roundId) {
        try {
            String message = simulationService.simulateRound(roundId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/results")
    public ResponseEntity<List<Match>> getRoundResults(@RequestParam Long roundId) {
        return ResponseEntity.ok(simulationService.getRoundResults(roundId));
    }

    @GetMapping("matches/round/{roundId}")
    public ResponseEntity<List<Match>> getMatchesByRound(@PathVariable Long roundId) {
        return ResponseEntity.ok(simulationService.getRoundResults(roundId));
    }
}
