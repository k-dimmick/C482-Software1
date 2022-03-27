package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**  FUTURE ENHANCEMENT (DELETE PART AND PRODUCT)
 *
 *     Ability to multi-select parts for deletion (even allowing it from a separate search result page), would increase ease of use - especially when talking about larger inventories.
 *     The scope of this program is great for a company that offers few parts/products, but is incredibly limiting for
 *     those larger companies that have in excess of 10 of each part or product, much less those that offer parts and products in the hundreds and thousands.
 */
public class FirstScreen implements Initializable {
    public TableView<Part> partTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partStockCol;
    public TableColumn partPricePerUnitCol;
    public TableView<Product> productTable;
    public TableColumn productIdCol;
    public TableColumn productNameCol;
    public TableColumn productStockCol;
    public TableColumn productPricePerUnitCol;
    public Button exit;
    public Button addPart;
    public Button modifyPart;
    public Button deletePart;
    public Button addProduct;
    public Button modifyProduct;
    public Button deleteProduct;
    public TextField partSearchField;
    public TextField productSearchField;


    /** This initializes the page, and sets up both tables with the correct column views.      */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set Part Table
        partTable.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Set Product Table
        productTable.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method enables the "Add Part" button and sets the stage for the "Add Part" screen.     */
    public void toAddParts(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        // root.setStyle("-fx-font-family: Times New Roman");
        Scene scene = new Scene(root, 600, 400);
        scene.getRoot().setStyle("-fx-font-family: 'serif'");
        stage.setTitle("Add Parts");
        stage.setScene(scene);
        stage.show();
    }

    /** This method enables the "Modify Part" button and sets the stage for the "Modify Part" screen. Additionally, it pushes the data for the selected part to that scene.     */
    public void toModifyParts(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        Parent modifyPart = loader.load();
        modifyPart.setStyle("-fx-font-family: Times New Roman");
        Scene  modifyPartScene = new Scene(modifyPart);

        //gets controller for Modify Part Screen
        ModifyPart controller = loader.getController();
        // adds selected part to Modify Part
        controller.initData(partTable.getSelectionModel().getSelectedItem());
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(modifyPartScene);
        window.setTitle("Modify Parts");
        window.show();
    }

    /** This closes the program.      */
    public void toExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    /** This method enables the "Add Product" button and sets the stage for the "Add Product" screen.     */
    public void toAddProducts(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: Times New Roman");
        Scene scene = new Scene (root, 850, 600);
        stage.setTitle("Add Products");
        stage.setScene(scene);
        stage.show();
    }

    /** This method enables the "Modify product" button and sets the stage for the "Modify product" screen. Additionally, it pushes the data for the selected product to that scene.     */
    public void toModifyProducts(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
        Parent modifyProduct = loader.load();
        modifyProduct.setStyle("-fx-font-family: Times New Roman");
        Scene  modifyProductScene = new Scene(modifyProduct);

        // Loads modify products screen
        ModifyProduct controller = loader.getController();
        // adds selected product to modify products screen
        controller.initData(productTable.getSelectionModel().getSelectedItem());
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(modifyProductScene);
        window.setTitle("Modify Product");
        window.show();
    }


    /** This method deletes the selected product from the inventory.  Confirmation pop up verifies that the user wants to remove the selected product, before removal. */
    public void deletePart(ActionEvent actionEvent) {
        // confirmation
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle("Parts");
        alert.setHeaderText("Delete Part Confirmation");
        alert.setContentText("Are you sure you want to delete this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            // actual removal of part
            Part SP = partTable.getSelectionModel().getSelectedItem();
            if (SP == null)
                return;
            Inventory.deletePart(SP);
        } else {
        }
    }

    /** This method deletes the selected product from the inventory.  Confirmation pop up verifies that the user wants to remove the selected product, before removal. */
    public void deleteProduct(ActionEvent actionEvent) {
        // validation
        if (!productTable.getSelectionModel().getSelectedItem().getAssociatedParts().isEmpty()){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("CANNOT DELETE");
            alert.setContentText("Cannot delete product with associated parts, please remove associated parts (modify) before deleting product.");
            alert.showAndWait();
            return;
        }

        // confirmation
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle("Products");
        alert.setHeaderText("Delete Product Confirmation");
        alert.setContentText("Are you sure you want to delete this product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Product SP = productTable.getSelectionModel().getSelectedItem();
            if (SP == null)
                return;
            Inventory.deleteProduct(SP);
            } else {
            }
    }

    /** This enables the search function on the part table. The search is case-sensitive, and will provide an error message if the search is invalid or does not have any applicable results.  */
    public void partSearch(ActionEvent actionEvent) {
        try {
            int searchInput = Integer.parseInt(partSearchField.getText());
            Part p = Inventory.lookupPart(Integer.parseInt(partSearchField.getText()));
            ObservableList<Part> idSearchList = FXCollections.observableArrayList();
            if (p != null) {
                idSearchList.add(p);
                partTable.setItems(idSearchList);
                // selects the first result after search
                partTable.getSelectionModel().select(0);
            // return and alert for failed ID search
            } else {
                partTable.setItems(idSearchList);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Failed Search");
                alert.setContentText("The part you searched for cannot be found");
                alert.showAndWait();
            }
        } catch (NumberFormatException e){
            ObservableList p = Inventory.lookupPart(partSearchField.getText());
            // displays result of string search
            partTable.setItems(Inventory.lookupPart(partSearchField.getText()));
            // selects the first result after search
            partTable.getSelectionModel().select(0);
            // return and alert for failed string search
            if (p.isEmpty()){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Failed Search");
                alert.setContentText("The part you searched for cannot be found");
                alert.showAndWait();
            }
        }
    }


    /** This enables the search function on the product table. The search is case-sensitive, and will provide an error message if the search is invalid or does not have any applicable results.  */
    public void productSearch(ActionEvent actionEvent) {
        try {
            int searchInput = Integer.parseInt(productSearchField.getText());
            Product p = Inventory.lookupProduct(Integer.parseInt(productSearchField.getText()));
            ObservableList<Product> idSearchList = FXCollections.observableArrayList();
            if (p != null) {
                idSearchList.add(p);
                productTable.setItems(idSearchList);
                // selects the first result after search
                productTable.getSelectionModel().select(0);
                // return and alert for failed ID search
            } else {
                productTable.setItems(idSearchList);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Failed Search");
                alert.setContentText("The product you searched for cannot be found");
                alert.showAndWait();
            }
        } catch (NumberFormatException e){
            ObservableList p = Inventory.lookupProduct(productSearchField.getText());
            // displays result of string search
            productTable.setItems(Inventory.lookupProduct(productSearchField.getText()));
            // selects the first result after search
            productTable.getSelectionModel().select(0);
            // return and alert for failed string search
            if (p.isEmpty()){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Failed Search");
                alert.setContentText("The product you searched for cannot be found");
                alert.showAndWait();
            }
        }
    }
}