package com.fatec.fantasy_game.simulation.dto;

public record FantasyTeamPlayerDTO(
    Long id,
    FantasyTeamDTO fantasyTeam,
    PlayerDTO player,
    RoundDTO round,
    Double roundPoints
) {
}
