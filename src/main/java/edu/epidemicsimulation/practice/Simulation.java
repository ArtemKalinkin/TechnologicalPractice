package edu.epidemicsimulation.practice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Simulation extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Получаем первый экран (монитор) в системе
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds(); // Получаем размеры экрана

            FXMLLoader loader = new FXMLLoader(getClass().getResource("simulation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Epidemic Simulation");

            // Устанавливаем размеры окна приложения равными размерам экрана
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());

            // Максимизируем окно (без декораций)
            primaryStage.setMaximized(true);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
