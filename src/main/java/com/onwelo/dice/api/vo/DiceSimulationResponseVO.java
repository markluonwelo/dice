package com.onwelo.dice.api.vo;

import java.util.List;

import com.onwelo.dice.api.dto.DiceRollCountDTO;
import lombok.Value;

@Value
public class DiceSimulationResponseVO {
    List<DiceRollCountDTO> diceRolls;
}
