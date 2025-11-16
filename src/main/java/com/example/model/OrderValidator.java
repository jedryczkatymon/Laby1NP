package com.example.model;

public class OrderValidator {

    public static void validate(Order order) {

        if (order.getCustomer().getFirstName() == null || order.getCustomer().getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }
        if (order.getCustomer().getLastName() == null || order.getCustomer().getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }

        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }
        if (order.getProducts().size() > 9) {
            throw new IllegalArgumentException("Order cannot contain more than 9 products.");
        }

        double totalInTons = 0.0;

        for (Product p : order.getProducts()) {

            if (!p.getUnit().equals("g") && !p.getUnit().equals("kg") && !p.getUnit().equals("t")) {
                throw new IllegalArgumentException("Invalid unit: " + p.getUnit());
            }

            if (p.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be positive for product: " + p.getProductCode());
            }

            double quantityTons;
            switch (p.getUnit()) {
                case "g":
                    quantityTons = p.getQuantity() / 1_000_000.0;
                    break;
                case "kg":
                    quantityTons = p.getQuantity() / 1000.0;
                    break;
                case "t":
                    quantityTons = p.getQuantity();
                    break;
                default:
                    quantityTons = 0.0;
                    break;
            }

            totalInTons += quantityTons;
        }

        if (totalInTons > 2.0) {
            throw new IllegalArgumentException("Total product quantity exceeds 2 tons.");
        }
    }
}