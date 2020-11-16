package com.onwelo.dice.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@AllArgsConstructor
@Value
public class DiceDistributionSummaryDTO {
    long diceNumber;
    long diceSidesNumber;
    long simulationsNumber;
    long totalRollsNumber;
    List<DiceRollRelativeDistributionDTO> distributions;
}
