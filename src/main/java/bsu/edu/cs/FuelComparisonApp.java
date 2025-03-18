package bsu.edu.cs;

import bsu.edu.cs.view.MainView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;


public class FuelComparisonApp extends Application {
    @Override
    public void start(Stage primaryStage){
        MainView mainView = new MainView();

        BorderPane borderPane = new BorderPane();



        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainView);

        Scene scene = new Scene(scrollPane, 800,800);

        scene.getStylesheets().add("styles.css");

        primaryStage.setResizable(true);



        primaryStage.setTitle("Vehicle Fuel Economy Comparison");

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
}
