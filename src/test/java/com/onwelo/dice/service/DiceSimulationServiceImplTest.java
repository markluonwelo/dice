package com.onwelo.dice.service;

import com.onwelo.dice.api.dto.DiceDistributionSummaryDTO;
import com.onwelo.dice.api.vo.DiceDistributionSummaryResponseVO;
import com.onwelo.dice.api.dto.DiceRollRelativeDistributionDTO;
import com.onwelo.dice.api.vo.DiceSimulationCreationRequestVO;
import com.onwelo.dice.api.vo.DiceSimulationResponseVO;
import com.onwelo.dice.persistence.entity.DiceRollEntity;
import com.onwelo.dice.persistence.repository.DiceRollRepository;
import com.onwelo.dice.persistence.entity.DiceSimulationEntity;
import com.onwelo.dice.persistence.repository.DiceSimulationRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DiceSimulationServiceImplTest {

    @Mock
    DiceDistributionSimulationMapper diceDistributionSimulationMapper;

    @Mock
    DiceSimulationRepository diceSimulationRepository;

    @Mock
    DiceRollRepository diceRollRepository;

    @Mock
    DiceRollerService diceRollerService;

    @InjectMocks
    DiceSimulationServiceImpl underTest;

    @Test
    void createDiceSimulation() {
        long diceNumber = 3;
        long diceSidesNumber = 6;
        long rollsNumber = 5;

        DiceSimulationCreationRequestVO requestVO
            = new DiceSimulationCreationRequestVO(diceNumber, diceSidesNumber, rollsNumber);

        DiceSimulationEntity expectedSimulationEntity = DiceSimulationEntity.builder()
            .diceNumber(requestVO.getDiceNumber())
            .diceSidesNumber(requestVO.getDiceSidesNumber())
            .rollsNumber(requestVO.getRollsNumber())
            .build();
        DiceSimulationEntity savedDiceSimulation = Mockito.mock(DiceSimulationEntity.class);
        Mockito.when(diceSimulationRepository.save(Mockito.any(DiceSimulationEntity.class))).thenReturn(savedDiceSimulation);

        Mockito.when(diceRollerService.createRollingStream(requestVO)).thenReturn(LongStream.of(7L, 10L, 9L, 9L, 9L));

        Set<DiceRollEntity> expectedDiceRollEntities = Set.of(
            DiceRollEntity.builder().rollSum(9L).count(3L).diceSimulation(savedDiceSimulation).build(),
            DiceRollEntity.builder().rollSum(10L).count(1L).diceSimulation(savedDiceSimulation).build(),
            DiceRollEntity.builder().rollSum(7L).count(1L).diceSimulation(savedDiceSimulation).build()
        );

        DiceSimulationResponseVO expected = new DiceSimulationResponseVO(null);
        Mockito.when(diceDistributionSimulationMapper.mapToDiceSimulationResponseVO(expectedDiceRollEntities))
            .thenReturn(expected);

        DiceSimulationResponseVO actual = underTest.createDiceSimulation(requestVO);
        assertEquals(expected, actual);

        Mockito.verify(diceSimulationRepository).save(expectedSimulationEntity);
        Mockito.verify(diceRollRepository).saveAll(expectedDiceRollEntities);
    }

    @Test
    void getDiceDistributionSummary() {
        List<DiceSimulationEntity> simulations = Arrays.asList(
            DiceSimulationEntity.builder()
                .diceNumber(2L)
                .diceSidesNumber(4L)
                .rollsNumber(99L)
                .diceRolls(Arrays.asList(
                    DiceRollEntity.builder().count(6L).rollSum(2L).build(),
                    DiceRollEntity.builder().count(14L).rollSum(3L).build(),
                    DiceRollEntity.builder().count(20L).rollSum(4L).build(),
                    DiceRollEntity.builder().count(24L).rollSum(5L).build(),
                    DiceRollEntity.builder().count(23L).rollSum(6L).build(),
                    DiceRollEntity.builder().count(8L).rollSum(7L).build(),
                    DiceRollEntity.builder().count(4L).rollSum(8L).build()
                ))
                .build(),
            DiceSimulationEntity.builder()
                .diceNumber(2L)
                .diceSidesNumber(4L)
                .rollsNumber(99L)
                .diceRolls(Arrays.asList(
                    DiceRollEntity.builder().count(5L).rollSum(2L).build(),
                    DiceRollEntity.builder().count(12L).rollSum(3L).build(),
                    DiceRollEntity.builder().count(19L).rollSum(4L).build(),
                    DiceRollEntity.builder().count(23L).rollSum(5L).build(),
                    DiceRollEntity.builder().count(24L).rollSum(6L).build(),
                    DiceRollEntity.builder().count(10L).rollSum(7L).build(),
                    DiceRollEntity.builder().count(6L).rollSum(8L).build()
                ))
                .build(),
            DiceSimulationEntity.builder()
                .diceNumber(3L)
                .diceSidesNumber(6L)
                .rollsNumber(12L)
                .diceRolls(Arrays.asList(
                    DiceRollEntity.builder().count(1L).rollSum(3L).build(),
                    DiceRollEntity.builder().count(2L).rollSum(7L).build(),
                    DiceRollEntity.builder().count(1L).rollSum(8L).build(),
                    DiceRollEntity.builder().count(2L).rollSum(11L).build(),
                    DiceRollEntity.builder().count(1L).rollSum(13L).build(),
                    DiceRollEntity.builder().count(2L).rollSum(14L).build(),
                    DiceRollEntity.builder().count(3L).rollSum(15L).build()
                ))
                .build(),
            DiceSimulationEntity.builder()
                .diceNumber(2L)
                .diceSidesNumber(4L)
                .rollsNumber(4L)
                .diceRolls(Arrays.asList(
                    DiceRollEntity.builder().count(1L).rollSum(2L).build(),
                    DiceRollEntity.builder().count(1L).rollSum(8L).build(),
                    DiceRollEntity.builder().count(2L).rollSum(6L).build()
                ))
                .build()
        );
        Mockito.when(diceSimulationRepository.findDiceSimulationsFetchingDiceRolls()).thenReturn(simulations);

        Set<DiceDistributionSummaryDTO> expectedSimulationSummaries = Set.of(
            DiceDistributionSummaryDTO.builder()
                .diceNumber(2)
                .diceSidesNumber(4)
                .simulationsNumber(3)
                .totalRollsNumber(202)
                .distributions(Arrays.asList(
                    new DiceRollRelativeDistributionDTO(2, new BigDecimal("5.94")),
                    new DiceRollRelativeDistributionDTO(3, new BigDecimal("12.87")),
                    new DiceRollRelativeDistributionDTO(4, new BigDecimal("19.31")),
                    new DiceRollRelativeDistributionDTO(5, new BigDecimal("23.27")),
                    new DiceRollRelativeDistributionDTO(6, new BigDecimal("24.26")),
                    new DiceRollRelativeDistributionDTO(7, new BigDecimal("8.91")),
                    new DiceRollRelativeDistributionDTO(8, new BigDecimal("5.45"))
                ))
                .build(),
            DiceDistributionSummaryDTO.builder()
                .diceNumber(3)
                .diceSidesNumber(6)
                .simulationsNumber(1)
                .totalRollsNumber(12)
                .distributions(Arrays.asList(
                    new DiceRollRelativeDistributionDTO(3, new BigDecimal("8.33")),
                    new DiceRollRelativeDistributionDTO(7, new BigDecimal("16.67")),
                    new DiceRollRelativeDistributionDTO(8, new BigDecimal("8.33")),
                    new DiceRollRelativeDistributionDTO(11, new BigDecimal("16.67")),
                    new DiceRollRelativeDistributionDTO(13, new BigDecimal("8.33")),
                    new DiceRollRelativeDistributionDTO(14, new BigDecimal("16.67")),
                    new DiceRollRelativeDistributionDTO(15, new BigDecimal("25.00"))
                ))
                .build()
        );
        DiceDistributionSummaryResponseVO expected = new DiceDistributionSummaryResponseVO(expectedSimulationSummaries);

        DiceDistributionSummaryResponseVO actual = underTest.getDiceDistributionSummary();
        assertEquals(expected, actual);
    }
}