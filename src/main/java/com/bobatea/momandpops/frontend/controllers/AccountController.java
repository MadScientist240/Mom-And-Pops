package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.Card;
import com.bobatea.momandpops.backend.models.DatabaseManager;
import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AccountController {
    @FXML private Label cardNumberErrorLabel;
    @FXML private Label expDateErrorLabel;
    @FXML private Label cvvErrorLabel;
    @FXML private Label deletionLabel;
    @FXML private Label personalInfoLabel;
    @FXML private Label cardInfoLabel;
    @FXML private Label deleteAccountLabel;
    @FXML private VBox deleteAccountView;
    @FXML private Button deletionConfirmButton;
    @FXML private Button deletionCancelButton;
    @FXML private VBox infoView;
    @FXML private TextField nameField;
    @FXML private Label nameErrorField;
    @FXML private TextField phoneField;
    @FXML private Label phoneErrorLabel;
    @FXML private TextField emailField;
    @FXML private Label emailErrorLabel;
    @FXML private TextField addressField;
    @FXML private Label addressErrorLabel;
    @FXML private TextField passwordField;
    @FXML private Label passwordErrorLabel;
    @FXML private Button infoSaveButton;
    @FXML private Button infoCancelButton;
    @FXML private VBox cardView;
    @FXML private Button cardSaveButton;
    @FXML private TextField cvvField;
    @FXML private TextField expDateField;
    @FXML private TextField cardNumberField;
    @FXML private HeaderController navigationHeaderController;

    int deletionAttempts = 3;

    public void initialize(){
        // Configure header
        if (navigationHeaderController != null) {
            navigationHeaderController.showLogo(false);
            navigationHeaderController.setTitle("My Account");
        }
        setInfoView();

        nameField.setText(UserSession.getInstance().getCurrentCustomer().getName());
        phoneField.setText(UserSession.getInstance().getCurrentCustomer().getPhone());
        emailField.setText(UserSession.getInstance().getCurrentCustomer().getEmail());
        addressField.setText(UserSession.getInstance().getCurrentCustomer().getAddress());
        passwordField.setText(UserSession.getInstance().getCurrentCustomer().getPassword());
    }

    private void highlightSection(Label section){
        section.setUnderline(true);
        if(section.equals(personalInfoLabel)){
            cardInfoLabel.setUnderline(false);
            deleteAccountLabel.setUnderline(false);
        } else if(section.equals(cardInfoLabel)){
            personalInfoLabel.setUnderline(false);
            deleteAccountLabel.setUnderline(false);
        } else {
            personalInfoLabel.setUnderline(false);
            cardInfoLabel.setUnderline(false);
        }
    }

    public void setInfoView(){
        infoView.setVisible(true);
        cardView.setVisible(false);
        deleteAccountView.setVisible(false);
        highlightSection(personalInfoLabel);
    }

    public void setCardView(){
        cardView.setVisible(true);
        infoView.setVisible(false);
        deleteAccountView.setVisible(false);
        highlightSection(cardInfoLabel);
    }

    public void setDeletionView(){
        deleteAccountView.setVisible(true);
        deletionLabel.setText("Are you sure you want to delete your account?");
        infoView.setVisible(false);
        cardView.setVisible(false);
        highlightSection(deleteAccountLabel);
    }

    private void startRedirectTimer(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int s = 5; s > 0; s--){
                    final int count = s;

                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        deletionLabel.setText("Deletion successful! Redirecting to home page in " + count + " seconds...");
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

    public void handleDeletionConfirm() {
        deletionLabel.setText("Press confirm " + deletionAttempts + " times to delete account");
        if(deletionAttempts < 1){
            //DatabaseManager.removeCustomer(UserSession.getInstance().getCurrentCustomer());   // TODO FIXME
            UserSession.getInstance().logout();
            startRedirectTimer();
        }
        deletionAttempts--;
    }

    public void handleDeletionCancel() {
        deletionLabel.setText("Deletion canceled.");
    }

    public boolean checkNameFormatting(){
        if(nameField.getText().isBlank()){
            nameErrorField.setText("Name should not be blank");
            return false;
        } else if(nameField.getText().split(" ").length != 2){
            nameErrorField.setText("Name should follow the format [First] [Last]");
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
        } else if (DatabaseManager.findCustomerByPhone(phoneField.getText()).isPresent()) {
            phoneErrorLabel.setText("Username already linked to an account. Please login!");
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
            passwordErrorLabel.setText("Password should be at least 8 characters long");
            return false;
        } else if(!passwordField.getText().matches(".*[!@$%&*].*")){
            passwordErrorLabel.setText("Password must contain one of these characters: (!, @, $, %, &, *)");
            return false;
        } else if (!passwordField.getText().matches(".*\\d.*")) {
            passwordErrorLabel.setText("Password must contain at least 1 number");
            return false;
        } else if (!passwordField.getText().chars().anyMatch(Character::isUpperCase)) {
            passwordErrorLabel.setText("Password must contain at least one upper case letter");
            return false;
        }
        return true;
    }

    public boolean checkCardNumberFormatting(){
        if(cardNumberField.getText().isBlank()){
            cardNumberErrorLabel.setText("Card number cannot be left blank.");
            return false;
        } else if(cardNumberField.getText().length() != 12){
            cardNumberErrorLabel.setText("Card number must be in the format of XXXXXXXXXXXXXXXX");
            return false;
        } else if(!cardNumberField.getText().chars().allMatch(Character::isDigit)){
            cardNumberErrorLabel.setText("Card number must use numbers only");
            return false;
        }
        return true;
    }

    public boolean checkCardCVVFormatting(){
        if(cvvField.getText().length() != 3){
            cvvErrorLabel.setText("CVV must be exactly 3 numbers");
            return false;
        } else if (!cvvField.getText().chars().allMatch(Character::isDigit)) {
            cvvErrorLabel.setText("CVV must use numbers only");
            return false;
        }
        return true;
    }

    public boolean checkCardExpDateFormatting(){
        if(!expDateField.getText().contains("/")){
            expDateErrorLabel.setText("Exp. Date must be in the format MM/YY");
            return false;
        } else if (expDateField.getText().length() != 5) {
            expDateErrorLabel.setText("Exp. Date must be 5 characters lomg");
            return false;
        }
        return true;
    }

    public void handleInfoCancelButton() {
        nameField.setText(UserSession.getInstance().getCurrentCustomer().getName());
        phoneField.setText(UserSession.getInstance().getCurrentCustomer().getPhone());
        emailField.setText(UserSession.getInstance().getCurrentCustomer().getEmail());
        addressField.setText(UserSession.getInstance().getCurrentCustomer().getAddress());
        passwordField.setText(UserSession.getInstance().getCurrentCustomer().getPassword());
    }

    public void handleInfoSaveButton() {
        // Clear the last error text
        nameErrorField.setText("");
        phoneErrorLabel.setText("");
        emailErrorLabel.setText("");
        addressErrorLabel.setText("");
        passwordErrorLabel.setText("");

        boolean nameFormattedProperly = checkNameFormatting();
        boolean phoneFormattedProperly = checkPhoneNumberFormatting();
        boolean emailFormattedProperly = checkEmailFormatting();
        boolean addressFormattedProperly = checkAddressFormatting();
        boolean passwordFormattedProperly = checkPasswordFormatting();

        if(
            nameFormattedProperly &&
            phoneFormattedProperly &&
            emailFormattedProperly &&
            addressFormattedProperly &&
            passwordFormattedProperly
        ){
            UserSession.getInstance().getCurrentCustomer().setName(nameField.getText());
            UserSession.getInstance().getCurrentCustomer().setPhone(phoneField.getText());
            UserSession.getInstance().getCurrentCustomer().setEmail(emailField.getText());
            UserSession.getInstance().getCurrentCustomer().setAddress(addressField.getText());
            UserSession.getInstance().getCurrentCustomer().setPassword(passwordField.getText());
        } else {
            System.out.println("Formatting checks failed.");
        }


    }

    public void handleCardSaveButton() {
        // Clear the last error text
        cardNumberField.setText("");
        cvvField.setText("");
        expDateField.setText("");

        boolean cardNumberFormattedProperly = checkCardNumberFormatting();
        boolean cardExpDateFormattedProperly = checkCardExpDateFormatting();
        boolean cardCVVFormattedProperly = checkCardCVVFormatting();

        if( cardExpDateFormattedProperly && cardNumberFormattedProperly && cardCVVFormattedProperly){
            // TODO: Add DB Manager method here to add a card to the DB
            //Card card = new Card()
            //DatabaseManager.createCard(card);
        }
    }
}
