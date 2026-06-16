package com.fatec.fantasy_game.simulation.mapper;

import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.simulation.dto.MatchDTO;
import java.util.List;
import java.util.stream.Collectors;

public class MatchMapper {
    public static MatchDTO toDTO(Match match) {
        if (match == null) return null;
        return new MatchDTO(
            match.getId(),
            NationalTeamMapper.toDTO(match.getHomeTeam()),
            NationalTeamMapper.toDTO(match.getAwayTeam()),
            match.getHomeGoals(),
            match.getAwayGoals(),
            match.getStatus() != null ? match.getStatus().name() : null,
            match.getDate(),
            RoundMapper.toDTO(match.getRound()),
            match.getEvents() != null ? match.getEvents().stream()
                .map(MatchEventMapper::toDTO)
                .collect(Collectors.toList()) : List.of()
        );
    }
}
