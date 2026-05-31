package com.fatec.fantasy_game.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fatec.fantasy_game.entities.MatchEvent;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    List<MatchEvent> findByMatchRoundId(Long roundId);

}
