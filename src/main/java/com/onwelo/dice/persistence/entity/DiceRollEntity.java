package com.onwelo.dice.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "DICE_ROLL")
public class DiceRollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DICE_ROLL")
    @SequenceGenerator(name = "SEQ_DICE_ROLL", allocationSize = 20)
    private Long id;

    @Column(name = "ROLL_SUM", nullable = false)
    private Long rollSum;

    @Column(name = "COUNT", nullable = false)
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICE_SIMULATION_ID", nullable = false)
    private DiceSimulationEntity diceSimulation;
}
