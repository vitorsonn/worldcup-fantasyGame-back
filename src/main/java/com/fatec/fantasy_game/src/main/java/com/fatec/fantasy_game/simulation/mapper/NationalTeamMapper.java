package com.fatec.fantasy_game.simulation.mapper;

import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.simulation.dto.NationalTeamDTO;

public class NationalTeamMapper {
    public static NationalTeamDTO toDTO(NationalTeam team) {
        if (team == null) return null;
        return new NationalTeamDTO(
            team.getId(),
            team.getName(),
            team.getConf(),
            team.getAttackScore(),
            team.getMidScore(),
            team.getDefenseScore(),
            team.getOverallScore()
        );
    }
}
