package ua.edu.ukma.dto;

import java.io.Serializable;

public class ProductAmountRequest implements Serializable {
    private int productId;
    private int amount;

    public ProductAmountRequest(int productId, int amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }
}
