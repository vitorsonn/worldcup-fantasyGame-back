package com.fatec.fantasy_game.simulation.dto;

public record FantasyTeamDTO(
    Long id,
    String name,
    Double cash,
    Double totalPoints,
    Double teamValue,
    String formation
) {
}
