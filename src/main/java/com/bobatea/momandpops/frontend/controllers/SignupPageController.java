
import com.bobatea.momandpops.backend.models.Customer;
import com.bobatea.momandpops.backend.models.DatabaseManager;
import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

public class SignupPageController {

    @FXML public TextField nameField;
    @FXML public TextField phoneField;
    @FXML public TextField emailField;
    @FXML public TextField addressField;
    @FXML public TextField passwordField;
    @FXML public Label passwordErrorLabel;
    @FXML public Button signupButton;
    @FXML public Label addressErrorLabel;
    @FXML public Label emailErrorLabel;
    @FXML public Label phoneErrorLabel;
    @FXML public Label nameErrorLabel;
    @FXML public Button loginButton;
    @FXML public Button homeButton;
    @FXML public Label successLabel;

    @FXML
    public void handleSignupButton() {
        // Clear the last error text
        nameErrorLabel.setText("");
        phoneErrorLabel.setText("");
        emailErrorLabel.setText("");
        addressErrorLabel.setText("");
        passwordErrorLabel.setText("");

        boolean nameFormattedProperly = checkNameFormatting();
        boolean addressFormattedProperly = checkAddressFormatting();
        boolean emailFormattedProperly = checkEmailFormatting();
        boolean phoneFormattedProperly = checkPhoneNumberFormatting();
        boolean passwordFormattedProperly = checkPasswordFormatting();

        if(
                nameFormattedProperly &&
                addressFormattedProperly &&
                emailFormattedProperly &&
                phoneFormattedProperly &&
                passwordFormattedProperly
        ){
            DatabaseManager.createCustomer(nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    passwordField.getText()
            );

            if(UserSession.getInstance().login(DatabaseManager.findCustomerByPhone(phoneField.getText()), passwordField.getText())){
                successLabel.setText("Customer successfully created. Continuing to home page...");
                successLabel.setVisible(true);
                SceneManager.getInstance().navigateTo("home-page.fxml");
            } else {
                successLabel.setText("Failed to create new customer. Please check formatting...");
                successLabel.setVisible(true);
            }
        } else {
            System.out.println("Formatting Checks Failing");
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
}
