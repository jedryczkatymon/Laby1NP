package com.example.app;

import com.example.model.Order;
import com.example.model.OrderWrapper;
import com.example.model.OrderValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        File propsFile = new File("src/main/resources/appsettings.properties");
        FileInputStream fis = new FileInputStream(propsFile);
        props.load(fis);
        fis.close();

        String envOrdersDir = System.getenv("ENV_ORDER_DIR");

        String ordersDir;
        if (envOrdersDir == null || envOrdersDir.trim().isEmpty()) {
            ordersDir = props.getProperty("orders.dir", ".");
        } else {
            ordersDir = envOrdersDir;
        }

        File dir = new File(ordersDir);

        ObjectMapper mapper = new ObjectMapper();

        File[] jsonFiles = dir.listFiles((d, name) ->
                name.toLowerCase().endsWith(".json")
        );

        if (jsonFiles == null) {
            System.err.println("Directory not found or not accessible: " + dir.getAbsolutePath());
            return;
        }

        for (File f : jsonFiles) {
            try {
                OrderWrapper wrapper = mapper.readValue(f, OrderWrapper.class);
                Order order = wrapper.getOrder();

                OrderValidator.validate(order);

                String orderNumber = generateOrderNumber(order);
                System.out.println("Processed file: " + f.getName() + " -> Order Number: " + orderNumber);

            } catch (Exception e) {
                System.err.println("Error in file " + f.getName() + ": " + e.getMessage());
            }
        }
    }

    static String generateOrderNumber(Order order) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        sb.append(order.getCustomer().getFirstName()).append("|");
        sb.append(order.getCustomer().getLastName()).append("|");
        sb.append(order.getCustomer().getEmail()).append("|");
        order.getProducts().forEach(p ->
            sb.append(p.getProductCode()).append(":").append(p.getQuantity()).append(",")
        );

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(sb.toString().getBytes());
        StringBuilder hex = new StringBuilder();

        for (int i = 0; i < 6 && i < digest.length; i++) {
            hex.append(String.format("%02X", digest[i]));
        }

        return "ORD-" + hex;
    }
}
