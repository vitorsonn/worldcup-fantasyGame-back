package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.NationalTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NationalTeamRepository extends JpaRepository<NationalTeam, Long> {
    Optional<NationalTeam> findByNameIgnoreCase(String name);
}
