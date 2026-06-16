package com.fatec.fantasy_game.simulation.mapper;

import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import com.fatec.fantasy_game.simulation.dto.FantasyTeamPlayerDTO;

public class FantasyTeamPlayerMapper {
    public static FantasyTeamPlayerDTO toDTO(FantasyTeamPlayer contract) {
        if (contract == null) return null;
        return new FantasyTeamPlayerDTO(
            contract.getId(),
            FantasyTeamMapper.toDTO(contract.getFantasyTeam()),
            PlayerMapper.toDTO(contract.getPlayer()),
            RoundMapper.toDTO(contract.getRound()),
            contract.getRoundPoints()
        );
    }
}
