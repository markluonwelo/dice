package com.onwelo.dice.service;

import com.onwelo.dice.api.dto.DiceRollCountDTO;
import com.onwelo.dice.api.vo.DiceSimulationResponseVO;
import com.onwelo.dice.persistence.entity.DiceRollEntity;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
class DiceDistributionSimulationMapper {

    DiceSimulationResponseVO mapToDiceSimulationResponseVO(Set<DiceRollEntity> diceRolls) {
        List<DiceRollCountDTO> diceRollCountDTOS = diceRolls.stream()
            .map(this::mapToDiceRollVO)
            .sorted(Comparator.comparing(DiceRollCountDTO::getRollSum))
            .collect(Collectors.toUnmodifiableList());
        return new DiceSimulationResponseVO(diceRollCountDTOS);
    }

    private DiceRollCountDTO mapToDiceRollVO(DiceRollEntity entity) {
        return new DiceRollCountDTO(entity.getRollSum(), entity.getCount());
    }
}
