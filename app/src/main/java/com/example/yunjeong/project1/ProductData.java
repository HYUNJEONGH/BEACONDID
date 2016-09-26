package com.example.yunjeong.project1;

/**
 * Created by YunJeong on 2016-09-05.
 */
public class ProductData {
    String productID;
    String storeID;
    String productName;
    String imageurl;
    int price;
    int amount;
    int bestMenu;

    public ProductData(String productID, String storeID, String productName, int price, String imageurl, int amount, int bestMenu) {
        this.productID = productID;
        this.storeID = storeID;
        this.productName = productName;
        this.price = price;
        this.imageurl = imageurl;
        this.amount = amount;
        this.bestMenu = bestMenu;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBestMenu() {
        return bestMenu;
    }

    public void setBestMenu(int bestMenu) {
        this.bestMenu = bestMenu;
    }
}
