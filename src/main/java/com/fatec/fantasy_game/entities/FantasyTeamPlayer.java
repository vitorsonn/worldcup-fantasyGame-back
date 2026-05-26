package com.fatec.fantasy_game.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fantasy_team_players")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FantasyTeamPlayer {

    private Long id;
    private Long fantasyTeamId;
    private Long playerId;
}
