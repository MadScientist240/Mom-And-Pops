package com.bobatea.momandpops;

import com.bobatea.momandpops.backend.models.Item;
import com.bobatea.momandpops.frontend.SceneManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Starting database sanity check...");

        try {
            // 1️⃣ Test connection
            if (DatabaseManager.getConnection() != null) {
                System.out.println("✅ Connection established successfully.");
            }

            DatabaseManager.createTables();
            addTestItems();
        } catch (Exception e) {
            System.out.println("❌ Database sanity check failed!");
            e.printStackTrace();
        }

        primaryStage.setTitle("Mom & Pops GUI");
        primaryStage.setResizable(false);

        SceneManager.getInstance().initialize(primaryStage);
        SceneManager.getInstance().navigateTo("home-page.fxml");

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void stop(){
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addTestItems() {
        if (DatabaseManager.getAllItems().isEmpty()) {
            // Simple items without attributes for now
            Item pepperoni = new Item("Pepperoni Pizza", 1, "Classic pepperoni with mozzarella", 12.99, true, true, "com/bobatea/momandpops/images/mom.png");
            Item margherita = new Item("Margherita Pizza", 2, "Fresh tomatoes and basil", 10.99, true, true, "com/bobatea/momandpops/images/mom.png");
            Item brownie = new Item("Chocolate Brownie", 3, "Warm chocolate brownie", 5.99, true, false, "com/bobatea/momandpops/images/mom.png");
            Item soda = new Item("Soda", 4, "Cold refreshing soda", 2.99, true, true, "com/bobatea/momandpops/images/mom.png");

            DatabaseManager.addItem(pepperoni);
            DatabaseManager.addItem(margherita);
            DatabaseManager.addItem(brownie);
            DatabaseManager.addItem(soda);

            System.out.println("Test items added to database!");
        }
    }
}