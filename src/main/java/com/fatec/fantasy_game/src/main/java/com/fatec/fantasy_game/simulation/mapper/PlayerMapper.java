package com.fatec.fantasy_game.simulation.mapper;

import com.fatec.fantasy_game.entities.Player;
import com.fatec.fantasy_game.simulation.dto.PlayerDTO;

public class PlayerMapper {
    public static PlayerDTO toDTO(Player player) {
        if (player == null) return null;
        return new PlayerDTO(
            player.getId(),
            player.getName(),
            player.getPosition() != null ? player.getPosition().name() : null,
            NationalTeamMapper.toDTO(player.getTeam()),
            player.getCurrentPrice()
        );
    }
}
