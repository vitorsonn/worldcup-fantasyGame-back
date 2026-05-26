package com.fatec.fantasy_game.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TBL_MATCH")
public class Match {

    private Long id;
    private NationalTeam teamA;
    private NationalTeam teamB;
    private LocalDateTime date;


}
