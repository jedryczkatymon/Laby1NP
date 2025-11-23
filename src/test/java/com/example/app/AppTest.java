package com.example.app;

import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.Product;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    void testGenerateOrderNumber() throws Exception {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("+48 600 123 456");

        Product p1 = new Product();
        p1.setProductCode("PRD-001");
        p1.setQuantity(10.0);
        p1.setUnit("kg");

        Product p2 = new Product();
        p2.setProductCode("PRD-310");
        p2.setQuantity(500.0);
        p2.setUnit("g");

        Product p3 = new Product();
        p3.setProductCode("PRD-210");
        p3.setQuantity(1.0);
        p3.setUnit("t");

        List<Product> products = Arrays.asList(p1, p2, p3);

        Order order = new Order(customer, products);

        String result = App.generateOrderNumber(order);

        assertEquals("ORD-13E27297139B", result);
    }
}
