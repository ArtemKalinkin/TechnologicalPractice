package edu.epidemicsimulation.practice;

import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SimulationController{

    @FXML
    private HBox BottomHBox;

    @FXML
    private CheckBox adultsCheckBox;

    @FXML
    private CheckBox childrenCheckBox;

    @FXML
    private Label consoleLabel;

    @FXML
    private TextArea consoleTextArea;

    @FXML
    private VBox consoleVBox;

    @FXML
    private Pane controlPane;

    @FXML
    private Label deceasedCountLabel;

    @FXML
    private Label deceasedLabel;

    @FXML
    private Label elapsedTimeLabel;

    @FXML
    private CheckBox elderlyCheckBox;

    @FXML
    private Label healthyCountLabel;

    @FXML
    private Label healthyLabel;

    @FXML
    private Label immunityDurationLabel;

    @FXML
    private Slider immunityDurationSlider;

    @FXML
    private Label infectedCountLabel;

    @FXML
    private Label infectedLabel;

    @FXML
    private Label infectionDurationLabel;

    @FXML
    private Slider infectionDurationSlider;

    @FXML
    private Label infectionRateLabel;

    @FXML
    private Slider infectionRateSlider;

    @FXML
    private GridPane infoGrid;

    @FXML
    private Label infoLabel;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label mortalityRateLabel;

    @FXML
    private Slider mortalityRateSlider;

    @FXML
    private Label numberImmunityDurationLabel;

    @FXML
    private Label numberInfectionDurationLabel;

    @FXML
    private Label numberInfectionRateLabel;

    @FXML
    private Label numberMortalityRateLabel;

    @FXML
    private Label numberPeopleLabel;

    @FXML
    private Label paramsLabel;

    @FXML
    private Pane paramsPane;

    @FXML
    private Label peopleLabel;

    @FXML
    private Slider peopleSlider;

    @FXML
    private Label recoveredCountLabel;

    @FXML
    private Label recoveredLabel;

    @FXML
    private Button resetButton;

    @FXML
    private VBox sidebar;

    @FXML
    private Pane simulationPane;

    @FXML
    private StackedAreaChart<Number, Number> stackedAreaChart;

    @FXML
    private Button startButton;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Button stopButton;

    @FXML
    private Label totalPeopleCountLabel;

    @FXML
    private Label totalPeopleLabel;

    @FXML
    private Label virusNameLabel;

    @FXML
    private TextField virusNameTextField;

    @FXML
    private CheckBox vulnerableAgeGroupsCheckBox;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;



    private Population population;
    private PopulationConfig populationConfig;
    private Timeline simulationTimeline;
    private Timeline elapsedTimeTimeline;
    private Timeline dayTimeline;
    private LocalTime startTime;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private boolean isRunning;
    private long elapsedSeconds = 0;
    private LocalTime lastUpdateTime;
    private int daysElapsed = 0;

    private SimulationEventListener eventListener = new SimulationEventListener() {
        @Override
        public void onInfection(Person person) {
            consoleTextArea.appendText(Utils.whoIs(person) + " " + person.getPersonNumber() + " заражен.\n");
        }

        @Override
        public void onRecovery(Person person) {
            consoleTextArea.appendText(Utils.whoIs(person) + " " + person.getPersonNumber() + " выздоровел.\n");
        }

        @Override
        public void onDeath(Person person) {
            consoleTextArea.appendText(Utils.whoIs(person) + " " + person.getPersonNumber() + " умер.\n");
        }
    };

    private XYChart.Series<Number, Number> infectedSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> recoveredSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> deceasedSeries = new XYChart.Series<>();
    private int timeElapsed = 0;


    @FXML
    public void initialize() {
        populationConfig = new PopulationConfig();


        simulationPane.widthProperty().addListener((obs, oldVal, newVal) -> checkAndInitializePopulation());
        simulationPane.heightProperty().addListener((obs, oldVal, newVal) -> checkAndInitializePopulation());

        // Установка начальных значений ползунков
        mortalityRateSlider.setValue(populationConfig.getMortalityRate());
        infectionRateSlider.setValue(populationConfig.getInfectionRate());
        infectionDurationSlider.setValue(populationConfig.getInfectionDuration());
        immunityDurationSlider.setValue(populationConfig.getImmunityDuration());
        peopleSlider.setValue(populationConfig.getNumberOfPeople());

        // Отображение начальных значений ползунков
        numberMortalityRateLabel.setText(String.format("%d %%", (int) mortalityRateSlider.getValue()));
        numberInfectionRateLabel.setText(String.format("%.2f", infectionRateSlider.getValue()));
        numberInfectionDurationLabel.setText(String.format("%d ", (int) infectionDurationSlider.getValue())
                + Utils.getDayAddition((int) infectionDurationSlider.getValue()));
        numberImmunityDurationLabel.setText(String.format("%d ", (int) immunityDurationSlider.getValue())
                + Utils.getDayAddition((int) immunityDurationSlider.getValue()));
        numberPeopleLabel.setText(String.format("%d", (int)peopleSlider.getValue()));

        // Отображение начальных данных таблицы общей информации
        totalPeopleCountLabel.setText(String.format("%d", (int) peopleSlider.getValue()));


        // Добавление обработчиков событий для ползунков
        mortalityRateSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            populationConfig.setMortalityRate(newVal.intValue());
            numberMortalityRateLabel.setText(String.format("%d", newVal.intValue()) + " %");
        });

        infectionRateSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            populationConfig.setInfectionRate(newVal.doubleValue());
            numberInfectionRateLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });

        infectionDurationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            populationConfig.setInfectionDuration(newVal.intValue());
            numberInfectionDurationLabel.setText(String.format("%d ", newVal.intValue())
                    + Utils.getDayAddition(newVal.intValue()));
        });

        immunityDurationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            populationConfig.setImmunityDuration(newVal.intValue());
            numberImmunityDurationLabel.setText(String.format("%d ", newVal.intValue())
                    + Utils.getDayAddition(newVal.intValue()));
        });

        peopleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            simulationPane.getChildren().clear();
            population = null;
            populationConfig.setNumberOfPeople(newVal.intValue());
            checkAndInitializePopulation();
            numberPeopleLabel.setText(String.format("%d", newVal.intValue()));
            totalPeopleCountLabel.setText(String.format("%d", newVal.intValue()));
        });


        childrenCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                populationConfig.setChildrenIncreasedRisk(true);
            } else {
                populationConfig.setChildrenIncreasedRisk(false);
            }
        });

        adultsCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                populationConfig.setAdultsIncreasedRisk(true);
            } else {
                populationConfig.setAdultsIncreasedRisk(false);
            }
        });

        elderlyCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                populationConfig.setElderlyIncreasedRisk(true);
            } else {
                populationConfig.setElderlyIncreasedRisk(false);
            }
        });

        vulnerableAgeGroupsCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            childrenCheckBox.setDisable(!newVal);
            adultsCheckBox.setDisable(!newVal);
            elderlyCheckBox.setDisable(!newVal);

            if (!newVal) {
                childrenCheckBox.setSelected(false);
                adultsCheckBox.setSelected(false);
                elderlyCheckBox.setSelected(false);
            }
        });

        // Добавление обработчиков событий для кнопок
        startButton.setOnAction(event -> startSimulation());
        stopButton.setOnAction(event -> toggleSimulation());
        resetButton.setOnAction(event -> resetSimulation());

        // Инициализация таймера для обновления времени
        elapsedTimeTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateElapsedTime()));
        elapsedTimeTimeline.setCycleCount(Timeline.INDEFINITE);

        // Таймер для обновления дня
        dayTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> updateDay())); // 3 секунды для каждого дня
        dayTimeline.setCycleCount(Timeline.INDEFINITE);

        // Инициализация кнопок
        stopButton.setDisable(true); // Стоп-кнопка неактивна при запуске
        isRunning = false;

        // Инициализация серий графика
        infectedSeries.setName("Зараженные");
        recoveredSeries.setName("Выздоровевшие");
        deceasedSeries.setName("Умершие");

        // Добавление серий в график
        stackedAreaChart.getData().addAll(infectedSeries, recoveredSeries, deceasedSeries);
    }


    private void checkAndInitializePopulation() {
        double width = simulationPane.getWidth();
        double height = simulationPane.getHeight();

        if (width > 0 && height > 0 && population == null) {
            int numberOfPeople = populationConfig.getNumberOfPeople();
            population = new Population(numberOfPeople, simulationPane, populationConfig, eventListener);
        }
    }



    private void startSimulation() {
        consoleTextArea.appendText(populationConfig.toString());
        consoleTextArea.appendText("День 0:\n");
        // Создание и запуск таймера для движения
        simulationTimeline = new Timeline(new KeyFrame(Duration.millis(15), e -> updateSimulation()));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();

        // Запуск таймеров для обновления времени и дня
        elapsedTimeTimeline.play();
        dayTimeline.play();

        peopleSlider.setDisable(true); // Слайдер количевства людей становится неактивным
        startButton.setDisable(true); // Кнопка "Начать" становится неактивной
        stopButton.setDisable(false); // Кнопка "Стоп" становится активной
        isRunning = true;
        population.infectRandomPerson();

        startTime = LocalTime.now();
        startTimeLabel.setText("Время начала: " + startTime.format(timeFormatter));

        healthyCountLabel.setText("0");
        infectedCountLabel.setText("0");
        recoveredCountLabel.setText("0");
        deceasedCountLabel.setText("0");

    }

    private void toggleSimulation() {
        if (isRunning) {
            lastUpdateTime = LocalTime.now();
            simulationTimeline.pause();
            elapsedTimeTimeline.pause();
            dayTimeline.pause();
            stopButton.setText("Продолжить");
        } else {
            simulationTimeline.play();
            elapsedTimeTimeline.play();
            dayTimeline.play();
            stopButton.setText("Стоп");
            LocalTime now = LocalTime.now();
            long pauseDuration = ChronoUnit.SECONDS.between(lastUpdateTime, now);
            elapsedSeconds += pauseDuration;
        }
        isRunning = !isRunning;
    }


    private void resetSimulation() {
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
        if (elapsedTimeTimeline != null) {
            elapsedTimeTimeline.stop();
        }
        if (dayTimeline != null) {
            dayTimeline.stop();
        }
        startTimeLabel.setText("Время начала: --:--:--");
        elapsedTimeLabel.setText("Прошедшее время: 00:00:00");
        consoleTextArea.clear();
        population = null;
        daysElapsed = 0;
        elapsedSeconds = 0;
        simulationPane.getChildren().clear();
        checkAndInitializePopulation();
        peopleSlider.setDisable(false); // Слайдер количевства людей становится активным
        startButton.setDisable(false); // Кнопка "Начать" становится активной
        stopButton.setDisable(true); // Кнопка "Стоп" становится неактивной
        stopButton.setText("Стоп"); // Восстанавливаем текст кнопки "Стоп"
        isRunning = false;
        healthyCountLabel.setText("-");
        infectedCountLabel.setText("-");
        recoveredCountLabel.setText("-");
        deceasedCountLabel.setText("-");

        // Сброс графика
        infectedSeries.getData().clear();
        recoveredSeries.getData().clear();
        deceasedSeries.getData().clear();

        xAxis.setLowerBound(0);
        xAxis.setUpperBound(100);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
    }

    private void pauseSimulation() {
        // Останавливаем таймеры
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
        if (elapsedTimeTimeline != null) {
            elapsedTimeTimeline.stop();
        }
        if (dayTimeline != null) {
            dayTimeline.stop();
        }

        // Деактивируем кнопки, кроме кнопки сброса
        startButton.setDisable(true);
        stopButton.setDisable(true);

        consoleTextArea.appendText("\nСимуляция завершена!");
    }

    private void updateElapsedTime() {
        if (startTime != null) {
            LocalTime now = LocalTime.now();
            long durationSinceStart = ChronoUnit.SECONDS.between(startTime, now);
            long effectiveElapsedSeconds = durationSinceStart - elapsedSeconds;
            long hours = effectiveElapsedSeconds / 3600;
            long minutes = (effectiveElapsedSeconds % 3600) / 60;
            long seconds = effectiveElapsedSeconds % 60;
            elapsedTimeLabel.setText(String.format("Прошедшее время: %02d:%02d:%02d (Дни: %d)", hours, minutes, seconds, daysElapsed));

            // Обновление статистики
            updateStatistics();

            // Проверяем, есть ли еще зараженные люди
            if (population.getInfectedCount() == 0) {
                pauseSimulation();
            }
        }
    }


    private void updateSimulation() {
        population.update();
    }

    private void updateDay() {
        // Логика обновления состояния за день
        daysElapsed++;
        consoleTextArea.appendText("\nДень " + daysElapsed + ":\n");
        population.updateDaily(); // Вызов метода обновления дневного состояния популяции
    }

    public void updateStatistics() {
        int healthyCount = population.getHealthyCount();
        int infectedCount = population.getInfectedCount();
        int recoveredCount = population.getImmuneCount();
        int deadCount = population.getDeadCount();

        healthyCountLabel.setText(String.valueOf(healthyCount));
        infectedCountLabel.setText(String.valueOf(infectedCount));
        recoveredCountLabel.setText(String.valueOf(recoveredCount));
        deceasedCountLabel.setText(String.valueOf(deadCount));

        // Обновление графика
        timeElapsed++;
        infectedSeries.getData().add(new XYChart.Data<>(timeElapsed, infectedCount));
        recoveredSeries.getData().add(new XYChart.Data<>(timeElapsed, recoveredCount));
        deceasedSeries.getData().add(new XYChart.Data<>(timeElapsed, deadCount));
    }
}
