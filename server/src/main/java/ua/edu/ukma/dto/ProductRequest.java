package ua.edu.ukma.dto;

import java.io.Serializable;

public class ProductRequest implements Serializable {
    private int productId;

    public ProductRequest(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }
}
