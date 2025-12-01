package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.UserSession;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class HeaderController {

    @FXML private MenuButton navigationMenu;
    @FXML private MenuButton profileMenu;
    @FXML private ImageView logoImage;
    @FXML private Label titleLabel;

    public void initialize() {
        buildNavigationMenu();
        buildProfileMenu();
    }

    // Show or hide logo
    public void showLogo(boolean show) {
        logoImage.setVisible(show);
        logoImage.setManaged(show);
    }

    // Show or set title
    public void setTitle(String title) {
        if (title != null && !title.isEmpty()) {
            titleLabel.setText(title);
            titleLabel.setVisible(true);
            titleLabel.setManaged(true);
        } else {
            titleLabel.setVisible(false);
            titleLabel.setManaged(false);
        }
    }

    private void buildNavigationMenu() {
        navigationMenu.getItems().clear();

        navigationMenu.getItems().add(createMenuItem("Home"));
        navigationMenu.getItems().add(createMenuItem("Menu"));
        navigationMenu.getItems().add(createMenuItem("Merch"));
        navigationMenu.getItems().add(createMenuItem("About Us"));
    }

    private void buildProfileMenu() {
        profileMenu.getItems().clear();

        if (UserSession.getInstance().isLoggedIn()) {
            profileMenu.getItems().add(createMenuItem("My Account"));
            profileMenu.getItems().add(createMenuItem("My Orders"));
            profileMenu.getItems().add(createMenuItem("My Cart"));
            profileMenu.getItems().add(createMenuItem("Logout"));
        } else {
            profileMenu.getItems().add(createMenuItem("Login"));
            profileMenu.getItems().add(createMenuItem("Sign Up"));
        }
    }

    private MenuItem createMenuItem(String text) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(e -> handleMenuClick(text));
        return item;
    }

    private void handleMenuClick(String menuItem) {
        switch (menuItem){
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

            case "Login":
                SceneManager.getInstance().navigateTo("login-page.fxml");
                break;

            case "Logout":
                SceneManager.getInstance().navigateTo("logout-page.fxml");
                break;

            case "Sign Up":
                SceneManager.getInstance().navigateTo("signup-page.fxml");
                break;

            case "My Orders":
                SceneManager.getInstance().navigateTo("order-page.fxml");
                break;

            case "My Account":
                SceneManager.getInstance().navigateTo("account-page.fxml");
                break;

            case "My Cart":
                SceneManager.getInstance().navigateTo("cart-page.fxml");
                break;
        }
    }
}
