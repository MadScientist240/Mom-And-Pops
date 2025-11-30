package com.bobatea.momandpops.frontend.controllers;

import com.bobatea.momandpops.backend.models.Cart;
import com.bobatea.momandpops.backend.models.CustomizedItem;
import com.bobatea.momandpops.backend.models.Item;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ItemPopupController {
    @FXML private VBox optionsContainer;
    @FXML private Label itemNameLabel;
    @FXML private TextField quantityField;
    @FXML private VBox errorContainer;

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

    public void initialize(){}

    public void setItem(Item item){
        this.item = item;
        configurePopup();
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

        // Size options
        if(item.hasSizes){
            for(String size : item.getSizes().keySet()){
                addSizeOption(size + " +(" + item.getSizes().get(size) + ")");
            }
        } else {
            setVboxVisibility(sizeSection, false);
        }

        // Topping options
        if(item.hasToppings){
            for(String topping : item.getToppings().keySet()){
                addToppingOption(topping + " +(" + item.getToppings().get(topping) + ")");
            }
        } else {
            setVboxVisibility(toppingsSection, false);
        }

        // Crust options
        if(item.hasCrust){
            for(String crust : item.getCrusts().keySet()){
                addCrustOption(crust + " +(" + item.getCrusts().get(crust) + ")");
            }
        } else {
            setVboxVisibility(crustSection, false);
        }

        // Color options
        if(item.hasColors){
            for(String color : item.getColors()){
                addColorOption(color);
            }
        } else {
            setVboxVisibility(colorSection, false);
        }
    }

    public void handleAddToCart() {
        errorContainer.getChildren().clear();
        CustomizedItem cartItem = new CustomizedItem(item.name, item.getBasePrice());

        if ("MENU".equals(item.getType())) {
            RadioButton selectedSize = (RadioButton) sizeToggleGroup.getSelectedToggle();
            RadioButton selectedCrust = (RadioButton) crustToggleGroup.getSelectedToggle();

            if(selectedCrust == null){
                setErrorLabel("Please pick a crust type");
                return;
            } else if(selectedSize == null){
                setErrorLabel("Please pick a size");
                return;
            } else {
                String crustString = stripSelectionName(selectedCrust.getText());
                cartItem.setCrust(crustString, item.getCrusts().get(crustString));

                String sizeString = stripSelectionName(selectedSize.getText());
                cartItem.setSize(sizeString, item.getSizes().get(sizeString));
            }

            boolean toppingChosen = false;
            for(Node node : toppingsContainer.getChildren()){
                if(node instanceof CheckBox cb && cb.isSelected()){
                    toppingChosen = true;
                    String strippedToppingName = stripSelectionName(cb.getText());
                    cartItem.addSelectedTopping(
                            strippedToppingName,
                            item.getToppings().get(strippedToppingName)
                    );
                }
            }
            if(item.hasToppings && !toppingChosen){
                setErrorLabel("Please pick at least 1 topping");
                return;
            }

        } else if ("MERCH".equals(item.getType())) {
            RadioButton selectedSize = (RadioButton) sizeToggleGroup.getSelectedToggle();
            RadioButton selectedColor = (RadioButton) colorToggleGroup.getSelectedToggle();

            if(selectedColor == null){
                setErrorLabel("Please pick a color");
                return;
            } else if(selectedSize == null){
                setErrorLabel("Please pick a size");
                return;
            } else {
                cartItem.setColor(selectedColor.getText());

                String sizeString = stripSelectionName(selectedSize.getText());
                cartItem.setSize(sizeString, item.getSizes().get(sizeString));
            }
        }

        cartItem.setQuantity(Integer.parseInt(quantityField.getText()));
        Cart.getInstance().addItem(cartItem);
        closePopup();
    }

    @FXML
    public void handleCancel() {
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

    /*
     *  Misc. helper functions
     */
    private void setVboxVisibility(VBox vb, boolean visibility){
        vb.setVisible(visibility);
        vb.setManaged(visibility);
    }

    private void setErrorLabel(String message){
        Label error = new Label();
        error.setText(message);
        errorContainer.getChildren().add(error);
    }

    private String stripSelectionName(String selectionText){
        int regexIndex = selectionText.indexOf("+");
        return selectionText.substring(0, regexIndex - 1);
    }
}
