package edu.epidemicsimulation.practice;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Random;

public abstract class Person {
    public static final double RADIUS = 5.0;
    private HealthStatus healthStatus;
    private int incubationDaysLeft;
    private int infectionDaysLeft;
    private int immuneDaysLeft;
    private Circle circle;
    protected double movementProbability;
    protected int moveDirection;
    private PopulationConfig populationConfig;
    private Random random;
    private int personNumber;
    private SimulationEventListener simulationEventListener;



    public Person(double movementProbability, PopulationConfig populationConfig, SimulationEventListener eventListener,
                  int personNumber) {
        this.healthStatus = HealthStatus.HEALTHY;
        this.incubationDaysLeft = 0;
        this.infectionDaysLeft = 0;
        this.immuneDaysLeft = 0;
        this.circle = new Circle(RADIUS, Color.GREEN);
        this.movementProbability = movementProbability;
        this.moveDirection = new Random().nextInt(8); // 8 возможных направлений
        this.random = new Random();
        this.populationConfig = populationConfig;
        this.personNumber = personNumber;
        this.simulationEventListener= eventListener;
    }

    public int getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(int personNumber) {
        this.personNumber = personNumber;
    }

    protected abstract double getInfectionPeriodCoefficient(); // Абстрактный метод
    protected abstract double getInfectionRateCoefficient(); // Абстрактный метод
    protected abstract double getMortalityRateCoefficient(); // Абстрактный метод
    protected abstract int getInfectionDurationCoefficient(); // Абстрактный метод

    private int getAdjustedInfectionPeriod() {
        return (int) (populationConfig.getInfectionDuration() * getInfectionPeriodCoefficient() * getInfectionDurationCoefficient());
    }
    private double getAdjustedMortalityRate() {
        return (populationConfig.getMortalityRate()  * getMortalityRateCoefficient());
    }
    private double getAdjustedInfectionRate() {
        return (populationConfig.getInfectionRate() * getInfectionRateCoefficient());
    }


    public void infect() {
        if (healthStatus == HealthStatus.HEALTHY) {
            healthStatus = HealthStatus.INCUBATING;
            incubationDaysLeft = populationConfig.getIncubationPeriod();
            circle.setFill(Color.YELLOW); // Цвет для инкубации

            if (simulationEventListener != null) {
                simulationEventListener.onInfection(this);
            }
        }
    }


    public void update() {
        updateHealthStatus();
    }

    private void updateHealthStatus() {
        if (incubationDaysLeft > 0) {
            incubationDaysLeft--;
        } else if (healthStatus == HealthStatus.INCUBATING) {
            healthStatus = HealthStatus.INFECTED;
            infectionDaysLeft = getAdjustedInfectionPeriod(); // Длительность болезни
            circle.setFill(Color.RED); // Цвет для инфекции
        }

        if (infectionDaysLeft > 0) {
            infectionDaysLeft--;
        } else if (healthStatus == HealthStatus.INFECTED) {
            if (random.nextInt(101) > getAdjustedMortalityRate()) {
                healthStatus = HealthStatus.IMMUNE;
                circle.setFill(Color.BLUE); // Цвет для иммунитета
                immuneDaysLeft = populationConfig.getImmunityDuration();
                if (simulationEventListener != null) {
                    simulationEventListener.onRecovery(this);
                }
            } else {
                healthStatus = HealthStatus.DEAD;
                circle.setFill(Color.BLACK); // Цвет для смерти
                if (simulationEventListener != null) {
                    simulationEventListener.onDeath(this);
                }
            }
        }

        if (immuneDaysLeft > 0){
            immuneDaysLeft--;
        } else if (healthStatus == HealthStatus.IMMUNE) {
            healthStatus = HealthStatus.HEALTHY;
            circle.setFill(Color.GREEN);
        }
    }


    public boolean isHealthy(){
        return healthStatus == HealthStatus.HEALTHY;
    }

