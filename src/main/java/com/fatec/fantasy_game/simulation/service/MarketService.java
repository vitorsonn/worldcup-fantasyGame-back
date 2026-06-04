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

import java.util.List;

@Service
public class MarketService {

    private final FantasyTeamRepository teamRepository;
    private final FantasyTeamPlayerRepository squadRepository;
    private final PlayerRepository playerRepository;
    private final RoundRepository roundRepository;

    public MarketService(FantasyTeamRepository teamRepository, 
                         FantasyTeamPlayerRepository squadRepository,
                         PlayerRepository playerRepository,
                         RoundRepository roundRepository) {
        this.teamRepository = teamRepository;
        this.squadRepository = squadRepository;
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

        List<FantasyTeamPlayer> existingContracts = squadRepository.findByPlayerIdAndRoundId(playerId, roundId);

        boolean alreadyEscalated = existingContracts.stream()
                .anyMatch(contract -> contract.getFantasyTeam().getId().equals(teamId));

        if(alreadyEscalated){
            throw new RuntimeException("Jogador já escalado para esta rodada! Você não pode comprar o mesmo jogador mais de uma vez por rodada.");
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
