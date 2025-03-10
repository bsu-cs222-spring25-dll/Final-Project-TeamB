package bsu.edu.cs;

import bsu.edu.cs.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class FuelComparisonApp extends Application {
    @Override
    public void start(Stage primaryStage){
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView,800, 800);

        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());

        primaryStage.setTitle("Vehicle Fuel Economy Comparison");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
