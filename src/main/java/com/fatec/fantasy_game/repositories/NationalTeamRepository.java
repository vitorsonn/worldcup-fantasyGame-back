package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.NationalTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalTeamRepository extends JpaRepository<NationalTeam, Long> {
}
