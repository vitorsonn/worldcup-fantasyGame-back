package com.fatec.fantasy_game.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


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
    private Integer attack;
    private Integer defense;
    private Integer overall;
    private BigDecimal bankBalance;
    private BigDecimal teamValue;



}
