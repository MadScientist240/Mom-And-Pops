package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.Customer;
import com.bobatea.momandpops.backend.models.DatabaseManager;
import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignupPageController {

    @FXML public TextField nameField;
    @FXML public TextField phoneField;
    @FXML public TextField emailField;
    @FXML public TextField addressField;
    @FXML public TextField passwordField;

    @FXML public Label passwordErrorLabel;
    @FXML public Label addressErrorLabel;
    @FXML public Label emailErrorLabel;
    @FXML public Label phoneErrorLabel;
    @FXML public Label nameErrorLabel;
    @FXML public Label successLabel;

    @FXML public Button signupButton;
    @FXML public Button loginButton;
    @FXML public Button homeButton;

    @FXML
    public void handleSignupButton() {
        nameErrorLabel.setText("");
        phoneErrorLabel.setText("");
        emailErrorLabel.setText("");
        addressErrorLabel.setText("");
        passwordErrorLabel.setText("");
        successLabel.setText("");
        successLabel.setVisible(false);

        boolean nameOk = checkNameFormatting();
        boolean addrOk = checkAddressFormatting();
        boolean emailOk = checkEmailFormatting();
        boolean phoneOk = checkPhoneNumberFormatting();
        boolean passwordOk = checkPasswordFormatting();

        if (nameOk && addrOk && emailOk && phoneOk && passwordOk) {
            Customer c = DatabaseManager.createCustomer(
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    passwordField.getText()
            );

            UserSession.getInstance().login();
            successLabel.setText("Customer successfully created. Continuing to home page...");
            successLabel.setVisible(true);
            SceneManager.getInstance().navigateTo("home-page.fxml");
        }
    }

    @FXML
    public void handleLoginButton() {
        SceneManager.getInstance().navigateTo("login-page.fxml");
    }

    @FXML
    public void handleHomeButton() {
        SceneManager.getInstance().navigateTo("home-page.fxml");
    }

    public boolean checkNameFormatting() {
        String name = nameField.getText();
        if (name.isBlank()) {
            nameErrorLabel.setText("Name should not be blank");
            return false;
        } else if (name.split(" ").length != 2) {
            nameErrorLabel.setText("Format: First Last");
            return false;
        }
        return true;
    }

    public boolean checkPhoneNumberFormatting() {
        String phone = phoneField.getText();
        if (phone.isBlank()) {
            phoneErrorLabel.setText("Phone cannot be blank");
            return false;
        } else if (phone.length() != 12) {
            phoneErrorLabel.setText("Format: XXX-XXX-XXXX");
            return false;
        } else if (DatabaseManager.findCustomerByPhone(phone).isPresent()) {
            phoneErrorLabel.setText("Account already exists");
            return false;
        }
        return true;
    }

    public boolean checkAddressFormatting() {
        String addr = addressField.getText();
        if (addr.isBlank()) {
            addressErrorLabel.setText("Address cannot be blank");
            return false;
        } else if (addr.split(",").length != 4) {
            addressErrorLabel.setText("Format: Street, City, State, Zip");
            return false;
        }
        return true;
    }

    public boolean checkEmailFormatting() {
        String email = emailField.getText();
        if (email.isBlank()) {
            emailErrorLabel.setText("Email cannot be blank");
            return false;
        } else if (!email.contains("@")) {
            emailErrorLabel.setText("Invalid email format");
            return false;
        }
        return true;
    }

    public boolean checkPasswordFormatting() {
        String pwd = passwordField.getText();
        if (pwd.isBlank()) {
            passwordErrorLabel.setText("Password cannot be blank");
            return false;
        } else if (pwd.length() < 8) {
            passwordErrorLabel.setText("Min 8 characters");
            return false;
        } else if (!pwd.matches(".*[!@$%&*].*")) {
            passwordErrorLabel.setText("Must contain ! @ $ % & *");
            return false;
        } else if (!pwd.matches(".*\\d.*")) {
            passwordErrorLabel.setText("Must contain a number");
            return false;
        } else if (pwd.chars().noneMatch(Character::isUpperCase)) {
            passwordErrorLabel.setText("Must contain uppercase");
            return false;
        }
        return true;
    }
    if (UserSession.getInstance().login(
        DatabaseManager.findCustomerByPhone(phoneField.getText()),
        passwordField.getText()
)) {
    ...
}

}

