package com.fatec.fantasy_game.simulation.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MatchDTO(
    Long id,
    NationalTeamDTO homeTeam,
    NationalTeamDTO awayTeam,
    Integer homeGoals,
    Integer awayGoals,
    String status,
    LocalDateTime date,
    RoundDTO round,
    List<MatchEventDTO> events
) {
}
