package com.app.jem.stockmanager.domain;

/**
 * Created by jem on 2017/1/23.
 */

public class goods_item {

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String model;

    public int price;

    public int number;
}
