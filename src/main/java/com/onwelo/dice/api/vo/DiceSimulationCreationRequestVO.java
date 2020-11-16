package com.onwelo.dice.api.vo;

import javax.validation.constraints.NotNull;
import lombok.Value;
import org.hibernate.validator.constraints.Range;

@Value
public class DiceSimulationCreationRequestVO {

    @Range(min = 1)
    @NotNull
    Long diceNumber;

    @Range(min = 4)
    @NotNull
    Long diceSidesNumber;

    @Range(min = 1)
    @NotNull
    Long rollsNumber;
}
