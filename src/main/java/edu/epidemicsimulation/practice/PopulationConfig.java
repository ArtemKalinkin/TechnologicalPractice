package edu.epidemicsimulation.practice;

import java.util.Random;

public class PopulationConfig {
    private int childrenPercentage;
    private int adultsPercentage;
    private int elderlyPercentage;
    private int mortalityRate = 3;
    private double infectionRate = 0.5;
    private int infectionDuration = 10;
    private int immunityDuration = 365;
    private int numberOfPeople = 500;
    private int incubationPeriod = 2;
    private boolean childrenIncreasedRisk;
    private boolean adultsIncreasedRisk;
    private boolean elderlyIncreasedRisk;


    public void setIncubationPeriod(int incubationPeriod) {
        this.incubationPeriod = incubationPeriod;
    }

    public int getIncubationPeriod() {
        return incubationPeriod;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setMortalityRate(int mortalityRate) {
        this.mortalityRate = mortalityRate;
    }

    public void setInfectionRate(double infectionRate) {
        this.infectionRate = infectionRate;
    }

    public void setInfectionDuration(int infectionDuration) {
        this.infectionDuration = infectionDuration;
    }

    public void setImmunityDuration(int immunityDuration) {
        this.immunityDuration = immunityDuration;
    }


    public double getMortalityRate() {
        return mortalityRate;
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public int getInfectionDuration() {
        return infectionDuration;
    }

    public int getImmunityDuration() {
        return immunityDuration;
    }


    public PopulationConfig() {
        setRandomAgeGroupPercentages();
    }

    private void setRandomAgeGroupPercentages() {
        Random rand = new Random();
        childrenPercentage = rand.nextInt(11) + 10;
        adultsPercentage = rand.nextInt(11) + 20;
        elderlyPercentage = 100 - childrenPercentage - adultsPercentage;
    }

    public int getChildrenPercentage() {
        return childrenPercentage;
    }

    public int getAdultsPercentage() {
        return adultsPercentage;
    }

    public int getElderlyPercentage() {
        return elderlyPercentage;
    }

    public boolean isChildrenIncreasedRisk() {
        return childrenIncreasedRisk;
    }

    public void setChildrenIncreasedRisk(boolean childrenIncreasedRisk) {
        this.childrenIncreasedRisk = childrenIncreasedRisk;
    }

    public boolean isAdultsIncreasedRisk() {
        return adultsIncreasedRisk;
    }

    public void setAdultsIncreasedRisk(boolean adultsIncreasedRisk) {
        this.adultsIncreasedRisk = adultsIncreasedRisk;
    }

    public boolean isElderlyIncreasedRisk() {
        return elderlyIncreasedRisk;
    }

    public void setElderlyIncreasedRisk(boolean elderlyIncreasedRisk) {
        this.elderlyIncreasedRisk = elderlyIncreasedRisk;
    }

    @Override
    public String toString() {
        return String.format("Дети: %d%%, Взрослые: %d%%, Пожилые: %d%%\n",
                childrenPercentage, adultsPercentage, elderlyPercentage);
    }
}
