package com.example.yunjeong.project1;

/**
 * Created by YunJeong on 2016-09-08.
 */
public class CartItemData {
    String cartId;
    String itemId;
    String itemName;
    int itemAmount;
    int itemPrice;
    String userId;

    public CartItemData(String cartId, String itemId, String itemName, int itemAmount, int itemPrice, String userId) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this.itemPrice = itemPrice;
        this.userId = userId;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
