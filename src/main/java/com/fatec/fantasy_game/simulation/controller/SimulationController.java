package com.fatec.fantasy_game.simulation.controller;


import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.entities.Round;
import com.fatec.fantasy_game.repositories.FantasyTeamPlayerRepository;
import com.fatec.fantasy_game.repositories.MatchRepository;
import com.fatec.fantasy_game.repositories.NationalTeamRepository;
import com.fatec.fantasy_game.repositories.RoundRepository;
import com.fatec.fantasy_game.simulation.service.MatchSimulatorService;
import com.fatec.fantasy_game.simulation.service.ScoringService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final MatchSimulatorService simulationService;
    private final NationalTeamRepository nationalTeamRepository;
    private final ScoringService scoringService;
    private final MatchRepository matchRepository;
    private final FantasyTeamPlayerRepository fantasyTeamPlayerRepository;
    private final RoundRepository roundRepository;

    @PersistenceContext
    private EntityManager entityManager;





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
    @Transactional
    public ResponseEntity<String> simulateRound(@RequestParam("roundId") Long roundId) {

        List<Match> matches = matchRepository.findByRoundId(roundId);
        if (matches.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma partida encontrada para a rodada " + roundId);
        }

        if (roundId > 1) {
            Long previousRoundId = roundId - 1;
            List<FantasyTeamPlayer> currentSquad = fantasyTeamPlayerRepository.findByRoundId(roundId);

            if (currentSquad.isEmpty()) {
                List<FantasyTeamPlayer> previousSquad = fantasyTeamPlayerRepository.findByRoundId(previousRoundId);
                Round currentRound = roundRepository.findById(roundId)
                        .orElseThrow(() -> new RuntimeException("Rodada " + roundId + " não encontrada no banco!"));

                for (FantasyTeamPlayer oldContract : previousSquad) {
                    FantasyTeamPlayer newContract = new FantasyTeamPlayer();
                    newContract.setFantasyTeam(oldContract.getFantasyTeam());
                    newContract.setPlayer(oldContract.getPlayer());
                    newContract.setRound(currentRound);
                    newContract.setRoundPoints(0.0);

                    fantasyTeamPlayerRepository.save(newContract);
                }
                System.out.println("🔄 Elenco da Rodada " + previousRoundId + " clonado para a Rodada " + roundId);
            }
        }

        for (Match match : matches) {
            simulationService.simulateMatch(match);
        }

        scoringService.calculateRoundScores(roundId);

        entityManager.flush();
        entityManager.clear();

        return ResponseEntity.ok("Rodada " + roundId + " simulada e pontuações distribuídas com sucesso!");
    }

    @GetMapping("/results")
    public List<Match>getRoundResults(@RequestParam Long roundId){
        return matchRepository.findByRoundId(roundId);
    }

    @GetMapping("matches/round/{roundId}")
    public ResponseEntity<List<Match>> getMatchesByRound(@PathVariable Long roundId) {
        List<Match> matches = matchRepository.findByRoundId(roundId);
        return ResponseEntity.ok(matches);

    }

}
