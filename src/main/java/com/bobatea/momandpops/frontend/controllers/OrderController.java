package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.DatabaseManager;
import com.bobatea.momandpops.backend.models.Order;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class OrderController {
    @FXML private VBox emptyView;
    @FXML private Button menuButton;
    @FXML private ScrollPane ordersView;
    @FXML private VBox ordersContainer;
    @FXML private VBox orderDetailedView;
    @FXML private Label statusLabel;
    @FXML private Label costLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private Label orderTypeLabel;
    @FXML private Label employeeLabel;
    @FXML private Label earnedPointsLabel;
    @FXML private Label lostPointsLabel;
    @FXML private Button receiptButton;
    @FXML private Button backButton;
    @FXML private Label orderIDLabel;
    @FXML private HBox navigationHeader;
    @FXML private HeaderController navigationHeaderController;

    public void initialize(){
        // Configure header
        if (navigationHeaderController != null) {
            navigationHeaderController.showLogo(false);
            navigationHeaderController.setTitle("My Orders");
        }
        orderDetailedView.setVisible(false);
        loadOrders();
    }

    private void loadOrders(){
        if(DatabaseManager.getOrdersByCustomer().isEmpty()){
            openEmptyView();
            return; // ADD THIS - stop execution if empty
        }

        // If not empty, show orders view
        openOrderView(); // ADD THIS LINE

        ordersContainer.getChildren().add(createOrderCard(new Order(1000, 2000, 3000, 4000, 25.99, "Cash", "Pickup", false, "", "","", 0, 0, "10-01-25")));
    /*for(Order order : DatabaseManager.getOrdersByCustomer()){
        VBox orderCard = createOrderCard(order);
        ordersContainer.getChildren().add(orderCard);
    }*/
    }

    private VBox createOrderCard(Order order){
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-cursor: hand;");

        Label orderId = new Label("Order #" + order.getId());
        orderId.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label date = new Label(order.getDate());
        date.setStyle("-fx-text-fill: #666;");

        Label total = new Label(String.format("$%.2f", order.getTotalCost()));
        total.setStyle("-fx-font-weight: bold;");

        Label status = new Label(order.getCompletionStatus() ? "Completed" : "In Progress");
        status.setStyle("-fx-text-fill: " + (order.getCompletionStatus() ? "#27ae60;" : "#f39c12;"));

        card.getChildren().addAll(orderId, date, total, status);

        // Click to view details
        card.setOnMouseClicked(e -> openDetailedView(order));
        return card;
    }

    public void handleBackButton(ActionEvent actionEvent) {
        openOrderView();
    }

    private void openDetailedView(Order order){
        ordersView.setVisible(false);
        emptyView.setVisible(false);
        orderDetailedView.setVisible(true);

        paymentMethodLabel.setText("Payment Method " + order.getPaymentMethod());
        orderIDLabel.setText(String.valueOf(order.getId()));
        statusLabel.setText("Is complete: " + String.valueOf(order.getCompletionStatus()));
        costLabel.setText(String.format("Total Cost: $%.2f", order.getTotalCost()));
        employeeLabel.setText("Employee ID: " + order.getEmployeeId());
        earnedPointsLabel.setText(String.valueOf(order.getRewardPointsEarned()));
        lostPointsLabel.setText(String.valueOf(order.getRewardPointsUsed()));
        orderTypeLabel.setText(order.getOrderType());
    }

    private void openEmptyView(){
        ordersView.setVisible(false);
        emptyView.setVisible(true);
        orderDetailedView.setVisible(false);
    }

    private void openOrderView(){
        ordersView.setVisible(true);
        emptyView.setVisible(false);
        orderDetailedView.setVisible(false);
    }

    public void handleMenuButton(){
        SceneManager.getInstance().navigateTo("menu-page.fxml");
    }

    public void handleReceiptButton() {

    }
}
