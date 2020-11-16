package com.onwelo.dice.service;

import com.onwelo.dice.api.dto.DiceRollCountDTO;
import com.onwelo.dice.api.vo.DiceSimulationResponseVO;
import com.onwelo.dice.persistence.entity.DiceRollEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DiceDistributionSimulationMapperTest {

    private final DiceDistributionSimulationMapper underTest = new DiceDistributionSimulationMapper();

    @Test
    void mapToDiceDistributionResponseDTO() {
        Set<DiceRollEntity> diceRolls = Set.of(
            DiceRollEntity.builder().rollSum(12L).count(3L).build(),
            DiceRollEntity.builder().rollSum(13L).count(1L).build(),
            DiceRollEntity.builder().rollSum(4L).count(1L).build(),
            DiceRollEntity.builder().rollSum(7L).count(2L).build(),
            DiceRollEntity.builder().rollSum(2L).count(3L).build()
        );
        List<DiceRollCountDTO> expectedDiceRollCountDTOs = Arrays.asList(
            new DiceRollCountDTO(2L, 3L),
            new DiceRollCountDTO(4L, 1L),
            new DiceRollCountDTO(7L, 2L),
            new DiceRollCountDTO(12L, 3L),
            new DiceRollCountDTO(13L, 1L)
        );
        DiceSimulationResponseVO expected = new DiceSimulationResponseVO(expectedDiceRollCountDTOs);

        DiceSimulationResponseVO actual = underTest.mapToDiceSimulationResponseVO(diceRolls);
        assertEquals(expected, actual);
    }
}