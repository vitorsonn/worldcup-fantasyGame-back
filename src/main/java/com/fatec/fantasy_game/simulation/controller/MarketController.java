package com.fatec.fantasy_game.simulation.controller;

import com.fatec.fantasy_game.entities.*;
import com.fatec.fantasy_game.repositories.*;
import com.fatec.fantasy_game.simulation.dto.SquadPlayerDTO;
import com.fatec.fantasy_game.simulation.dto.TeamRankingDTO;
import com.fatec.fantasy_game.simulation.service.MarketService;
import com.fatec.fantasy_game.simulation.service.ScoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;
    private final ScoringService scoringService;
    private final PlayerRepository playerRepository;
    private final FantasyTeamPlayerRepository fantasyTeamPlayerRepository;
    private final FantasyTeamRepository fantasyTeamRepository;

    public MarketController(MarketService marketService, PlayerRepository playerRepository, FantasyTeamPlayerRepository fantasyTeamPlayerRepository, FantasyTeamRepository fantasyTeamRepository, MatchRepository matchRepository, ScoringService scoringService) {
        this.marketService = marketService;
        this.scoringService = scoringService;
        this.playerRepository = playerRepository;
        this.fantasyTeamPlayerRepository = fantasyTeamPlayerRepository;
        this.fantasyTeamRepository = fantasyTeamRepository;



    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyPlayer(
            @RequestParam Long teamId,
            @RequestParam Long playerId,
            @RequestParam Long roundId) {
        try {
            FantasyTeamPlayer contract = marketService.buyPlayer(teamId, playerId, roundId);
            return ResponseEntity.ok(contract);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/players")
    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    @GetMapping("/{teamId}/roster")
    public ResponseEntity<List<FantasyTeamPlayer>> getMySquad(
            @PathVariable("teamId") Long teamId,
            @RequestParam("roundId") Long roundId) { // 🌟 Adicionado o roundId dinâmico

        System.out.println("🔍 Buscando elenco do Time ID: " + teamId + " especificamente para a Rodada ID: " + roundId);


        List<FantasyTeamPlayer> squad = fantasyTeamPlayerRepository.findByFantasyTeamIdAndRoundId(teamId, roundId);

        return ResponseEntity.ok(squad);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getTeamInfo(@PathVariable Long teamId) {
        return fantasyTeamRepository.findById(teamId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<TeamRankingDTO>> getLeaderboard() {
        List<FantasyTeam> teams = fantasyTeamRepository.findAll();
        List<TeamRankingDTO> ranking = new java.util.ArrayList<>();

        for (FantasyTeam team : teams) {
            Double totalPoints = fantasyTeamPlayerRepository.findByFantasyTeamId(team.getId())
                    .stream()
                    .mapToDouble(FantasyTeamPlayer::getRoundPoints)
                    .sum();

            ranking.add(new TeamRankingDTO(team.getName(), totalPoints, team.getCash()));
        }

        ranking.sort((a, b) -> b.getTotalPoints().compareTo(a.getTotalPoints()));

        return ResponseEntity.ok(ranking);
    }

    @PutMapping("/teams/{teamId}/formation")
    public ResponseEntity<?> updateFormation(
            @PathVariable Long teamId,
            @RequestParam("formation") Formation newFormation,
            @RequestParam("roundId") Long roundId) {
        try {
            FantasyTeam updatedTeam = marketService.changeTeamFormation(teamId, newFormation, roundId);
            return ResponseEntity.ok(updatedTeam);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}