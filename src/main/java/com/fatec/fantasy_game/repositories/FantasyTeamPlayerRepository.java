package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.FantasyTeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FantasyTeamPlayerRepository extends JpaRepository<FantasyTeamPlayer, Long> {
}
