package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/** FUTURE ENHANCEMENT
 * The search function is great, but to increase useability, the ability to have the search results populate as the user is typing, also to have the search not be case-sensitive, would be great.
 * Case sensitivity will limit the users to know the exact way that a product or part was input into the system - which is immensely challenging the larger the inventory is
 */

public class AddProduct implements Initializable {
    public TableView allPartTable;
    public TableColumn allIdCol;
    public TableColumn allNameCol;
    public TableColumn allStockCol;
    public TableColumn allPerUnitCol;
    public TableView associatedPartTable;
    public TableColumn asscIdCol;
    public TableColumn asscNameCol;
    public TableColumn asscStockCol;
    public TableColumn asscPerUnitCol;
    public Button removeAssociated;
    public Button save;
    public Button cancel;
    public Button addAssociated;
    public TextField addId;
    public TextField addName;
    public TextField addStock;
    public TextField addPrice;
    public TextField addMax;
    public TextField addMin;
    public TextField addSearch;


    private ObservableList<Part> placeHolder = FXCollections.observableArrayList();
    private ObservableList<Part> allPartPH = FXCollections.observableArrayList(Inventory.getAllParts());


    /** This method initializes the page. This is the first method that is called, and formats the tables to be functional. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set All Part Table
        allPartTable.setItems(allPartPH);
        allIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Set Associated Part Table
        associatedPartTable.setItems(FXCollections.observableArrayList());
        asscIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        asscNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        asscPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        asscStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }

    /** This method cancels the screen. It deletes all entered (but not saved) data and returns to the main screen.  */
    public void cancelToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: Times New Roman");
        Scene scene = new Scene(root, 850, 600);
        stage.setTitle("Back to First Screen");
        stage.setScene(scene);
        stage.show();
    }


    /** This method removes an associated part from the bottom table. Confirmation pop up verifies that the user wants to remove the associated part, before removal. */
    public void removeAsscPart(ActionEvent actionEvent) {
        // confirmation
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Remove Associated Part Confirmation");
        alert.setContentText("Are you sure you want to remove this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part SP = (Part) associatedPartTable.getSelectionModel().getSelectedItem();
            if (SP == null) {
                return;
            } else {
                allPartPH.add(SP);
                placeHolder.remove(SP);
                allPartTable.setItems(allPartPH);
                associatedPartTable.setItems(placeHolder);
            }
        }
    }

    /** This method checks if input is an integer. This is accomplished by checking if the user input is parsable.  */
    private boolean intCheck (String string){
        try {
            int searchInput = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }


    /** This method checks if input is a double. This is accomplished by checking if the user input is parsable. */
    private boolean doubleCheck (String string){
        try {
            double searchInput = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }


    /** This method validates all user input and saves the product to the observable list. This method also pushes alerts when input is invalid; if valid, it will save and return to main screen. */
    public void saveAdd(ActionEvent actionEvent) throws IOException {
        // validation for correct input
        // checking that maximum is a number
        if (!intCheck(addMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Maximum must be a number");
            alert.showAndWait();
            return;
        }
        // checking that minimum is a number
        if (!intCheck(addMin.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Minimum must be a number");
            alert.showAndWait();
            return;
        }

        // checking that stock is a number
        if (!intCheck(addStock.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Inventory/Stock must be a number");
            alert.showAndWait();
            return;
        }

        // checking that price is a number
        if (!doubleCheck(addPrice.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Price must be a number");
            alert.showAndWait();
            return;
        }

        // checking that name is a text string
        if (intCheck(addName.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Name must be a name");
            alert.showAndWait();
            return;
        }


        // validation for stock to fall between minimum and maximum
        if (Integer.parseInt(addMin.getText()) > Integer.parseInt(addStock.getText()) || Integer.parseInt(addStock.getText()) > Integer.parseInt(addMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Inventory must be greater than minimum and less than maximum.");
            alert.showAndWait();
            return;
        }
        // validation for min to be less than maximum
        if (Integer.parseInt(addMin.getText()) > Integer.parseInt(addMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Maximum must be greater than minimum");
            alert.showAndWait();
            return;
        }


        Product pH = new Product(
                Product.getIdentifier(),
                addName.getText(),
                Double.parseDouble(addPrice.getText()),
                Integer.parseInt(addStock.getText()),
                Integer.parseInt(addMin.getText()),
                Integer.parseInt(addMax.getText()));

        for (Part p : placeHolder){
            pH.addAssociatedParts(p);
        }

        Inventory.addProduct(pH);

// return to main menu
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: Times New Roman");
        Scene scene = new Scene(root, 850, 600);
        stage.setTitle("Back to First Screen");
        stage.setScene(scene);
        stage.show();
    }


    /** This method adds an associated part from the upper table (which shows all parts in the Inventory) to the lower table (which is the specified product's associated parts list).      */
    public void addAsscPart(ActionEvent actionEvent) {

        Part SP = (Part) allPartTable.getSelectionModel().getSelectedItem();
        if (SP == null) {
            return;
        } else {
            placeHolder.add(SP);
            allPartPH.remove(SP);
            associatedPartTable.setItems(placeHolder);
        }


    }


    /** This enables the search function on the page. The search is case-sensitive, and will provide an error message if the search is invalid or does not have any applicable results.  */
    public void searchParts(ActionEvent actionEvent) {
        try {
            int searchInput = Integer.parseInt(addSearch.getText());
            Part p = Inventory.lookupPart(Integer.parseInt(addSearch.getText()));
            ObservableList<Part> idSearchList = FXCollections.observableArrayList();
            if (p != null) {
                idSearchList.add(p);
                allPartTable.setItems(idSearchList);
                // selects the first result after search
                allPartTable.getSelectionModel().select(0);
                // return and alert for failed ID search
            } else {
                allPartTable.setItems(idSearchList);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Failed Search");
                alert.setContentText("The part you searched for cannot be found");

                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            ObservableList p = Inventory.lookupPart(addSearch.getText());
            // displays result of string search
            allPartTable.setItems(Inventory.lookupPart(addSearch.getText()));
            // selects the first result after search
            allPartTable.getSelectionModel().select(0);
            // return and alert for failed string search
            if (p.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Failed Search");
                alert.setContentText("The part you searched for cannot be found");
                alert.showAndWait();
            }
        }
    }
}