    public boolean isInfected() {
        return healthStatus == HealthStatus.INFECTED;
    }

    public boolean isIncubating(){
        return healthStatus == HealthStatus.INCUBATING;
    }

    public boolean isImmune() {
        return healthStatus == HealthStatus.IMMUNE;
    }

    public boolean isDead(){
        return healthStatus == HealthStatus.DEAD;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setPosition(double x, double y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
    }


    public boolean shouldMove() {
        if (healthStatus == HealthStatus.DEAD)
            return false;
        return Math.random() < movementProbability;
    }


    public void move(int paneWidth, int paneHeight, List<Person> allPeople) {
        if (!shouldMove()) {
            return;
        }

        double newX = circle.getCenterX();
        double newY = circle.getCenterY();

        // Движение в текущем направлении
        switch (moveDirection) {
            case 0 -> newY--; // вверх
            case 1 -> { newY--; newX++; } // вверх-вправо
            case 2 -> newX++; // вправо
            case 3 -> { newY++; newX++; } // вниз-вправо
            case 4 -> newY++; // вниз
            case 5 -> { newY++; newX--; } // вниз-влево
            case 6 -> newX--; // влево
            case 7 -> { newY--; newX--; } // вверх-влево
        }

        // Проверка столкновений с другими людьми
        for (Person other : allPeople) {
            if (other != this && Math.abs(other.getCircle().getCenterX() - newX) < 5 && Math.abs(other.getCircle().getCenterY() - newY) < 5) {
                // Проверка и заражение других людей с учетом вероятности
                if (isInfected() && !other.isInfected() && !other.isImmune()) {
                    double infectionChance = getAdjustedInfectionRate();
                    if (random.nextDouble() < infectionChance) {
                        other.infect(); // Заражаем другого человека с учетом вероятности
                    }
                }

                // Также проверяем обратное столкновение
                if (!isInfected() && other.isInfected() && !isImmune()) {
                    double infectionChance = getAdjustedInfectionRate();
                    if (random.nextDouble() < infectionChance) {
                        infect(); // Текущий человек заражается
                    }
                }

                if (random.nextInt(5) == 0) { // 20% вероятность изменения направления
                    moveDirection = random.nextInt(8); // Случайное новое направление
                } else {
                    moveDirection = (moveDirection + 4) % 8; // Разворот на 180 градусов
                }
                break;
            }
        }

        // Проверка столкновений со стенками и отскоки
        if (newX < RADIUS) {
            newX = RADIUS;
            if (random.nextInt(5) == 0) { // 20% вероятность изменения направления
                moveDirection = random.nextInt(8); // Случайное новое направление
            } else {
                moveDirection = (moveDirection + 4) % 8; // Разворот на 180 градусов
            }
        } else if (newX > paneWidth - RADIUS) {
            newX = paneWidth - RADIUS;
            if (random.nextInt(5) == 0) { // 20% вероятность изменения направления
                moveDirection = random.nextInt(8); // Случайное новое направление
            } else {
                moveDirection = (moveDirection + 4) % 8; // Разворот на 180 градусов
            }
        }

        if (newY < RADIUS) {
            newY = RADIUS;
            if (random.nextInt(5) == 0) { // 20% вероятность изменения направления
                moveDirection = random.nextInt(8); // Случайное новое направление
            } else {
                moveDirection = (moveDirection + 4) % 8; // Разворот на 180 градусов
            }
        } else if (newY > paneHeight - RADIUS) {
            newY = paneHeight - RADIUS;
            if (random.nextInt(5) == 0) { // 20% вероятность изменения направления
                moveDirection = random.nextInt(8); // Случайное новое направление
            } else {
                moveDirection = (moveDirection + 4) % 8; // Разворот на 180 градусов
            }
        }

        // Обновление положения
        circle.setCenterX(newX);
        circle.setCenterY(newY);
    }
}
