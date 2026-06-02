package com.fatec.fantasy_game.simulation.service;

import org.springframework.stereotype.Service;
import com.fatec.fantasy_game.entities.FantasyTeam;
import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import com.fatec.fantasy_game.entities.Player;
import com.fatec.fantasy_game.entities.Round;
import com.fatec.fantasy_game.repositories.FantasyTeamPlayerRepository;
import com.fatec.fantasy_game.repositories.FantasyTeamRepository;
import com.fatec.fantasy_game.repositories.PlayerRepository;
import com.fatec.fantasy_game.repositories.RoundRepository;

import jakarta.transaction.Transactional;

@Service
public class MarketService {

    private final FantasyTeamRepository teamRepository;
    private final FantasyTeamPlayerRepository squadRepository;
    private final PlayerRepository playerRepository;
    private final RoundRepository roundRepository;

    public MarketService(FantasyTeamRepository teamRepository, 
                         FantasyTeamPlayerRepository rosterRepository, 
                         PlayerRepository playerRepository,
                         RoundRepository roundRepository) {
        this.teamRepository = teamRepository;
        this.squadRepository = rosterRepository;
        this.playerRepository = playerRepository;
        this.roundRepository = roundRepository;
    }

    @Transactional
    public FantasyTeamPlayer buyPlayer(Long teamId, Long playerId, Long roundId) {
        FantasyTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Time Fantasia não encontrado!"));
                
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado!"));
                
        Round round = roundRepository.findById(roundId)
                .orElseThrow(() -> new RuntimeException("Rodada não encontrada!"));

        if (team.getCash() < player.getCurrentPrice()) {
            throw new RuntimeException("Saldo insuficiente! Você precisa de mais moedas.");
        }

        team.setCash(team.getCash() - player.getCurrentPrice());
        teamRepository.save(team);

        FantasyTeamPlayer contract = new FantasyTeamPlayer();
        contract.setFantasyTeam(team);
        contract.setPlayer(player);
        contract.setRound(round);
        contract.setRoundPoints(0.0);

        return squadRepository.save(contract);
    }

}
