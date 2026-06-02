package com.fatec.fantasy_game.entities;


import jakarta.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long matchId;
    private Integer roundNumber;


}
