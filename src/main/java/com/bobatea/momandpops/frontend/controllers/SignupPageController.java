package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.Customer;
import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupPageController {

    @FXML public TextField nameField;
    @FXML public TextField phoneField;
    @FXML public TextField emailField;
    @FXML public TextField addressField;
    @FXML public PasswordField passwordField;
    @FXML public Label passwordErrorLabel;
    @FXML public Button signupButton;
    @FXML public Label addressErrorLabel;
    @FXML public Label emailErrorLabel;
    @FXML public Label phoneErrorLabel;
    @FXML public Label nameErrorLabel;
    @FXML public Button loginButton;
    @FXML public Button homeButton;

    @FXML
    public void handleSignupButton() {
        // Clear the last error text
        nameErrorLabel.setText("");
        phoneErrorLabel.setText("");
        emailErrorLabel.setText("");
        addressErrorLabel.setText("");
        passwordErrorLabel.setText("");

        if(
                checkNameFormatting() &&
                checkAddressFormatting() &&
                checkEmailFormatting() &&
                checkPhoneNumberFormatting() &&
                checkPasswordFormatting()
        ){
            Customer newUser = new Customer(
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    passwordField.getText(),
                    0);
            UserSession.getInstance().login(newUser);
        }

    }

    @FXML
    public void handleLoginButton() {
        SceneManager.getInstance().navigateTo("login-page.fxml");
    }

    public void handleHomeButton() {
        SceneManager.getInstance().navigateTo("home-page.fxml");
    }

    public boolean checkNameFormatting(){
        if(nameField.getText().isBlank()){
            nameErrorLabel.setText("Name should not be blank");
            return false;
        } else if(nameField.getText().split(" ").length != 2){
            nameErrorLabel.setText("Name should follow the format [First] [Last]");
            return false;
        }
        return true;
    }

    public boolean checkPhoneNumberFormatting(){
        if(phoneField.getText().isBlank()){
            phoneErrorLabel.setText("Username cannot be blank");
            return false;
        } else if(phoneField.getText().length() != 12){
            phoneErrorLabel.setText("Username should be phone number in the format XXX-XXX-XXXX");
            return false;
        }
        return true;
    }

    public boolean checkAddressFormatting(){
        if(addressField.getText().isBlank()){
            addressErrorLabel.setText("Address should not be blank");
            return false;
        } else if(addressField.getText().split(",").length != 4){
            addressErrorLabel.setText("Address should follow the format [Street], [City], [State], [Zip Code]");
            return false;
        }
        return true;
    }

    public boolean checkEmailFormatting(){
        if(emailField.getText().isBlank()){
            emailErrorLabel.setText("Email cannot be blank");
            return false;
        } else if(!emailField.getText().contains("@")){
            emailErrorLabel.setText("Email should have @gmail.com, @outlook.com, @hotmail.com, @aol.com, or similar");
            return false;
        }
        return true;
    }

    public boolean checkPasswordFormatting(){
        if(passwordField.getText().isBlank()){
            passwordErrorLabel.setText("Password cannot be blank");
            return false;
        } else if(passwordField.getText().length() < 8){
            passwordErrorLabel.setText("Password should be at least 15 characters long");
            return false;
        } else if(
                passwordField.getText().contains("!") ||
                passwordField.getText().contains("@") ||
                passwordField.getText().contains("$") ||
                passwordField.getText().contains("%") ||
                passwordField.getText().contains("&") ||
                passwordField.getText().contains("*")
        ){
            passwordErrorLabel.setText("Password must contain one of these characters: (!, @, $, %, &, *");
            return false;
        } else if (passwordField.getText().chars().anyMatch(Character::isUpperCase)) {
            passwordErrorLabel.setText("Password must contain at least one upper case letter");
            return false;
        }
        return true;
    }
}
