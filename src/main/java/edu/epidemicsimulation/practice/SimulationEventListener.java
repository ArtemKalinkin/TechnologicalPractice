package edu.epidemicsimulation.practice;

public interface SimulationEventListener {
    void onInfection(Person person);
    void onRecovery(Person person);
    void onDeath(Person person);
}
