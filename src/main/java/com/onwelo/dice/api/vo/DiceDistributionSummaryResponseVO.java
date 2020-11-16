package com.onwelo.dice.api.vo;

import java.util.Set;

import com.onwelo.dice.api.dto.DiceDistributionSummaryDTO;
import lombok.Value;

@Value
public class DiceDistributionSummaryResponseVO {
    Set<DiceDistributionSummaryDTO> simulationsSummaries;
}
