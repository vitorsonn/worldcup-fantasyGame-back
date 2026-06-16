package com.fatec.fantasy_game.repositories;

import com.fatec.fantasy_game.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
