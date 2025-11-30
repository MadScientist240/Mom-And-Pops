package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.*;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CartController {
    @FXML private Label rewardsErrorLabel;
    @FXML private VBox itemContainer;
    @FXML private Button menuButton;
    @FXML private VBox emptyView;
    @FXML private Button merchButton;
    @FXML private ScrollPane itemPane;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label deliveryFeeLabel;
    @FXML private Label totalLabel;
    @FXML private ToggleButton rewardPointsToggle;
    @FXML private Button orderModeButton;
    @FXML private Button checkoutButton;
    @FXML private HBox navigationHeader;
    @FXML private HeaderController navigationHeaderController;

    private boolean orderModality = false;  //false = pickup, true = delivery
    private boolean discountApplied = false;

    public void initialize(){
        Label test = new Label("TEST LABEL");
        test.setStyle("-fx-font-size: 30; -fx-background-color: yellow;");
        itemContainer.getChildren().add(test);

        // Configure header
        if (navigationHeaderController != null) {
            navigationHeaderController.showLogo(false);
            navigationHeaderController.setTitle("My Cart");
        }
        loadCartItems();
        setTotals();
    }

    public void loadCartItems(){
        itemContainer.getChildren().clear();

        if(Cart.getInstance().getItems().isEmpty()){
            showEmptyView();
        } else {
            for(CustomizedItem item : Cart.getInstance().getItems()){
                itemContainer.getChildren().add(createItemCard(item));
            }
            showItemView();
        }
    }

    private VBox createItemCard(CustomizedItem item){
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #666; -fx-border-radius: 5;");

        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        ImageView img = new ImageView(new Image(item.getImagePath()));
        img.setFitWidth(80);
        img.setFitHeight(80);
        img.setPreserveRatio(true);

        VBox titleBox = new VBox(5);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        Label title = new Label(item.name);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label mods = new Label(String.join(", ", item.getSelectedModifications()));
        mods.setStyle("-fx-text-fill: #666;");
        mods.setWrapText(true);
        titleBox.getChildren().addAll(title, mods);

        VBox qtyBox = new VBox(3);
        qtyBox.setAlignment(Pos.CENTER);
        Label qtyLabel = new Label("Qty: " + item.getQuantity());
        qtyLabel.setStyle("-fx-font-size: 16px;");
        qtyBox.getChildren().add(qtyLabel);

        VBox totalBox = new VBox(3);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        Label priceLabel = new Label(String.format("$%.2f", item.getTotalCost()));
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        totalBox.getChildren().add(priceLabel);

        VBox buttonBox = new VBox(5);
        buttonBox.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 15;");
        editBtn.setOnAction(e -> {
            openCustomizePopup(item);
            loadCartItems();
            setTotals();
        });

        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5 15;");
        removeBtn.setOnAction(e -> {
            itemContainer.getChildren().remove(card);
            if(Cart.getInstance().removeItem(item)){
                loadCartItems();
                setTotals();
            } else {
                System.out.println("Failed to remove item " + item.name + " from cart");
            }
        });
        buttonBox.getChildren().addAll(editBtn, removeBtn);

        content.getChildren().addAll(img, titleBox, qtyBox, totalBox, buttonBox);
        card.getChildren().add(content);
        return card;
    }

    public void setTotals(){
        rewardsErrorLabel.setText("");
        subtotalLabel.setText(String.format("$%.2f", Cart.getInstance().getSubtotal()));
        taxLabel.setText(String.format("$%.2f", Cart.getInstance().getTotalTax()));
        deliveryFeeLabel.setText(String.format("$%.2f", Cart.getInstance().getDeliveryFee()));
        totalLabel.setText(String.format("$%.2f", Cart.getInstance().getTotal()));
    }

    public void showEmptyView(){
        emptyView.setVisible(true);
        itemPane.setVisible(false);
    }

    public void showItemView(){
        emptyView.setVisible(false);
        itemPane.setVisible(true);
    }

    private void openCustomizePopup(CustomizedItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bobatea/momandpops/views/item-popup.fxml")
            );
            Parent root = loader.load();
            Stage popup = new Stage();
            popup.setTitle(item.name);
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

    public void handleMenuButton(){
        SceneManager.getInstance().navigateTo("menu-page.fxml");
    }

    public void handleMerchButton(){
        SceneManager.getInstance().navigateTo("merch-page.fxml");
    }

    public void handleOrderModeButton(){
        orderModality = !orderModality;
        if(orderModality){  // delivery
            orderModeButton.setText("Delivery");
            Cart.getInstance().setDeliveryFee(3.50);
        } else {    // pickup
            orderModeButton.setText("Pickup");
            Cart.getInstance().setDeliveryFee(0.00);
        }
        setTotals();
    }

    public void handleRewardsButton() {
        rewardsErrorLabel.setVisible(true);
        if(UserSession.getInstance().getCurrentCustomer().getRewardPoints() < 10){
            rewardsErrorLabel.setText("You do not have enough reward points for this.");
        }else if(discountApplied) {
            rewardsErrorLabel.setVisible(true);
            rewardsErrorLabel.setText("Discount Already Applied!");
        } else if(Cart.getInstance().getTotal() < 20.00){

        } else {
            discountApplied = true;
            Cart.getInstance().addDiscount(10.00);
        }
    }
}
