package com.fatec.fantasy_game.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fatec.fantasy_game.entities.Round;

import java.util.Optional;

public interface RoundRepository extends JpaRepository<Round, Long> {
    Optional<Round> findByRoundNumber(Integer roundNumber);
}
