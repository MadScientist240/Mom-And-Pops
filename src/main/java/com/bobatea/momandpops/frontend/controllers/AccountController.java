package com.bobatea.momandpops.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class AccountController {
    @FXML private HBox navigationHeader;
    @FXML private HeaderController navigationHeaderController;

    public void initialize(){
        // Configure header
        if (navigationHeaderController != null) {
            navigationHeaderController.showLogo(false);
            navigationHeaderController.setTitle("My Account");
        }
    }
}
