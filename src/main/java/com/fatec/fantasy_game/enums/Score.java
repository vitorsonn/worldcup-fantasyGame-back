package com.fatec.fantasy_game.enums;

import lombok.Getter;

@Getter
public enum Score {
GOAL(8.0),
ASSIST(5.0),
RED_CARD(-5.0),
YELLOW_CARD(-2.0),
CLEAN_SHEET(4.0);

private final Double value;

Score(Double value){
    this.value = value;
}

}
