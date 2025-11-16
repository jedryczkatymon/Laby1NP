package com.example.model;

import java.util.List;

public class Order {
    private Customer customer;
    private List<Product> products;

    // Domyślny konstruktor
    public Order() {
    }

    // Konstruktor z parametrami
    public Order(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
