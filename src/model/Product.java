package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/** This creates the Product Class */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private static int identifier = 1;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    public Product (int id, String name, double price, int stock, int min, int max) {
        this.id = Product.getIdentifier();
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        identifier ++;

    }

    public static int getIdentifier() { return identifier; }

    // get ID #
    public int getId() {
        return id;
    }
    // set ID #
    public void setId(int id) {
        this.id = id;
    }
    // get Name
    public String getName() {
        return name;
    }
    // set Name
    public void setName(String name) {
        this.name = name;
    }
    // get price
    public double getPrice() {
        return price;
    }
    //set price
    public void setPrice(double price) {
        this.price = price;
    }
    // get stock of product
    public int getStock() {
        return stock;
    }
    // set stock of product
    public void setStock(int stock) {
        this.stock = stock;
    }
    // get minimum stock
    public int getMin() {
        return min;
    }
    // get set minimum stock
    public void setMin(int min) {
        this.min = min;
    }
    // get maximum stock
    public int getMax() {
        return max;
    }
    // set maximum stock
    public void setMax(int max) {
        this.max = max;
    }

    // add an associated part
    public void addAssociatedParts(Part part){associatedParts.add(part);}

    // get the associated parts
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    //delete associated part from product
    public boolean deleteAssociatedPart(Part selectedAssociatedpart){
        for (int i=0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == selectedAssociatedpart.getId()){
                associatedParts.remove(i);
                return true;
            }
        }
        return false;
    }

}

