package bsu.edu.cs;

import bsu.edu.cs.controller.FuelComparisonController;
import bsu.edu.cs.controller.FuelComparisonControllerImpl;
import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.view.MainView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class FuelComparisonApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FuelCalculator calculator = new FuelCalculator();
        FuelComparisonController controller = new FuelComparisonControllerImpl(calculator);

        MainView mainView = new MainView(controller);

        ScrollPane root = new ScrollPane(mainView);
        root.setFitToWidth(true);
        root.setFitToHeight(true);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double initialHeight = screenBounds.getHeight() * 0.8;

        Scene scene = new Scene(root, 800, initialHeight);
        scene.getStylesheets().add("/styles.css");

        primaryStage.setTitle("Vehicle Fuel Economy Comparison");
        primaryStage.setScene(scene);

        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        primaryStage.setMaximized(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}