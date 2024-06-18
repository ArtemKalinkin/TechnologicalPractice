package edu.epidemicsimulation.practice;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Person {
    private boolean infected;
    private boolean immune;
    private int daysInfected;
    private static final int INFECTION_PERIOD = 14;
    private Circle circle;

    public Person(double x, double y) {
        this.infected = false;
        this.immune = false;
        this.daysInfected = 0;
        this.circle = new Circle(x, y, 5, Color.GREEN);
    }

    public void infect() {
        if (!infected && !immune) {
            infected = true;
            daysInfected = 0;
            circle.setFill(Color.RED);
        }
    }

    public void update() {
        if (infected) {
            daysInfected++;
            if (daysInfected >= INFECTION_PERIOD) {
                infected = false;
                immune = true;
                circle.setFill(Color.BLUE);
            }
        }
    }

    public boolean isInfected() {
        return infected;
    }

    public boolean isImmune() {
        return immune;
    }

    public Circle getCircle() {
        return circle;
    }
}
