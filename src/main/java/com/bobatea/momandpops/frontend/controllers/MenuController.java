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
import javafx.scene.control.MenuButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController {

    @FXML private VBox categoryView;
    @FXML private VBox itemsView;
    @FXML private FlowPane categoryGrid;
    @FXML private FlowPane itemsGrid;
    @FXML private Label categoryTitle;
    @FXML private MenuButton navigationMenu;
    @FXML private HBox navigationHeader;
    @FXML private HeaderController navigationHeaderController;

    public void initialize(){
        // Configure header
        if (navigationHeaderController != null) {
            navigationHeaderController.showLogo(false);
            navigationHeaderController.setTitle("Menu");
        }
        itemsGrid.getChildren().clear();
        loadCategories();
    }

    private void loadCategories(){
        categoryGrid.getChildren().add(createCategoryCard("Pizza"));
        categoryGrid.getChildren().add(createCategoryCard("Desserts"));
        categoryGrid.getChildren().add(createCategoryCard("Drinks"));
        categoryGrid.getChildren().add(createCategoryCard("Sides"));
    }

    private VBox createCategoryCard(String categoryName) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; " +
                "-fx-padding: 30; " +
                "-fx-border-color: #ddd; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-cursor: hand;");
        card.setPrefSize(300, 300);

        Label icon = new Label("🍕");
        icon.setStyle("-fx-font-size: 48px;");

        Label name = new Label(categoryName);
        name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-font-color: #000000;");

        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #f0f0f0; " +
                        "-fx-padding: 30; " +
                        "-fx-border-color: #4CAF50; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-padding: 30; " +
                        "-fx-border-color: #ddd; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"));

        card.setOnMouseClicked(e -> showItemsForCategory(categoryName));

        card.getChildren().addAll(icon, name);
        return card;
    }

    private void showItemsForCategory(String category) {
        categoryView.setVisible(false);
        itemsView.setVisible(true);

        categoryTitle.setText(category);
        itemsGrid.getChildren().clear();

        for (Item item : DatabaseManager.getMenuItemsByCategory(category)) {
            boolean customizable = item.hasColors || item.hasCrust || item.hasSizes || item.hasToppings;
            itemsGrid.getChildren().add(createItemCard(item, customizable));
        }
    }

    @FXML
    private void showCategoryView() {
        categoryView.setVisible(true);
        itemsView.setVisible(false);
    }

    private VBox createItemCard(Item item, boolean isCustomizeable) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; " +
                "-fx-padding: 15; " +
                "-fx-border-color: #ddd; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5;");
        card.setPrefSize(180, 220);

        // Placeholder for image
        Label imagePlaceholder = new Label("[Image]");
        imagePlaceholder.setStyle("-fx-background-color: #e0e0e0; " +
                "-fx-pref-width: 150; " +
                "-fx-pref-height: 120; " +
                "-fx-alignment: center;");

        Label nameLabel = new Label(item.name);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
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

        card.getChildren().addAll(imagePlaceholder, nameLabel, priceLabel, interaction);
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
