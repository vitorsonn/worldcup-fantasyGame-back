package com.fatec.fantasy_game.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Round {
    private Long id;
    private Long matchId;
    private Integer roundNumber;


}
