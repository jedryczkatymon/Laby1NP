package com.example.app;

import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.Product;
import com.github.nylle.javafixture.Fixture;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void testGenerateOrderNumberWithRandomOrders() throws Exception {
        Fixture fixture = new Fixture();

        // Pobieramy klasę App i prywatną statyczną metodę przez reflection
        Class<?> appClass = Class.forName("com.example.app.App");
        Method method = appClass.getDeclaredMethod("generateOrderNumber", Order.class);
        method.setAccessible(true);

        // Wygeneruj np. 5 różnych zamówień
        for (int i = 0; i < 5; i++) {
            Order order = fixture.create(Order.class);

            // Upewniamy się, że order ma klienta
            if (order.getCustomer() == null) {
                order.setCustomer(fixture.create(Customer.class));
            }

            // Upewniamy się, że order ma produkty
            if (order.getProducts() == null || order.getProducts().isEmpty()) {
                List<Product> products = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    products.add(fixture.create(Product.class));
                }
                order.setProducts(products);
            }

            // Wywołanie prywatnej statycznej metody
            String actualOrderNumber = (String) method.invoke(null, order);

            // Generujemy oczekiwany wynik MD5 w teście
            StringBuilder sb = new StringBuilder();
            sb.append(order.getCustomer().getFirstName()).append("|")
              .append(order.getCustomer().getLastName()).append("|")
              .append(order.getCustomer().getEmail()).append("|");
            order.getProducts().forEach(p ->
                    sb.append(p.getProductCode()).append(":").append(p.getQuantity()).append(",")
            );

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(sb.toString().getBytes());
            StringBuilder hex = new StringBuilder();
            for (int j = 0; j < 6 && j < digest.length; j++) {
                hex.append(String.format("%02X", digest[j]));
            }
            String expectedOrderNumber = "ORD-" + hex;

            // Porównanie wyniku
            assertEquals(expectedOrderNumber, actualOrderNumber, "Order number mismatch for random order #" + (i + 1));
        }
    }
}
