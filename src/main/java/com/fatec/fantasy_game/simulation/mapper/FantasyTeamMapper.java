package com.fatec.fantasy_game.simulation.mapper;

import com.fatec.fantasy_game.entities.FantasyTeam;
import com.fatec.fantasy_game.simulation.dto.FantasyTeamDTO;

public class FantasyTeamMapper {
    public static FantasyTeamDTO toDTO(FantasyTeam team) {
        if (team == null) return null;
        return new FantasyTeamDTO(
            team.getId(),
            team.getName(),
            team.getCash(),
            team.getTotalPoints(),
            team.getTeamValue(),
            team.getFormation() != null ? team.getFormation().name() : null
        );
    }
}
