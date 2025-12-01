package com.bobatea.momandpops.backend.models;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class DatabaseManager {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path CUSTOMERS_FILE = DATA_DIR.resolve("customers.text");
    private static final Path ITEMS_FILE = DATA_DIR.resolve("items.text");
    private static final Path ORDERS_FILE = DATA_DIR.resolve("orders.text");

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

    public static Optional<Customer> findCustomerByPhone(String phone) {
        if (!Files.exists(CUSTOMERS_FILE)) {
            return Optional.empty();
        }

        try {
            List<String> lines = Files.readAllLines(CUSTOMERS_FILE);
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Customer c = parseCustomer(line);
                if (c != null && c.getPhone().equals(phone)) {
                    return Optional.of(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static boolean validateCustomerLogin(String phone, String password) {
        Optional<Customer> result = findCustomerByPhone(phone);
        if (result.isPresent()) {
            return result.get().getPassword().equals(password);
        }
        return false;
    }

    public static Customer createCustomer(String name, String phone, String email, String address, String password) {
        int newId = getNextCustomerId();
        Customer c = new Customer(newId, name, phone, email, address, password, 0);
        saveCustomer(c);
        return c;
    }

    private static int getNextCustomerId() {
        int maxId = 0;

        if (!Files.exists(CUSTOMERS_FILE)) {
            return 1;
        }

        try {
            List<String> lines = Files.readAllLines(CUSTOMERS_FILE);
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Customer c = parseCustomer(line);
                if (c != null && c.getId() > maxId) {
                    maxId = c.getId();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxId + 1;
    }

    private static Customer parseCustomer(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;

        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String phone = parts[2].trim();
            String email = parts[3].trim();
            String address = parts[4].trim();
            String password = parts[5].trim();
            int rewardPoints = Integer.parseInt(parts[6].trim());

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

    public static List<Item> getAllItems() {
        if (itemsCache == null) {
            itemsCache = new ArrayList<>();

            if (Files.exists(ITEMS_FILE)) {
                try {
                    List<String> lines = Files.readAllLines(ITEMS_FILE);
                    for (String line : lines) {
                        line = line.trim();
                        if (line.isEmpty()) continue;

                        Item item = parseItem(line);
                        if (item != null) {
                            itemsCache.add(item);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return itemsCache;
    }

    private static Item parseItem(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;

        try {
            String name = parts[0].trim();
            int itemId = Integer.parseInt(parts[1].trim());
            String description = parts[2].trim();
            double startingPrice = Double.parseDouble(parts[3].trim());
            boolean isMenuItem = Boolean.parseBoolean(parts[4].trim());
            boolean hasSizes = Boolean.parseBoolean(parts[5].trim());
            String imagePath = parts[6].trim();

            return new Item(name, itemId, description, startingPrice, isMenuItem, hasSizes, imagePath);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Item> getMenuItems() {
        List<Item> menuItems = new ArrayList<>();
        List<Item> all = getAllItems();

        for (Item item : all) {
            if ("MENU".equals(item.getType())) {
                menuItems.add(item);
            }
        }

        return menuItems;
    }

    public static List<Item> getMerchItems() {
        List<Item> merchItems = new ArrayList<>();
        List<Item> all = getAllItems();

        for (Item item : all) {
            if ("MERCH".equals(item.getType())) {
                merchItems.add(item);
            }
        }

        return merchItems;
    }

    public static List<Item> getMenuItemsByCategory(String categoryName) {
        String lower = categoryName.toLowerCase();
        List<Item> result = new ArrayList<>();
        List<Item> menuItems = getMenuItems();

        for (Item item : menuItems) {
            String desc = item.getDescription().toLowerCase();
            String name = item.name.toLowerCase();
            if (desc.contains(lower) || name.contains(lower)) {
                result.add(item);
            }
        }

        return result;
    }

    private static Order parseOrder(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 13) return null;

        try {
            int id = Integer.parseInt(parts[0].trim());
            int customerId = Integer.parseInt(parts[1].trim());

            String empStr = parts[2].trim();
            String receiptStr = parts[3].trim();

            Integer employeeId = empStr.equalsIgnoreCase("null") ? null : Integer.parseInt(empStr);
            Integer receiptId = receiptStr.equalsIgnoreCase("null") ? null : Integer.parseInt(receiptStr);

            double totalCost = Double.parseDouble(parts[4].trim());
            String paymentMethod = parts[5].trim();
            String orderType = parts[6].trim();
            boolean complete = Boolean.parseBoolean(parts[7].trim());
            String deliveryAddr = parts[8].trim();
            String deliveryInst = parts[9].trim();
            String arrivalTime = parts[10].trim();
            int rewardEarned = Integer.parseInt(parts[11].trim());
            int rewardUsed = Integer.parseInt(parts[12].trim());

            return new Order(id, customerId, employeeId, receiptId, totalCost,
                    paymentMethod, orderType, complete, deliveryAddr,
                    deliveryInst, arrivalTime, rewardEarned, rewardUsed);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Order> getOrdersByCustomer() {
        Customer current = UserSession.getInstance().getCurrentCustomer();
        if (current == null) {
            return Collections.emptyList();
        }

        int customerId = current.getId();
        List<Order> results = new ArrayList<>();

        if (!Files.exists(ORDERS_FILE)) {
            return results;
        }

        try {
            List<String> lines = Files.readAllLines(ORDERS_FILE);
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Order order = parseOrder(line);
                if (order != null && order.getCustomerId() == customerId) {
                    results.add(order);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}



