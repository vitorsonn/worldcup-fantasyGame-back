package com.fatec.fantasy_game.simulation.mapper;

import com.fatec.fantasy_game.entities.MatchEvent;
import com.fatec.fantasy_game.simulation.dto.MatchEventDTO;

public class MatchEventMapper {
    public static MatchEventDTO toDTO(MatchEvent event) {
        if (event == null) return null;
        return new MatchEventDTO(
            event.getId(),
            PlayerMapper.toDTO(event.getPlayer()),
            event.getEventType() != null ? event.getEventType().name() : null,
            event.getMinute()
        );
    }
}
