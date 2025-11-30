package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LogoutController {

    @FXML private ImageView spinningImage;

    @FXML private Label timerText;

    public void initialize(){
        UserSession.getInstance().logout();
        startLogoutTimer();
        startLoadingAnimation();
    }

    private void startLogoutTimer(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int s = 5; s > 0; s--){
                    final int count = s;

                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        timerText.setText("Redirecting to home page in " + count + " seconds...");
                    });
                    Thread.sleep(1000);
                }

                return null;
            }
        };

        task.setOnSucceeded(e -> {
            SceneManager.getInstance().navigateTo("home-page.fxml");
        });

        new Thread(task).start();
    }

    @FXML
    private void startLoadingAnimation(){
        RotateTransition rotate = new RotateTransition(Duration.seconds(1.5), spinningImage);
        rotate.setByAngle(360);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.play();
    }

}
