package com.onwelo.dice.service;

import com.onwelo.dice.api.vo.DiceSimulationCreationRequestVO;
import java.util.stream.LongStream;

interface DiceRollerService {
    LongStream createRollingStream(DiceSimulationCreationRequestVO requestDTO);
}
