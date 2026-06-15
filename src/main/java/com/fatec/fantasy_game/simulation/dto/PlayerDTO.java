package com.fatec.fantasy_game.simulation.dto;

public record PlayerDTO(
    Long id,
    String name,
    String position,
    NationalTeamDTO team,
    Double currentPrice
) {
}
