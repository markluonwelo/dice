package com.onwelo.dice.api.controller;

import com.onwelo.dice.api.vo.DiceDistributionSummaryResponseVO;
import com.onwelo.dice.api.vo.DiceSimulationCreationRequestVO;
import com.onwelo.dice.api.vo.DiceSimulationResponseVO;
import com.onwelo.dice.service.DiceSimulationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("dice/simulations")
@RestController
class DiceDistributionSimulationController {

    private final DiceSimulationService diceSimulationService;

    @PostMapping
    public DiceSimulationResponseVO createDiceSimulation(@Valid @RequestBody DiceSimulationCreationRequestVO requestDTO) {
        return diceSimulationService.createDiceSimulation(requestDTO);
    }

    @GetMapping
    public DiceDistributionSummaryResponseVO getDiceDistributionsSummary() {
        return diceSimulationService.getDiceDistributionSummary();
    }
}
