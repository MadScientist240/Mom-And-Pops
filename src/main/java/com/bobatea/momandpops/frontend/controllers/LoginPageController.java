package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.DatabaseManager;
import com.bobatea.momandpops.backend.models.Customer;
import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginPageController {

    @FXML private TextField usernameField;      // phone number in format XXX-XXX-XXXX
    @FXML private PasswordField passwordField;
    @FXML private Label usernameErrorLabel;
    @FXML private Label passwordErrorLabel;
    @FXML private Button loginButton;

    private int loginAttemptsLeft = 3;
    private boolean isLockedOut = false;

    public void backButton() {
        SceneManager.getInstance().navigateToLast();
    }

    @FXML
    private void handleSignupButton() {
        SceneManager.getInstance().navigateTo("signup-page.fxml");
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        attemptLogin();
    }

    private void attemptLogin() {
        if (isLockedOut) {
            // Ignore clicks while locked out
            return;
        }

        // Clear previous errors
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");

        String phone = usernameField.getText().trim();
        String password = passwordField.getText();

        // Basic format checks
        if (!checkUsernameFormatting(phone) | !checkPasswordFormatting(password)) {
            return;
        }

        // Look up customer in our "database"
        Optional<Customer> customerOpt = DatabaseManager.findCustomerByPhone(phone);

        if (customerOpt.isEmpty()) {
            usernameErrorLabel.setText("No matching user record with this phone number");
            return;
        }

        Customer customer = customerOpt.get();

        // Password check
        if (!customer.getPassword().equals(password)) {
            handleWrongPassword();
            return;
        }

        // Success: log in and go back
        UserSession.getInstance().login(customer);
        SceneManager.getInstance().navigateToLast();
    }

    private boolean checkUsernameFormatting(String phone){
        if (phone.isBlank()) {
            usernameErrorLabel.setText("Username cannot be blank");
            return false;
        } else if (phone.length() != 12 || phone.charAt(3) != '-' || phone.charAt(7) != '-') {
            usernameErrorLabel.setText("Username should be phone number in the format XXX-XXX-XXXX");
            return false;
        }
        return true;
    }

    private boolean checkPasswordFormatting(String password){
        if (password.isBlank()) {
            passwordErrorLabel.setText("Password cannot be blank");
            return false;
        } else if (password.length() < 8) {
            passwordErrorLabel.setText("Password must be at least 8 characters");
            return false;
        }
        return true;
    }

    private void handleWrongPassword() {
        if (loginAttemptsLeft <= 1) {
            startLockoutTimer();
        } else {
            loginAttemptsLeft--;
            passwordErrorLabel.setText(
                    "Password is incorrect. " + loginAttemptsLeft +
                            (loginAttemptsLeft == 1 ? " try left" : " tries left")
            );
        }
    }

    private void startLockoutTimer(){
        isLockedOut = true;
        loginButton.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Lock for 30 seconds
                for (int s = 30; s > 0; s--) {
                    final int count = s;
                    Platform.runLater(() ->
                            passwordErrorLabel.setText(
                                    "Too many attempts. Please wait " + count + " seconds."
                            )
                    );
                    Thread.sleep(1000);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            isLockedOut = false;
            loginAttemptsLeft = 3;
            loginButton.setDisable(false);
            passwordErrorLabel.setText("");
        });

        new Thread(task).start();
    }
}
