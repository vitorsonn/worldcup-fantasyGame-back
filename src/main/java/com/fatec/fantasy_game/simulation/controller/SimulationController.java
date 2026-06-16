package com.fatec.fantasy_game.simulation.controller;

import com.fatec.fantasy_game.simulation.dto.MatchDTO;
import com.fatec.fantasy_game.simulation.mapper.MatchMapper;
import com.fatec.fantasy_game.simulation.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    @Autowired
    private final SimulationService simulationService;

    @GetMapping("/quick-match")
    public ResponseEntity<?> simulateQuickMatch(@RequestParam Long homeId, @RequestParam Long awayId) {
        try {
            return ResponseEntity.ok(MatchMapper.toDTO(simulationService.simulateQuickMatch(homeId, awayId)));
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
    public ResponseEntity<List<MatchDTO>> getRoundResults(@RequestParam Long roundId) {
        List<MatchDTO> results = simulationService.getRoundResults(roundId).stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("matches/round/{roundId}")
    public ResponseEntity<List<MatchDTO>> getMatchesByRound(@PathVariable Long roundId) {
        List<MatchDTO> matches = simulationService.getRoundResults(roundId).stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(matches);
    }
}
