package com.fatec.fantasy_game.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fatec.fantasy_game.entities.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {
}
