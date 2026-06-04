package com.fatec.fantasy_game.entities;

public enum PlayerPosition {
    GOLEIRO(0.995),
    LATERAL(0.85),
    ZAGUEIRO(0.85),
    MEIA(0.50),
    ATACANTE(0.15);

    private final double goalCutoff;

    PlayerPosition(double goalCutoff) {
        this.goalCutoff = goalCutoff;
    }

    public double getGoalCutoff() {
        return goalCutoff;
    }
}
