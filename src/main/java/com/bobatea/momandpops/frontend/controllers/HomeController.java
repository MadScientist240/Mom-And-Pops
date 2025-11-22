package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.frontend.SceneManager;
import javafx.fxml.FXML;
import java.awt.Desktop;
import java.net.URI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.bobatea.momandpops.backend.models.UserSession;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {

    @FXML private MenuButton navigationMenu;

    @FXML private MenuButton profileMenu;

    @FXML private Button locationButton;

    public void initialize(){
        boolean loggedIn = UserSession.getInstance().isLoggedIn();
        buildProfileMenu(loggedIn);
        buildNavigationMenu();
    }

    public void buildProfileMenu(boolean loggedIn){
        if(loggedIn){
            profileMenu.getItems().add(createMenuItem("My Account", "profileMenu"));
            profileMenu.getItems().add(createMenuItem("My Orders", "profileMenu"));
            profileMenu.getItems().add(createMenuItem("My Cart", "profileMenu"));
            profileMenu.getItems().add(createMenuItem("Logout", "profileMenu"));
        } else {
            profileMenu.getItems().add(createMenuItem("Login/Sign Up", "profileMenu"));
        }
    }

    public void buildNavigationMenu(){
        navigationMenu.getItems().add(createMenuItem("Home", "navigationMenu"));
        navigationMenu.getItems().add(createMenuItem("Menu", "navigationMenu"));
        navigationMenu.getItems().add(createMenuItem("Merch", "navigationMenu"));
        navigationMenu.getItems().add(createMenuItem("About Us", "navigationMenu"));
    }

    /*
     *  Click Handlers
     */
    private void handleProfileMenuClick(String menuItem) {
        switch(menuItem){
            case "Login/Sign Up":
                SceneManager.getInstance().navigateTo("login-page.fxml");
                // Extra For Development
                UserSession.getInstance().login();
                SceneManager.getInstance().navigateTo("home-page.fxml");
                break;
            case "My Account":
                SceneManager.getInstance().navigateTo("account-page.fxml");
                break;
            case "My Orders":
                SceneManager.getInstance().navigateTo("orders-page.fxml");
                break;
            case "My Cart":
                SceneManager.getInstance().navigateTo("cart-page.fxml");
                break;
            case "Logout":
                SceneManager.getInstance().navigateTo("logout-page.fxml");
                break;
            default:
                System.out.println("Unknown item case. Check for syntax errors");
                break;
        }
    }

    private void handleNavigationMenuClick(String menuItem) {
        switch(menuItem){
            case "Home":
                SceneManager.getInstance().navigateTo("home-page.fxml");
                break;
            case "Menu":
                SceneManager.getInstance().navigateTo("menu-page.fxml");
                break;
            case "Merch":
                SceneManager.getInstance().navigateTo("merch-page.fxml");
                break;
            case "About Us":
                SceneManager.getInstance().navigateTo("about-page.fxml");
                break;
            default:
                System.out.println("Unknown item case. Check for syntax errors");
                break;
        }
    }

    @FXML
    private void openMapsUrl(){
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://maps.app.goo.gl/nMcBYaVDYwCnGLyKA"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  Misc Functions
     */
    private MenuItem createMenuItem(String text, String component) {
        MenuItem item = new MenuItem(text);
        if(component == "profileMenu") {
            item.setOnAction(e -> handleProfileMenuClick(text));
        } else if(component == "navigationMenu"){
            item.setOnAction(e -> handleNavigationMenuClick(text));
        }
        return item;
    }

    @FXML
    private void testCustomizePopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bobatea/momandpops/views/item-popup.fxml")
            );
            Parent root = loader.load();

            Stage popup = new Stage();
            popup.setTitle("Customize Item");
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setScene(new Scene(root));
            popup.showAndWait();
        } catch (Exception e) {
            System.err.println("Failed to load popup");
            e.printStackTrace();
        }
    }
}