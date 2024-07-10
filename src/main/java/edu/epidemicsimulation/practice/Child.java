package edu.epidemicsimulation.practice;

public class Child extends Person{
    PopulationConfig config;
    public Child(PopulationConfig populationConfig, int number, SimulationEventListener eventListener) {
        super(0.2, populationConfig, eventListener, number);
        this.config = populationConfig;
    }

    @Override
    protected double getInfectionPeriodCoefficient() {
        return 0.7;
    }

    @Override
    protected double getInfectionRateCoefficient() {
        if (config.isAdultsIncreasedRisk())
            return 1.5;
        else
            return 1.0;
    }

    @Override
    protected double getMortalityRateCoefficient() {
        if (config.isAdultsIncreasedRisk())
            return 1.5;
        else
            return 1.0;
    }

    @Override
    protected int getInfectionDurationCoefficient() {
        if (config.isAdultsIncreasedRisk())
            return 2;
        else
            return 1;
    }
}
