package com.example.shoppinglist.Models;

public class Item {
    private String name, personName,brand, amount, itemId;
    private boolean isPriority, bought;

    public Item(String name, String personName){
        this.name=name;
        this.personName=personName;
    }

    public Item(){

    }

    public Item( boolean bought, String name, String personName ,boolean isPriority){
        this.name=name;
        this.personName=personName;
        this.bought=bought;
        this.isPriority=isPriority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
