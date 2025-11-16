package com.example.model;

import com.github.nylle.javafixture.Fixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class OrderValidatorTest {

    private Product createProduct(Fixture fixture, String unit, double quantity) {
        Product p = fixture.create(Product.class);
        try {
            p.setUnit(unit);
        } catch (NoSuchMethodError e) {
        }
        p.setQuantity(quantity);
        if (p.getProductCode() == null || p.getProductCode().trim().isEmpty()) {
            p.setProductCode("PRD-" + Math.abs(p.hashCode()));
        }
        return p;
    }

    @Test
    public void missingLastName_shouldThrow() {
        Fixture fixture = new Fixture();

        Order order = fixture.create(Order.class);
        Customer c = fixture.create(Customer.class);
        c.setFirstName("Jan");
        c.setLastName(""); // brak nazwiska
        order.setCustomer(c);

        List<Product> products = new ArrayList<>();
        products.add(createProduct(fixture, "kg", 100.0));
        order.setProducts(products);

        Assertions.assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(order));
    }

    @Test
    public void productsNull_shouldThrow() {
        Fixture fixture = new Fixture();

        Order order = fixture.create(Order.class);
        Customer c = fixture.create(Customer.class);
        c.setFirstName("Jan");
        c.setLastName("Kowalski");
        order.setCustomer(c);

        order.setProducts(null); // brak produktów

        Assertions.assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(order));
    }

    @Test
    public void productsEmpty_shouldThrow() {
        Fixture fixture = new Fixture();

        Order order = fixture.create(Order.class);
        Customer c = fixture.create(Customer.class);
        c.setFirstName("Jan");
        c.setLastName("Kowalski");
        order.setCustomer(c);

        order.setProducts(new ArrayList<>()); // pusta lista

        Assertions.assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(order));
    }

    @Test
    public void productQuantityZero_shouldThrow() {
        Fixture fixture = new Fixture();

        Order order = fixture.create(Order.class);
        Customer c = fixture.create(Customer.class);
        c.setFirstName("Ala");
        c.setLastName("Nowak");
        order.setCustomer(c);

        List<Product> products = new ArrayList<>();
        products.add(createProduct(fixture, "kg", 0.0)); // zero
        order.setProducts(products);

        Assertions.assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(order));
    }

    @Test
    public void productQuantityNegative_shouldThrow() {
        Fixture fixture = new Fixture();

        Order order = fixture.create(Order.class);
        Customer c = fixture.create(Customer.class);
        c.setFirstName("Ala");
        c.setLastName("Nowak");
        order.setCustomer(c);

        List<Product> products = new ArrayList<>();
        products.add(createProduct(fixture, "kg", -10.0)); // ujemna
        order.setProducts(products);

        Assertions.assertThrows(IllegalArgumentException.class, () -> OrderValidator.validate(order));
    }

    @Test
    public void totalExactlyTwoTons_shouldNotThrow() {
        Fixture fixture = new Fixture();

        Order order = fixture.create(Order.class);
        Customer c = fixture.create(Customer.class);
        c.setFirstName("Jan");
        c.setLastName("Kowalski");
        order.setCustomer(c);

        List<Product> products = new ArrayList<>();
        products.add(createProduct(fixture, "kg", 1000.0)); // 1 t
        products.add(createProduct(fixture, "kg", 1000.0)); // 1 t -> total = 2 t
        order.setProducts(products);

        Assertions.assertDoesNotThrow(() -> OrderValidator.validate(order));
    }
}
