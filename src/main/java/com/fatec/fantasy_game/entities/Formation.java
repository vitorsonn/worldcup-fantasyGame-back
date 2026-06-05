package com.fatec.fantasy_game.entities;

public enum Formation {
    F_433("4-3-3", 4, 3, 3),
    F_442("4-4-2", 4, 4, 2),
    F_352("3-5-2", 3, 5, 2),
    F_343("3-4-3", 3, 4, 3),
    F_541("5-4-1", 5, 4, 1),
    F_451("4-5-1", 4, 5, 1);



    private final String label;
    private final int defendersLimit;
    private final int midfieldersLimit;
    private final int attackersLimit;

    Formation(String label, int defendersLimit, int midfieldersLimit, int attackersLimit) {
        this.label = label;
        this.defendersLimit = defendersLimit;
        this.midfieldersLimit = midfieldersLimit;
        this.attackersLimit = attackersLimit;
    }

    public String getLabel() {
        return label;
    }

    public int getLimitByPosition(PlayerPosition position) {
        return switch (position) {
            case GOLEIRO -> 1;
            case ZAGUEIRO, LATERAL -> defendersLimit;
            case MEIA -> midfieldersLimit;
            case ATACANTE -> attackersLimit;
        };
    }
}


