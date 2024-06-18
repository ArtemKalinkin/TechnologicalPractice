package edu.epidemicsimulation.practice;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SimulationController {
    @FXML
    private Pane simulationPane;

    private Population population;

    public void initialize() {
        simulationPane.widthProperty().addListener((obs, oldVal, newVal) -> checkAndInitializePopulation());
        simulationPane.heightProperty().addListener((obs, oldVal, newVal) -> checkAndInitializePopulation());
    }

    private void checkAndInitializePopulation() {
        if (simulationPane.getWidth() > 0 && simulationPane.getHeight() > 0 && population == null) {
            population = new Population(100, simulationPane);
            population.infectRandomPerson();

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> updateSimulation()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    private void updateSimulation() {
        population.update();
    }
}
