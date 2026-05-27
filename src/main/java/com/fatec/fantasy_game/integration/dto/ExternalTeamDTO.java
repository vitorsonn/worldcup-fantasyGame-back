package com.fatec.fantasy_game.integration.dto;

import java.util.List;

public record ExternalTeamDTO(
        Long id,
        String name,
        String tla,
        String crest,
        List<ExternalPlayerDTO> squad
) {
}
