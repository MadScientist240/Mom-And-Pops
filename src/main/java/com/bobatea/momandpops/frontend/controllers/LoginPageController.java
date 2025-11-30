package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.Customer;
import com.bobatea.momandpops.backend.models.DatabaseManager;
import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginPageController {

    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Label usernameErrorLabel;
    @FXML Label passwordErrorLabel;
    @FXML Button loginButton;

    private int loginAttemptsLeft = 5;

    public void backButton() {
        SceneManager.getInstance().navigateToLast();
    }

    @FXML
    public void handleSignupButton() {
        SceneManager.getInstance().navigateTo("signup-page.fxml");
    }

    @FXML
    public EventHandler<ActionEvent> handleLoginButton() {
        // Clear the last error text
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");

        boolean usernameFormattedProperly = checkUsernameFormatting();
        boolean passwordFormattedProperly = checkPasswordFormatting();

        if(usernameFormattedProperly && passwordFormattedProperly){
            UserSession.getInstance().login(DatabaseManager.findCustomerByPhone(usernameField.getText()));
            SceneManager.getInstance().navigateToLast();
        }
        return null;
    }

    private boolean checkUsernameFormatting(){
        if(usernameField.getText().isBlank()){
            usernameErrorLabel.setText("Username cannot be blank");
            return false;
        } else if(usernameField.getText().length() != 12){
            usernameErrorLabel.setText("Username should be phone number in the format XXX-XXX-XXXX");
            return false;
        }/* else if(searchUsername(usernameField.getText())){     // Not done, should return bool
            usernameErrorLabel.setText("No matching user record with this phone number");
        }*/
        return true;
    }

    private boolean checkPasswordFormatting(){
        if(passwordField.getText().isBlank()){
            passwordErrorLabel.setText("Password cannot be blank");
            return false;
        } else if(passwordField.getText().length() < 8){
            passwordErrorLabel.setText("Password must be at least 8 characters long");
            return false;
        } else if(true){    // Not done, should return bool
            if(loginAttemptsLeft <= 0){
                startLockoutTimer();
            } else {
                passwordErrorLabel.setText("Password is incorrect. " + loginAttemptsLeft + ((loginAttemptsLeft == 1) ? " try left" : " tries left"));
                loginAttemptsLeft--;
            }
        }
        return true;
    }

    private void startLockoutTimer(){
        loginButton.setOnAction(null);  // Remove login action so they cant spam the timer
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int s = 3; s > 0; s--){
                    final int count = s;

                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        passwordErrorLabel.setText("Password incorrect. Please wait " + count + " seconds before trying again.");
                    });
                    Thread.sleep(1000);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            SceneManager.getInstance().navigateTo("login-page.fxml");
        });

        new Thread(task).start();
    }

    @FXML
    public void handleHomeButton() {
        SceneManager.getInstance().navigateTo("home-page.fxml");
    }
}
