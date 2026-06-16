package com.fatec.fantasy_game.simulation.mapper;

import com.fatec.fantasy_game.entities.Round;
import com.fatec.fantasy_game.simulation.dto.RoundDTO;

public class RoundMapper {
    public static RoundDTO toDTO(Round round) {
        if (round == null) return null;
        return new RoundDTO(
            round.getId(),
            round.getRoundNumber()
        );
    }
}
