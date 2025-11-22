package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.Cart;
import com.bobatea.momandpops.backend.models.CustomizedItem;
import com.bobatea.momandpops.backend.models.Item;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ItemPopupController {
    @FXML private VBox optionsContainer;
    @FXML private Label itemNameLabel;
    @FXML private TextField quantityField;
    @FXML private HBox errorContainer;

    // Sections
    @FXML private VBox sizeSection;
    @FXML private VBox colorSection;
    @FXML private VBox toppingsSection;
    @FXML private VBox crustSection;

    // Containers for dynamic options
    @FXML private VBox sizeContainer;
    @FXML private VBox colorContainer;
    @FXML private VBox toppingsContainer;
    @FXML private VBox crustContainer;

    private Item item;
    private int quantity = 1;
    private ToggleGroup sizeToggleGroup = new ToggleGroup();
    private ToggleGroup colorToggleGroup = new ToggleGroup();
    private ToggleGroup crustToggleGroup = new ToggleGroup();

    public void initialize(){
        testPopup();
    }

    public void setItem(Item item){
        this.item = item;
        configurePopup();
    }

    private void testPopup() {
        // Temporary test - show all sections
        itemNameLabel.setText("Test Pizza");

        // Add crust toppings
        addCrustOption("Thin (+$1.50)");
        addCrustOption("Thicc (+$1.00)");
        addCrustOption("NOODLESSSS (+$1.00)");

        // Add test sizes
        addSizeOption("Small - $10.99");
        addSizeOption("Medium - $12.99");
        addSizeOption("Large - $14.99");

        // Add test colors
        addColorOption("Red");
        addColorOption("Blue");
        addColorOption("Green");

        // Add test toppings
        addToppingOption("Pepperoni (+$1.50)");
        addToppingOption("Mushrooms (+$1.00)");
        addToppingOption("Olives (+$1.00)");


    }

    private void addSizeOption(String sizeText) {
        RadioButton rb = new RadioButton(sizeText);
        rb.setToggleGroup(sizeToggleGroup);
        sizeContainer.getChildren().add(rb);
    }

    private void addCrustOption(String crustText) {
        RadioButton rb = new RadioButton(crustText);
        rb.setToggleGroup(crustToggleGroup);
        crustContainer.getChildren().add(rb);
    }

    private void addColorOption(String colorText) {
        RadioButton rb = new RadioButton(colorText);
        rb.setToggleGroup(colorToggleGroup);
        colorContainer.getChildren().add(rb);
    }

    private void addToppingOption(String toppingText) {
        CheckBox cb = new CheckBox(toppingText);
        toppingsContainer.getChildren().add(cb);
    }

    private void configurePopup(){
        itemNameLabel.setText(item.name);

        // Pull size options
        if(item.hasSizes){
            for(String size : item.getSizes().keySet()){
                addSizeOption(size + "+(" + item.getSizes().get(size) + ")");
            }
        }

        // Pull topping options
        if(item.hasToppings){
            for(String topping : item.getSizes().keySet()){
                addToppingOption(topping + "+(" + item.getSizes().get(topping) + ")");
            }
        }

        // Pull color options
        if(item.hasCrust){
            for(String crust : item.getSizes().keySet()){
                addCrustOption(crust + "+(" + item.getSizes().get(crust) + ")");
            }
        }

        // Pull color options
        if(item.hasColors){
            for(String color : item.getColors()){
                addColorOption(color);
            }
        }
    }

    public void handleAddToCart() {
        errorContainer.getChildren().removeAll();
        CustomizedItem cartItem = new CustomizedItem(item.name, Integer.parseInt(quantityField.getText()), item.getBasePrice());

        if(item.getType() == "MENU"){
            RadioButton selectedSize = (RadioButton) sizeToggleGroup.getSelectedToggle();
            RadioButton selectedCrust = (RadioButton) crustToggleGroup.getSelectedToggle();
            if(selectedCrust == null){
                Label crustError = new Label();
                crustError.setText("Please pick a crust type.");
                errorContainer.getChildren().add(crustError);
                return;
            } else if(selectedSize == null){
                Label sizeError = new Label();
                sizeError.setText("Please pick a size type.");
                errorContainer.getChildren().add(sizeError);
                return;
            } else {
                cartItem.setCrust(selectedCrust.getText());
                cartItem.setSize(selectedSize.getText());
            }
        } else if(item.getType() == "MERCH"){
            RadioButton selectedSize = (RadioButton) sizeToggleGroup.getSelectedToggle();
            RadioButton selectedColor = (RadioButton) colorToggleGroup.getSelectedToggle();
            if(selectedColor == null){
                Label colorError = new Label();
                colorError.setText("Please pick a color.");
                errorContainer.getChildren().add(colorError);
                return;
            } else if(selectedSize == null){
                Label sizeError = new Label();
                sizeError.setText("Please pick a size type.");
                errorContainer.getChildren().add(sizeError);
                return;
            } else {
                cartItem.setColor(selectedColor.getText());
                cartItem.setSize(selectedSize.getText());
            }
        }

        for(Node node : toppingsContainer.getChildren()){
            if(node instanceof CheckBox){
                // Add the topping to the customized item with the right price
                cartItem.addSelectedTopping(((CheckBox) node).getText(), item.getToppings().get(((CheckBox) node).getText()));
            }
        }
        Cart.getInstance().addItem(cartItem);
        
    }

    @FXML
    public void handleCancel() {
        System.out.println("Cancelling item config");
        closePopup();
    }

    public void decreaseQuantity() {
        if (quantity > 1){
        quantity--;
        quantityField.setText(String.valueOf(quantity));
        }
    }

    public void increaseQuantity() {
        quantity++;
        quantityField.setText(String.valueOf(quantity));
    }

    private void closePopup(){
        Stage stage = (Stage) quantityField.getScene().getWindow();
        stage.close();
    }
}
