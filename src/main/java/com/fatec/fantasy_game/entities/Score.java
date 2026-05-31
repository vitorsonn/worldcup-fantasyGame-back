package com.fatec.fantasy_game.entities;

import lombok.Getter;

@Getter
public enum Score {
GOAL(8.0),
ASSIST(5.0),
RED_CARD(-5.0),
YELLOW_CARD(-2.0),
OWN_GOAL(-10.0);

private final Double value;

Score(Double value){
    this.value = value;
}

}
