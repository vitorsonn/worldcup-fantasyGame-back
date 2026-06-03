package com.fatec.fantasy_game.simulation.dto;

public record SquadPlayerDTO(Long id,
                             String position,
                             String name,
                             Double price,
                             Double roundPoints,
                             Double totalPoints) {
}
