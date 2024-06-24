package ua.edu.ukma.dto;

import java.io.Serializable;

public class ProductPriceRequest implements Serializable {
    private int productId;
    private double price;

    public ProductPriceRequest(int productId, double price) {
        this.productId = productId;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }
}
