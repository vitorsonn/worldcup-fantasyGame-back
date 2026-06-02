package com.fatec.fantasy_game.repositories;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fatec.fantasy_game.entities.MatchEvent;
import org.springframework.data.jpa.repository.Modifying;


public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
    List<MatchEvent> findByMatchRoundId(Long roundId);
    @Transactional
    @Modifying
    List<MatchEvent>deleteByMatchId(Long matchId);

}
