package bsu.edu.cs;

import bsu.edu.cs.controller.FuelComparisonController;
import bsu.edu.cs.controller.FuelComparisonControllerImpl;
import bsu.edu.cs.model.FuelCalculator;
import bsu.edu.cs.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class FuelComparisonApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FuelCalculator calculator = new FuelCalculator();
        FuelComparisonController controller = new FuelComparisonControllerImpl(calculator);

        MainView mainView = new MainView(controller);
        ScrollPane root = new ScrollPane(mainView);

        Scene scene = new Scene(root, root.getHeight(), root.getWidth());
        scene.getStylesheets().add("/styles.css");

        primaryStage.setTitle("Vehicle Fuel Economy Comparison");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}