package com.fatec.fantasy_game.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fantasy_teams")
public class FantasyTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ownerName;
    private Integer attack;
    private Integer defense;
    private Integer overall;
    private BigDecimal bankBalance;
    private BigDecimal teamValue;



}
