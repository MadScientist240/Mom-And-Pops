package com.bobatea.momandpops.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager{
    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager(){}

    public static SceneManager getInstance(){
        if(instance == null){
            instance = new SceneManager();
        }
        return instance;
    }

    public void initialize(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void navigateTo(String fxmlName){
        try{
            String path = "/com/bobatea/momandpops/views/" + fxmlName;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/com/bobatea/momandpops/styles/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            System.out.println("Failed to load scene: " + fxmlName + "\nWas a matching fxml created?");
        }
    }
}