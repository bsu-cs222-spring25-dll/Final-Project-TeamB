package bsu.edu.cs;

import bsu.edu.cs.view.MainView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class FuelComparisonApp extends Application {
    @Override
    public void start(Stage primaryStage){
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView);

        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        primaryStage.setResizable(true);

        primaryStage.setTitle("Vehicle Fuel Economy Comparison");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
