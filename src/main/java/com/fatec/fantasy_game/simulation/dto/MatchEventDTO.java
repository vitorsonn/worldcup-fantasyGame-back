package com.fatec.fantasy_game.simulation.dto;

public record MatchEventDTO(
    Long id,
    PlayerDTO player,
    String eventType,
    Integer minute
) {
}
