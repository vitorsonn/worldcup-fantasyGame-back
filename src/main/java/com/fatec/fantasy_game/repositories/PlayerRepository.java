package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
