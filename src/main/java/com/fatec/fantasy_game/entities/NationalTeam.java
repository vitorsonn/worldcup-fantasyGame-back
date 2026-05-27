package com.fatec.fantasy_game.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "TBL_TEAM")
@Entity
public class NationalTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String conf;
    private Double attackScore;
    private Double midScore;
    private Double defenseScore;
    private Double overallScore;




}
