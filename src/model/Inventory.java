package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This creates the Inventory class. */
public class Inventory {

    // create List of Parts and Products
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product>allProducts = FXCollections.observableArrayList();

    //add part to parts list
    public static void addPart(Part part){allParts.add(part);}

    //add product to products list
    public static void addProduct(Product product){allProducts.add(product);}

    // search parts by ID
   public static Part lookupPart(int id){

       for (Part part : allParts){
           if (part.getId() == id)
               return part;
       }
        return null;
    }

    // lookup product by id number
    public static Product lookupProduct(int id) {

        for (Product product : allProducts){
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    // search Parts by name
    public static ObservableList<Part> lookupPart(String partialName){
        ObservableList<Part>namedParts = FXCollections.observableArrayList();

        for (Part np : allParts){
            if (np.getName().contains(partialName)){
                namedParts.add(np);
            }
        }
        return namedParts;
    }

    // search Products by name
    public static ObservableList<Product> lookupProduct(String partialName){
        ObservableList<Product>namedProducts = FXCollections.observableArrayList();

        for (Product np : allProducts){
            if(np.getName().contains(partialName)){
                namedProducts.add(np);
            }
        }
        return namedProducts;
    }

    // update all parts list when saving on modify screen
    public static void updatePart (int index,Part selectedPart) {
        allParts.set(index, selectedPart);
            }

    // update all products list when saving on modify screen
    public static void updateProduct (int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    // delete part
    public static boolean deletePart (Part selectedPart){
        allParts.remove(selectedPart);
        return true;
    }

    // delete product
    public static boolean deleteProduct (Product selectedProduct){
        allProducts.remove(selectedProduct);
        return true;
    }

    // get parts list
    public static ObservableList<Part> getAllParts(){return allParts;}

    // get products list
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
