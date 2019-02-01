package com.archi.tithetracker;

public class Product {

    private String input, date;
    private int id;

    public Product(String input, String date) {
        this.setInput(input);
        this.setDate(date);
        this.setId(id);
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}