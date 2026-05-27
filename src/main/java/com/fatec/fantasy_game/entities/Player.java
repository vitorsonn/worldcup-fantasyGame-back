package com.fatec.fantasy_game.entities;


import jakarta.persistence.*;
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
    private PlayerPosition positon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "national_team_id", nullable = false)
    private NationalTeam team;

    private Double currentPrice;





}
