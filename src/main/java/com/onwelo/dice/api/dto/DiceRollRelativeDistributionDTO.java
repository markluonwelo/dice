package com.onwelo.dice.api.dto;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class DiceRollRelativeDistributionDTO {
    long rollSum;
    BigDecimal relativeDistributionPercentage;
}
