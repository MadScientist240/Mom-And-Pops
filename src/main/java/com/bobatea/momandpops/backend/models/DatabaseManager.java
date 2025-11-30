package com.bobatea.momandpops.backend.models;

import com.bobatea.momandpops.backend.models.Customer;
import com.bobatea.momandpops.backend.models.Item;

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

            // --- Example menu items (enough to demo) ---

            // Pizza
            Item cheesePizza = new Item(
                    "Cheese Pizza",
                    1,
                    "Classic cheese pizza",
                    10.99,
                    true,   // isMenuItem
                    true,   // hasSizes
                    "/com/bobatea/momandpops/images/cheesepizza.png"
            );
            cheesePizza.addTopping("Extra Cheese", 1.50);
            cheesePizza.addTopping("Pepperoni", 1.75);
            cheesePizza.addTopping("Mushroom", 1.00);
            itemsCache.add(cheesePizza);

            // Drink
            Item soda = new Item(
                    "Sprite (2L)",
                    2,
                    "Soft drinks",
                    1.99,
                    true,
                    false,
                    "/com/bobatea/momandpops/images/sprite.png"
            );
            itemsCache.add(soda);

            // Merch
            Item shirt = new Item(
                    "Mom & Pop's T-Shirt",
                    100,
                    "Branded shirt",
                    10.00,
                    false,   // merch
                    true,
                    "/com/bobatea/momandpops/images/t-shirt.png"
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
                          || i.name.toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}
