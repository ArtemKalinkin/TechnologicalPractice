package edu.epidemicsimulation.practice;

public class Elderly extends Person{
    public Elderly(PopulationConfig populationConfig, int number, SimulationEventListener eventListener) {
        super(0.1, populationConfig, eventListener, number);
    }

    @Override
    protected double getInfectionPeriodCoefficient() {
        return 1.5;
    }
}
