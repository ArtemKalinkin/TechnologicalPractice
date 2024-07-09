package edu.epidemicsimulation.practice;

public class Child extends Person{
    public Child(PopulationConfig populationConfig, int number, SimulationEventListener eventListener) {
        super(0.2, populationConfig, eventListener, number);
    }

    @Override
    protected double getInfectionPeriodCoefficient() {
        return 0.7;
    }
}
