package com.onwelo.dice.persistence.repository;

import java.util.List;

import com.onwelo.dice.persistence.entity.DiceSimulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiceSimulationRepository extends JpaRepository<DiceSimulationEntity, Long> {

    @Query("SELECT DISTINCT dse FROM DiceSimulationEntity dse JOIN FETCH dse.diceRolls")
    List<DiceSimulationEntity> findDiceSimulationsFetchingDiceRolls();
}
