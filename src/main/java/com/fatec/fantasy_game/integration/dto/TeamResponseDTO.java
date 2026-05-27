package com.fatec.fantasy_game.integration.dto;

import java.util.List;

public record TeamResponseDTO(
        List<ExternalTeamDTO> teams
) {
}
