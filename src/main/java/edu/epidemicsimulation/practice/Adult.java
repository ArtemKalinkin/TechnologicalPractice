package edu.epidemicsimulation.practice;

public class Adult extends Person{
    PopulationConfig config;
    public Adult(PopulationConfig populationConfig, int number, SimulationEventListener eventListener) {
        super(0.99, populationConfig, eventListener, number);
        this.config = populationConfig;
    }

    @Override
    protected double getInfectionPeriodCoefficient() {
        return 1.0;
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
