package edu.epidemicsimulation.practice;

import java.util.ArrayList;
import java.util.List;


import javafx.scene.layout.Pane;

public class Population {
    private List<Person> people;
    private Pane simulationPane;



    public Population(int size, Pane pane, PopulationConfig populationConfig, SimulationEventListener eventListener) {
        this.simulationPane = pane;
        people = new ArrayList<>(size);

        int childrenCount = size * populationConfig.getChildrenPercentage() / 100;
        int adultsCount = size * populationConfig.getAdultsPercentage() / 100;
        int elderlyCount = size * populationConfig.getElderlyPercentage() / 100;

        for (int i = 0; i < childrenCount; i++) {
            addPerson(new Child(populationConfig, i, eventListener), pane);
        }
        for (int i = 0; i < adultsCount; i++) {
            addPerson(new Adult(populationConfig, i, eventListener), pane);
        }
        for (int i = 0; i < elderlyCount; i++) {
            addPerson(new Elderly(populationConfig, i, eventListener), pane);
        }

    }

    public List<Person> getPeople() {
        return people;
    }

    private void addPerson(Person person, Pane pane) {
        double x, y;
        boolean overlap;
        do {
            overlap = false;
            x = Math.random() * (pane.getWidth() - 2 * Person.RADIUS) + Person.RADIUS;
            y = Math.random() * (pane.getHeight() - 2 * Person.RADIUS) + Person.RADIUS;

            for (Person p : people) {
                double distance = Math.sqrt(Math.pow(x - p.getCircle().getCenterX(), 2)
                        + Math.pow(y - p.getCircle().getCenterY(), 2));
                if (distance < 2 * Person.RADIUS) {
                    overlap = true;
                    break;
                }
            }
        } while (overlap);

        person.setPosition(x, y);
        people.add(person);
        pane.getChildren().add(person.getCircle());
    }


    public void infectRandomPerson() {
        int index = (int) (Math.random() * people.size());
        people.get(index).infect();

    }

    public void update() {
        for (Person person : people) {
            person.move((int) simulationPane.getWidth(), (int) simulationPane.getHeight(), people);
        }
    }

    public int getHealthyCount(){
        int count = 0;
        for (Person person : people) {
            if (person.isHealthy()) {
                count++;
            }
        }
        return count;
    }

    public int getInfectedCount() {
        int count = 0;
        for (Person person : people) {
            if (person.isInfected() | person.isIncubating()) {
                count++;
            }
        }
        return count;
    }

    public int getImmuneCount() {
        int count = 0;
        for (Person person : people) {
            if (person.isImmune()) {
                count++;
            }
        }
        return count;
    }

    public int getDeadCount(){
        int count = 0;
        for (Person person : people) {
            if (person.isDead()) {
                count++;
            }
        }
        return count;
    }


    public void updateDaily() {
        for (Person person : people) {
            person.update();
        }
    }
}