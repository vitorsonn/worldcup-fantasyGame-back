package com.fatec.fantasy_game.simulation.service;

import com.fatec.fantasy_game.entities.*;
import org.springframework.stereotype.Service;
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

        List<FantasyTeamPlayer> currentSquad = squadRepository.findByFantasyTeamIdAndRoundId(teamId, roundId);

        long currentCount = currentSquad.stream().filter(contract -> contract.getPlayer().getPosition() == player.getPosition()).count();
        int positionLimit = team.getFormation().getLimitByPosition(player.getPosition());

        if(currentCount >= positionLimit){
            throw new RuntimeException("Limite de jogadores para a posição " + player.getPosition() + " atingido! Você só pode ter " + positionLimit + " jogadores nessa posição com a formação atual.");
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

    @Transactional
    public FantasyTeam changeTeamFormation(Long teamId, Formation newFormation, Long roundId) {
        // 1. Busca o time no banco
        FantasyTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Time não encontrado!"));

        // 2. Busca o elenco escalado pelo usuário para a rodada atual
        List<FantasyTeamPlayer> currentSquad = squadRepository.findByFantasyTeamIdAndRoundId(teamId, roundId);

        // 3. Valida se o elenco atual cabe dentro das regras da nova formação
        for (PlayerPosition position : PlayerPosition.values()) {
            // Conta quantos jogadores o usuário já tem nessa posição
            long currentCount = currentSquad.stream()
                    .filter(contract -> contract.getPlayer().getPosition() == position)
                    .count();

            // Pega o limite que a NOVA formação desejada impõe
            int maxAllowedByNewFormation = newFormation.getLimitByPosition(position);

            // Se ele já tiver mais jogadores comprados do que a nova formação permite, bloqueia!
            if (currentCount > maxAllowedByNewFormation) {
                throw new RuntimeException("Não é possível mudar para " + newFormation.getLabel() +
                        "! Você já possui " + currentCount + " jogador(es) da posição " + position +
                        " escalados, mas essa formação só permite no máximo " + maxAllowedByNewFormation + ".");
            }
        }

        // 4. Se passou na validação, atualiza a formação do time e salva
        team.setFormation(newFormation);
        return teamRepository.save(team);
    }

}
