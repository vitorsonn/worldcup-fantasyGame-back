package com.fatec.fantasy_game.simulation.dto;

public class TeamRankingDTO {
    private String teamName;
    private Double totalPoints;
    private Double cash;

    public TeamRankingDTO(String teamName, Double totalPoints, Double cash) {
        this.teamName = teamName;
        this.totalPoints = totalPoints;
        this.cash = cash;
    }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public Double getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Double totalPoints) { this.totalPoints = totalPoints; }
    public Double getCash() { return cash; }
    public void setCash(Double cash) { this.cash = cash; }
}