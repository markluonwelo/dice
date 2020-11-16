package com.onwelo.dice.service;

import com.onwelo.dice.api.vo.DiceSimulationCreationRequestVO;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;
import org.springframework.stereotype.Service;

@Service
class DiceRollerServiceImpl implements DiceRollerService {

    private static final long MIN_SINGLE_DICE_ROLL_RESULT = 1L;

    public LongStream createRollingStream(DiceSimulationCreationRequestVO requestDTO) {
        final long streamSize = requestDTO.getRollsNumber();
        final long diceNumber = requestDTO.getDiceNumber();
        final long diceSidesNumber = requestDTO.getDiceSidesNumber();
        return LongStream.generate(
            () -> {
                int roll = 0;
                for (int i = 0; i < diceNumber; i++) {
                    roll += ThreadLocalRandom.current().nextLong(MIN_SINGLE_DICE_ROLL_RESULT, diceSidesNumber);
                }
                return roll;
            })
            .limit(streamSize)
            .parallel();
    }
}
