package com.fatec.fantasy_game.simulation.service;

import com.fatec.fantasy_game.entities.Match;
import com.fatec.fantasy_game.entities.MatchStatus;
import com.fatec.fantasy_game.entities.NationalTeam;
import com.fatec.fantasy_game.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MatchSimulatorService {


    private final MatchRepository matchRepository;
    private final Random random = new Random();

    public MatchSimulatorService(MatchRepository matchRepository){
        this.matchRepository = matchRepository;
    }

    public Match simulateMatch(Match match){
        NationalTeam home = match.getHomeTeam();
        NationalTeam away = match.getAwayTeam();

        double homeExpectancy = (home.getAttackScore() / away.getDefenseScore()) * 1.5;
        double awayExpectancy = (away.getAttackScore() / home.getDefenseScore()) * 1.2;


        int homeGoals = drawGoals(homeExpectancy);
        int awayGoals = drawGoals(awayExpectancy);

        match.setHomeGoals(homeGoals);
        match.setAwayGoals(awayGoals);
        match.setStatus(MatchStatus.CONCLUIDA);

        System.out.println(String.format("SIMULAÇÃO: %s %d x %d %s",
                home.getName(), homeGoals, awayGoals, away.getName()));

        return matchRepository.save(match);

    }






    private int drawGoals(double expectancy){
        double roll = random.nextDouble() * 100.0;

        if (expectancy > 1.3) {
            roll += 15.0; // Facilita fazer mais gols
        } else if (expectancy < 0.8) {
            roll -= 10.0; // Dificulta fazer gols
        }

        // Definimos os gols com base em faixas da rolagem
        if (roll > 90) return 4;      // 10% de chance de goleada
        if (roll > 75) return 3;      // 15% de chance de 3 gols
        if (roll > 45) return 2;      // 30% de chance de 2 gols
        if (roll > 15) return 1;      // 30% de chance de 1 gol

        return 0;

    }





}
