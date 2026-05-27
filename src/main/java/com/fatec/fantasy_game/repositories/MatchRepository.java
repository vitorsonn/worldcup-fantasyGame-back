package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

}
