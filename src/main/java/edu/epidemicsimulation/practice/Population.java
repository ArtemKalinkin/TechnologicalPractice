package edu.epidemicsimulation.practice;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;

public class Population {
    private List<Person> people;

    public Population(int size, Pane pane) {
        people = new ArrayList<>(size);
        System.out.println("This is: "+pane.getWidth() + " " + pane.getHeight());
        for (int i = 0; i < size; i++) {
            double x = Math.random() * pane.getWidth();
            double y = Math.random() * pane.getHeight();
            System.out.println(x + " " + y);
            Person person = new Person(x, y);
            people.add(person);
            pane.getChildren().add(person.getCircle());
        }
    }

    public void infectRandomPerson() {
        int index = (int) (Math.random() * people.size());
        people.get(index).infect();

    }

    public void update() {
        for (Person person : people) {
            person.update();
        }
    }

    public int getInfectedCount() {
        int count = 0;
        for (Person person : people) {
            if (person.isInfected()) {
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
}
