package com.onwelo.dice.persistence.repository;

import com.onwelo.dice.persistence.entity.DiceRollEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiceRollRepository extends JpaRepository<DiceRollEntity, Long> {
}
