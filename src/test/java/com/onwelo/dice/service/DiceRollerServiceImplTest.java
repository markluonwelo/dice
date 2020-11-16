package com.onwelo.dice.service;

import com.onwelo.dice.api.vo.DiceSimulationCreationRequestVO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

@ExtendWith(MockitoExtension.class)
class DiceRollerServiceImplTest {

    private static final long DICE_NUMBER = 3L;
    private final DiceRollerServiceImpl underTest = new DiceRollerServiceImpl();

    @ParameterizedTest
    @ValueSource(longs = {7L, 3L, 5L, 2L, 15L, 10L})
    void rollDice(long diceSidesNumber) {
        DiceSimulationCreationRequestVO requestDTO
            = new DiceSimulationCreationRequestVO(DICE_NUMBER, diceSidesNumber, 100L);
        underTest.createRollingStream(requestDTO).forEach(actual -> {
            assertThat(actual, lessThanOrEqualTo(diceSidesNumber * DICE_NUMBER));
            assertThat(actual, greaterThanOrEqualTo(DICE_NUMBER));
        });
    }
}