package com.onwelo.dice.service;

import com.onwelo.dice.api.dto.DiceDistributionSummaryDTO;
import com.onwelo.dice.api.dto.DiceRollRelativeDistributionDTO;
import com.onwelo.dice.api.vo.DiceDistributionSummaryResponseVO;
import com.onwelo.dice.api.vo.DiceSimulationCreationRequestVO;
import com.onwelo.dice.api.vo.DiceSimulationResponseVO;
import com.onwelo.dice.persistence.entity.DiceRollEntity;
import com.onwelo.dice.persistence.repository.DiceRollRepository;
import com.onwelo.dice.persistence.entity.DiceSimulationEntity;
import com.onwelo.dice.persistence.repository.DiceSimulationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
class DiceSimulationServiceImpl implements DiceSimulationService {

    private final DiceDistributionSimulationMapper diceDistributionSimulationMapper;
    private final DiceSimulationRepository diceSimulationRepository;
    private final DiceRollRepository diceRollRepository;
    private final DiceRollerService diceRollerService;

    private static final int HUNDRED_PERCENT = 100;
    private static final int ROLL_SUM_PERCENTAGE_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Transactional
    @Override
    public DiceSimulationResponseVO createDiceSimulation(DiceSimulationCreationRequestVO requestVO) {
        Map<Long, Long> diceRollSumCountMap = createDiceRollSumCountMap(requestVO);

        DiceSimulationEntity simulationEntity = DiceSimulationEntity.builder()
                .diceNumber(requestVO.getDiceNumber())
                .diceSidesNumber(requestVO.getDiceSidesNumber())
                .rollsNumber(requestVO.getRollsNumber())
                .build();
        DiceSimulationEntity savedDiceDistribution = diceSimulationRepository.save(simulationEntity);

        Set<DiceRollEntity> diceRollEntities = diceRollSumCountMap.entrySet().stream()
                .map(entry -> DiceRollEntity.builder()
                        .rollSum(entry.getKey())
                        .count(entry.getValue())
                        .diceSimulation(savedDiceDistribution)
                        .build())
                .collect(Collectors.toUnmodifiableSet());
        diceRollRepository.saveAll(diceRollEntities);

        return diceDistributionSimulationMapper.mapToDiceSimulationResponseVO(diceRollEntities);
    }

    @Override
    public DiceDistributionSummaryResponseVO getDiceDistributionSummary() {
        List<DiceSimulationEntity> simulations = diceSimulationRepository.findDiceSimulationsFetchingDiceRolls();
        Map<Map.Entry<Long, Long>, List<DiceSimulationEntity>> numberOfDiceAndSidesSimulationsMap
                = createNumberOfDiceAndSidesSimulationsMap(simulations);
        Set<DiceDistributionSummaryDTO> distributionSummaryDTOs = numberOfDiceAndSidesSimulationsMap.entrySet().stream()
                .map(this::createDiceDistributionSummaryDTO)
                .collect(Collectors.toUnmodifiableSet());
        return new DiceDistributionSummaryResponseVO(distributionSummaryDTOs);
    }

    private Map<Long, Long> createDiceRollSumCountMap(DiceSimulationCreationRequestVO requestVO) {
        return diceRollerService.createRollingStream(requestVO)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private Map<Map.Entry<Long, Long>, List<DiceSimulationEntity>> createNumberOfDiceAndSidesSimulationsMap(
            List<DiceSimulationEntity> simulations) {
        return simulations.stream()
                .map(simulation -> Map.entry(Map.entry(simulation.getDiceNumber(), simulation.getDiceSidesNumber()), simulation))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    private DiceDistributionSummaryDTO createDiceDistributionSummaryDTO(
            Map.Entry<Map.Entry<Long, Long>, List<DiceSimulationEntity>> entry) {

        Map.Entry<Long, Long> numberOfDiceAndSides = entry.getKey();
        List<DiceSimulationEntity> simulations = entry.getValue();

        long diceNumber = numberOfDiceAndSides.getKey();
        long diceSidesNumber = numberOfDiceAndSides.getValue();
        long simulationsNumber = simulations.size();
        long totalRollsNumber = simulations.stream()
                .map(DiceSimulationEntity::getRollsNumber)
                .reduce(0L, Long::sum);
        List<DiceRollRelativeDistributionDTO> distributions = createDistributionDTOs(simulations, totalRollsNumber);

        return DiceDistributionSummaryDTO.builder()
                .diceNumber(diceNumber)
                .diceSidesNumber(diceSidesNumber)
                .simulationsNumber(simulationsNumber)
                .totalRollsNumber(totalRollsNumber)
                .distributions(distributions)
                .build();
    }

    private List<DiceRollRelativeDistributionDTO> createDistributionDTOs(List<DiceSimulationEntity> simulations, long totalRollsNumber) {
        Map<Long, Long> rollSumRollCountMap = createRollSumRollCountMap(simulations);

        return rollSumRollCountMap.entrySet().stream()
                .map(rollSumRollCountEntry -> createDiceRollRelativeDistributionDTO(totalRollsNumber, rollSumRollCountEntry))
                .sorted(Comparator.comparingLong(DiceRollRelativeDistributionDTO::getRollSum))
                .collect(Collectors.toUnmodifiableList());
    }

    private Map<Long, Long> createRollSumRollCountMap(List<DiceSimulationEntity> simulations) {
        return simulations.stream()
                .map(DiceSimulationEntity::getDiceRolls)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(DiceRollEntity::getRollSum, DiceRollEntity::getCount, Long::sum));
    }

    private DiceRollRelativeDistributionDTO createDiceRollRelativeDistributionDTO(long totalRollsNumber, Map.Entry<Long, Long> rollSumRollCountEntry) {
        Long rollSum = rollSumRollCountEntry.getKey();
        Long rollCount = rollSumRollCountEntry.getValue();
        BigDecimal relativeDistributionPercentage = computeRollCountPercentageOfTotalRollsNumber(rollCount, totalRollsNumber);
        return new DiceRollRelativeDistributionDTO(rollSum, relativeDistributionPercentage);
    }

    private BigDecimal computeRollCountPercentageOfTotalRollsNumber(long rollCount, long totalRollsNumber) {
        return new BigDecimal(rollCount)
                .multiply(new BigDecimal(HUNDRED_PERCENT))
                .divide(new BigDecimal(totalRollsNumber), ROLL_SUM_PERCENTAGE_SCALE, ROUNDING_MODE);
    }
}
