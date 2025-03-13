package bsu.edu.cs;

import bsu.edu.cs.view.MainView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class FuelComparisonApp extends Application {
    @Override
    public void start(Stage primaryStage){
        MainView mainView = new MainView();

        //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainView);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        Scene scene = new Scene(scrollPane, 600,800);

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
