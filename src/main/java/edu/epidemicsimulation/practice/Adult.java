package edu.epidemicsimulation.practice;

public class Adult extends Person{
    public Adult(PopulationConfig populationConfig, int number, SimulationEventListener eventListener) {
        super(0.99, populationConfig, eventListener, number);
    }

    @Override
    protected double getInfectionPeriodCoefficient() {
        return 1.0;
    }
}
