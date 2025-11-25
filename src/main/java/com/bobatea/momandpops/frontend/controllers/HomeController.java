package com.bobatea.momandpops.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class HomeController {

    @FXML private Button locationButton;
    @FXML private HBox navigationHeader;
    @FXML private HeaderController navigationHeaderController;

    public void initialize(){
        // Configure header
        if (navigationHeaderController != null) {
            navigationHeaderController.showLogo(true);
            navigationHeaderController.setTitle(null);
        }
    }

    /*
     *  Click Handlers
     */
    @FXML
    private void openMapsUrl(){
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://maps.app.goo.gl/nMcBYaVDYwCnGLyKA"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}