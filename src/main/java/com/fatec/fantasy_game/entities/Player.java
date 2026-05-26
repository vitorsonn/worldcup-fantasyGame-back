package com.fatec.fantasy_game.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBL_PLAYER")
@Getter
@Setter
public class Player {
    private Long id;
    private String name;
    private String position;
    private NationalTeam team;





}
