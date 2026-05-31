package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.FantasyTeamPlayer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FantasyTeamPlayerRepository extends JpaRepository<FantasyTeamPlayer, Long> {
    List<FantasyTeamPlayer> findByFantasyTeamIdAndRoundId(Long fantasyTeamId, Long roundId);
    List<FantasyTeamPlayer> findByPlayerIdAndRoundId(Long playerId, Long roundId);
}
