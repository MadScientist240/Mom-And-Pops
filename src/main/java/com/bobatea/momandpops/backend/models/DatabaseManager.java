package com.bobatea.momandpops.backend.models;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseManager {

    private static final Path DATA_DIR       = Paths.get("data");
    private static final Path CUSTOMERS_FILE = DATA_DIR.resolve("customers.txt");

    private static List<Item> itemsCache;

    static {
        try {
            if (!Files.exists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            if (!Files.exists(CUSTOMERS_FILE)) {
                Files.createFile(CUSTOMERS_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== CUSTOMER METHODS =====

    public static Optional<Customer> findCustomerByPhone(String phone) {
        try {
            return Files.lines(CUSTOMERS_FILE)
                    .map(DatabaseManager::parseCustomer)
                    .filter(Objects::nonNull)
                    .filter(c -> c.getPhone().equals(phone))
                    .findFirst();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static boolean validateCustomerLogin(String phone, String password) {
        return findCustomerByPhone(phone)
                .map(c -> c.getPassword().equals(password))
                .orElse(false);
    }

    public static Customer createCustomer(String name,
                                          String phone,
                                          String email,
                                          String address,
                                          String password) {
        int newId = getNextCustomerId();
        Customer c = new Customer(newId, name, phone, email, address, password, 0);
        saveCustomer(c);
        return c;
    }

    private static int getNextCustomerId() {
        try {
            return Files.lines(CUSTOMERS_FILE)
                    .map(DatabaseManager::parseCustomer)
                    .filter(Objects::nonNull)
                    .mapToInt(Customer::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private static Customer parseCustomer(String line) {
        if (line == null || line.isBlank()) return null;
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;

        try {
            int id           = Integer.parseInt(parts[0]);
            String name      = parts[1];
            String phone     = parts[2];
            String email     = parts[3];
            String address   = parts[4];
            String password  = parts[5];
            int rewardPoints = Integer.parseInt(parts[6]);

            return new Customer(id, name, phone, email, address, password, rewardPoints);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveCustomer(Customer c) {
        String line = String.join("|",
                String.valueOf(c.getId()),
                c.getName(),
                c.getPhone(),
                c.getEmail(),
                c.getAddress(),
                c.getPassword(),
                String.valueOf(c.getRewardPoints())
        );

        try {
            Files.writeString(
                    CUSTOMERS_FILE,
                    line + System.lineSeparator(),
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== MENU + MERCH ITEMS =====

    public static List<Item> getAllItems() {
        if (itemsCache == null) {
            itemsCache = new ArrayList<>();

            // --- Pizza Base ---
            Item pizzaBase = new Item(
                    "Custom Pizza",
                    1,
                    "Build your own pizza",
                    8.99,
                    "MENU",   // type
                    true,     // hasSizes
                    "/images/pizza.png"
            );

            // Crusts
            pizzaBase.addOption("Crust", "Thin", 0.00);
            pizzaBase.addOption("Crust", "Regular", 0.50);
            pizzaBase.addOption("Crust", "Pan", 1.00);

            // Sizes
            pizzaBase.addSize("Small", 0.00);
            pizzaBase.addSize("Medium", 2.00);
            pizzaBase.addSize("Large", 4.00);
            pizzaBase.addSize("Extra Large", 6.00);

            // Sauces
            pizzaBase.addOption("Sauce", "Marinara", 0.00);
            pizzaBase.addOption("Sauce", "Alfredo", 0.50);

            // Toppings (Veg + Meat)
            pizzaBase.addTopping("Cheese", 1.00);
            pizzaBase.addTopping("Onion", 0.75);
            pizzaBase.addTopping("Pineapple", 1.00);
            pizzaBase.addTopping("Jalapeno", 0.75);
            pizzaBase.addTopping("Olives", 1.00);
            pizzaBase.addTopping("Mushroom", 1.00);
            pizzaBase.addTopping("Pepperoni", 1.50);
            pizzaBase.addTopping("Chicken", 1.75);
            pizzaBase.addTopping("Italian Sausage", 1.75);
            pizzaBase.addTopping("Bacon", 1.75);
            pizzaBase.addTopping("Beef", 2.00);

            itemsCache.add(pizzaBase);

            // --- Beverages ---
            itemsCache.add(new Item("Coke", 2, "Soft drink", 1.99, "MENU", false, "/images/coke.png"));
            itemsCache.add(new Item("Sprite", 3, "Soft drink", 1.99, "MENU", false, "/images/sprite.png"));
            itemsCache.add(new Item("Fanta", 4, "Soft drink", 1.99, "MENU", false, "/images/fanta.png"));

            // --- Desserts ---
            itemsCache.add(new Item("Chocolate Chunk Cookie", 5, "Dessert", 2.99, "MENU", false, "/images/cookie.png"));
            itemsCache.add(new Item("Cinnamon Roll", 6, "Dessert", 3.49, "MENU", false, "/images/cinnamon.png"));
            itemsCache.add(new Item("Molten Lava Cake", 7, "Dessert", 4.99, "MENU", false, "/images/lava.png"));

            // --- Merch Example ---
            Item shirt = new Item(
                    "Mom & Pop's T-Shirt",
                    100,
                    "Branded shirt",
                    10.00,
                    "MERCH",
                    true,
                    "/images/tshirt.png"
            );
            shirt.addColor("Red");
            shirt.addColor("Black");
            shirt.addColor("White");
            shirt.addSize("S", 0.00);
            shirt.addSize("M", 2.00);
            shirt.addSize("L", 5.00);
            itemsCache.add(shirt);
        }
        return itemsCache;
    }

    public static List<Item> getMenuItems() {
        return getAllItems().stream()
                .filter(i -> "MENU".equals(i.getType()))
                .collect(Collectors.toList());
    }

    public static List<Item> getMerchItems() {
        return getAllItems().stream()
                .filter(i -> "MERCH".equals(i.getType()))
                .collect(Collectors.toList());
    }

    public static List<Item> getMenuItemsByCategory(String categoryName) {
        String lower = categoryName.toLowerCase();
        return getMenuItems().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(lower)
                          || i.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // ===== ORDER STUB =====
    public static List<Order> getOrdersByCustomer(Customer customer) {
        // TODO: implement persistence of orders
        return new ArrayList<>();
    }
}


