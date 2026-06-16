package com.fatec.fantasy_game.simulation.dto;

public record NationalTeamDTO(
    Long id,
    String name,
    String conf,
    Double attackScore,
    Double midScore,
    Double defenseScore,
    Double overallScore
) {
}
