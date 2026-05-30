package com.fatec.fantasy_game.simulation.controller;

import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import com.fatec.fantasy_game.simulation.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
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
}