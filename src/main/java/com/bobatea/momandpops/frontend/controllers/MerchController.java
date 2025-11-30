package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.DatabaseManager;
import com.bobatea.momandpops.backend.models.Item;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MerchController {

    @FXML private VBox itemsView;
    @FXML private FlowPane itemsGrid;
    @FXML private HBox navigationHeader;
    @FXML private HeaderController navigationHeaderController;

    public void initialize(){
        // Configure header
        if (navigationHeaderController != null) {
            navigationHeaderController.showLogo(false);
            navigationHeaderController.setTitle("Merch");
        }
        itemsGrid.getChildren().clear();
        showItems();
    }

    private void showItems() {
        itemsGrid.getChildren().clear();
        for (Item item : DatabaseManager.getMerchItems()) {
            boolean customizable = item.hasColors || item.hasSizes;
            itemsGrid.getChildren().add(createItemCard(item, customizable));
        }
    }

    private VBox createItemCard(Item item, boolean isCustomizeable) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; " +
                "-fx-padding: 15; " +
                "-fx-border-color: #ddd; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;");
        card.setPrefSize(210, 250);

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(item.imagePath));
            imageView.setImage(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(true);
            card.getChildren().add(imageView);
        } catch (NullPointerException e) {
            // Fallback to placeholder if image not found
            Label imagePlaceholder = new Label("[Image]");
            imagePlaceholder.setStyle("-fx-background-color: #e0e0e0; " +
                    "-fx-pref-width: 150; " +
                    "-fx-pref-height: 120; " +
                    "-fx-alignment: center;");
            card.getChildren().add(imagePlaceholder);
        }

        Label nameLabel = new Label(item.name);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #666;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(150);

        Label priceLabel = new Label("$" + String.format("%.2f", item.getBasePrice()));
        priceLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");

        Button interaction = new Button();

        interaction.setText(isCustomizeable ? "Customize" : "Add to Cart");
        interaction.setStyle("-fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 8 20; " +
                "-fx-cursor: hand;");
        interaction.setOnAction(e -> openCustomizePopup(item));

        card.getChildren().addAll(nameLabel, priceLabel, interaction);
        return card;
    }

    private void openCustomizePopup(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bobatea/momandpops/views/item-popup.fxml")
            );
            Parent root = loader.load();
            Stage popup = new Stage();
            popup.setTitle("Customize Item");
            ItemPopupController pc = loader.getController();
            pc.setItem(item);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setScene(new Scene(root));
            popup.showAndWait();
        } catch (Exception e) {
            System.err.println("Failed to load popup");
            e.printStackTrace();
        }
    }
}
