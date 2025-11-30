package com.bobatea.momandpops;

import com.bobatea.momandpops.backend.models.Item;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Mom & Pops GUI");
        primaryStage.setResizable(false);

        SceneManager.getInstance().initialize(primaryStage);
        SceneManager.getInstance().navigateTo("home-page.fxml");

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void stop(){
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}