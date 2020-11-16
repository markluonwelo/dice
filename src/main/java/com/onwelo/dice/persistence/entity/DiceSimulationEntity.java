package com.onwelo.dice.persistence.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DICE_SIMULATION")
public class DiceSimulationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DICE_SIMULATION")
    @SequenceGenerator(name = "SEQ_DICE_SIMULATION", allocationSize = 1)
    private Long id;

    @Column(name = "DICE_NUMBER", nullable = false)
    private Long diceNumber;

    @Column(name = "DICE_SIDES_NUMBER", nullable = false)
    private Long diceSidesNumber;

    @Column(name = "ROLLS_NUMBER", nullable = false)
    private Long rollsNumber;

    @OneToMany(mappedBy = "diceSimulation")
    private List<DiceRollEntity> diceRolls;
}
