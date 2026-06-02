package com.fatec.fantasy_game.simulation.controller;

import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.entities.Player;
import com.fatec.fantasy_game.repositories.*;
import com.fatec.fantasy_game.simulation.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;
    private final PlayerRepository playerRepository;
    private final FantasyTeamPlayerRepository fantasyTeamPlayerRepository;
    private final FantasyTeamRepository fantasyTeamRepository;
    private final MatchRepository matchRepository;

    public MarketController(MarketService marketService, PlayerRepository playerRepository, FantasyTeamPlayerRepository fantasyTeamPlayerRepository, FantasyTeamRepository fantasyTeamRepository, MatchRepository matchRepository) {
        this.marketService = marketService;
        this.playerRepository = playerRepository;
        this.fantasyTeamPlayerRepository = fantasyTeamPlayerRepository;
        this.fantasyTeamRepository = fantasyTeamRepository;
        this.matchRepository = matchRepository;

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
    public List<FantasyTeamPlayer> getMySquad(@PathVariable Long teamId){
        return fantasyTeamPlayerRepository.findByFantasyTeamId(teamId);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getTeamInfo(@PathVariable Long teamId) {
        return fantasyTeamRepository.findById(teamId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}