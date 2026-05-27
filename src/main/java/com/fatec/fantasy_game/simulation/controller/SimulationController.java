package com.fatec.fantasy_game.simulation.controller;


import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.repositories.NationalTeamRepository;
import com.fatec.fantasy_game.simulation.service.MatchSimulatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final MatchSimulatorService simulationService;
    private final NationalTeamRepository nationalTeamRepository;

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

}
