package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.FantasyTeam;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FantasyTeamRepository extends JpaRepository<FantasyTeam, Long> {
   
    Optional<FantasyTeam> findByOwnerId(Long userId);

}
