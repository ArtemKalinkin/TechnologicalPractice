package edu.epidemicsimulation.practice;

import java.util.Random;

public class PopulationConfig {
    private int childrenPercentage;
    private int adultsPercentage;
    private int elderlyPercentage;
    private int mortalityRate = 30;
    private double infectionRate = 1.0;
    private int infectionDuration = 10;
    private int immunityDuration = 365;
    private int numberOfPeople = 500;
    private int incubationPeriod = 2;
    private String virusName;


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

    public void setVirusName(String virusName) {
        this.virusName = virusName;
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

    public String getVirusName() {
        return virusName;
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

    @Override
    public String toString() {
        return String.format("Children: %d%%, Adults: %d%%, Elderly: %d%%",
                childrenPercentage, adultsPercentage, elderlyPercentage);
    }
}
