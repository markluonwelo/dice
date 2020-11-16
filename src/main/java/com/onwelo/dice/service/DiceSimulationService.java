package com.onwelo.dice.service;

import com.onwelo.dice.api.vo.DiceDistributionSummaryResponseVO;
import com.onwelo.dice.api.vo.DiceSimulationCreationRequestVO;
import com.onwelo.dice.api.vo.DiceSimulationResponseVO;

public interface DiceSimulationService {
    DiceSimulationResponseVO createDiceSimulation(DiceSimulationCreationRequestVO requestDTO);

    DiceDistributionSummaryResponseVO getDiceDistributionSummary();
}
