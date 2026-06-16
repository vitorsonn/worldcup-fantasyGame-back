package com.fatec.fantasy_game.simulation.controller;

import com.fatec.fantasy_game.enums.Formation;
import com.fatec.fantasy_game.simulation.dto.FantasyTeamDTO;
import com.fatec.fantasy_game.simulation.dto.FantasyTeamPlayerDTO;
import com.fatec.fantasy_game.simulation.dto.PlayerDTO;
import com.fatec.fantasy_game.simulation.dto.TeamRankingDTO;
import com.fatec.fantasy_game.simulation.mapper.FantasyTeamMapper;
import com.fatec.fantasy_game.simulation.mapper.FantasyTeamPlayerMapper;
import com.fatec.fantasy_game.simulation.mapper.PlayerMapper;
import com.fatec.fantasy_game.simulation.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    @Autowired
    private final MarketService marketService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyPlayer(
            @RequestParam Long teamId,
            @RequestParam Long playerId,
            @RequestParam Long roundId) {
        try {
            return ResponseEntity.ok(FantasyTeamPlayerMapper.toDTO(marketService.buyPlayer(teamId, playerId, roundId)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/players")
    public List<PlayerDTO> getAllPlayers() {
        return marketService.getAllPlayers().stream()
                .map(PlayerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{teamId}/roster")
    public ResponseEntity<List<FantasyTeamPlayerDTO>> getMySquad(
            @PathVariable("teamId") Long teamId,
            @RequestParam("roundId") Long roundId) {
        List<FantasyTeamPlayerDTO> squad = marketService.getSquadByTeamAndRound(teamId, roundId).stream()
                .map(FantasyTeamPlayerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(squad);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getTeamInfo(@PathVariable Long teamId) {
        return marketService.getTeamById(teamId)
                .map(FantasyTeamMapper::toDTO)
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
            return ResponseEntity.ok(FantasyTeamMapper.toDTO(marketService.changeTeamFormation(teamId, newFormation, roundId)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/teams/{teamId}/sell-player")
    public ResponseEntity<?> sellPlayer(
            @PathVariable Long teamId,
            @RequestParam("playerId") Long playerId,
            @RequestParam("roundId") Long roundId) {
        try {
            marketService.sellPlayer(teamId, playerId, roundId);
            return ResponseEntity.ok("Jogador removido do elenco e saldo estornado!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
