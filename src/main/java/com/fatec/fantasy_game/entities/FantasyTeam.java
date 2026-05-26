package com.fatec.fantasy_game.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "fantasy_teams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FantasyTeam {

    private Long id;
    private String name;
    private String ownerName;



}
