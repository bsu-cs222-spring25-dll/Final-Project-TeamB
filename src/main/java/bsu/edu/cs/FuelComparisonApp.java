package bsu.edu.cs;

import bsu.edu.cs.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
//import javafx.scene.web.WebEngine;
//import javafx.scene.web.WebView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FuelComparisonApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        MainView mainView = new MainView();

        //WebView browser = new WebView();
        //WebEngine webEngine = browser.getEngine();
        //webEngine.load();
        //webEngine.load(getClass().getResource("src/main/resources/test.html").toExternalForm());
        BorderPane root = new BorderPane();
        root.setCenter(new ScrollPane(mainView));
        //root.setBottom(browser);
        //browser.setPrefHeight(300);

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