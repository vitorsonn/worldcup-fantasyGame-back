package com.fatec.fantasy_game.simulation.controller;

import com.fatec.fantasy_game.entities.FantasyTeam;
import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import com.fatec.fantasy_game.enums.Formation;
import com.fatec.fantasy_game.entities.Player;
import com.fatec.fantasy_game.simulation.dto.TeamRankingDTO;
import com.fatec.fantasy_game.simulation.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

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
    public List<Player> getAllPlayers() {
        return marketService.getAllPlayers();
    }

    @GetMapping("/{teamId}/roster")
    public ResponseEntity<List<FantasyTeamPlayer>> getMySquad(
            @PathVariable("teamId") Long teamId,
            @RequestParam("roundId") Long roundId) {
        return ResponseEntity.ok(marketService.getSquadByTeamAndRound(teamId, roundId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getTeamInfo(@PathVariable Long teamId) {
        return marketService.getTeamById(teamId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<TeamRankingDTO>> getLeaderboard() {
        return ResponseEntity.ok(marketService.getLeaderboard());
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
