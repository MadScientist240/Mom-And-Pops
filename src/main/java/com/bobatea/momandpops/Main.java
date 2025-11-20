package com.bobatea.momandpops;

import com.bobatea.momandpops.frontend.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mom & Pops GUI");
        primaryStage.setResizable(false);

        SceneManager.getInstance().initialize(primaryStage);
        SceneManager.getInstance().navigateTo("home-page.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}