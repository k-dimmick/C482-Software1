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
import java.util.Set;
import java.util.stream.Collectors;


/** RUNTIME ERROR: NULL POINTER EXCEPTION
 *             While writing the save function (specifically to add the associated parts list on a modified product) numerous errors were generated, most noticeably, the Null Pointer Exception.
 *             After creating a break within the method here, and the "addAssociatedParts" within the Inventory, I was able to confirm that the starting issue, was that the mapped item was not actually
 *             pulling the correct data. After correcting that, the next issue presented that the for loop was being called, but then was never going within the actual if statement. The brackets I had intended
 *             on closing the for loop had been missed (user error) and had created a much larger for loop. After reviewing the curly braces more closely, I added the missing one, and knocked "Inventory.updateProduct(i, mPart);"
 *             OUTSIDE the for loop, resolving the runtime error.
 */
public class ModifyProduct implements Initializable {
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
    public TextField modifyProductId;
    public TextField modName;
    public TextField modStock;
    public TextField modPrice;
    public TextField modMax;
    public TextField modMin;
    public TextField search;
    public Product selectedP;

    private ObservableList<Part> placeHolder = FXCollections.observableArrayList();
    private ObservableList<Part> allPartPH = FXCollections.observableArrayList(Inventory.getAllParts());

    /** This method pre-populates the data into the user input fields. This is done by pulling the selected product, and parsing each variable to its corresponding text field.   */
    public void initData (Product selectedProduct){

        selectedP = selectedProduct;
        modifyProductId.setText(Integer.toString(selectedProduct.getId()));
        modName.setText(selectedProduct.getName());
        modPrice.setText(Double.toString(selectedProduct.getPrice()));
        modStock.setText(Integer.toString(selectedProduct.getStock()));
        modMin.setText(Integer.toString(selectedProduct.getMin()));
        modMax.setText(Integer.toString(selectedProduct.getMax()));
        this.placeHolder = FXCollections.observableArrayList(selectedP.getAssociatedParts());
        associatedPartTable.setItems(placeHolder);

    }

    /** This method initializes the page. This is the first method that is called. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set All Parts table
        allPartTable.setItems(Inventory.getAllParts());
        allIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        asscIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        asscNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        asscPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        asscStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }

    /** This method cancels the screen. It deletes all entered (but not saved) data and returns to the main screen.  */
    public void cancelToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: Times New Roman");
        Scene scene = new Scene(root, 850, 600);
        stage.setTitle("Back to First Screen");
        stage.setScene(scene);
        stage.show();
    }

    /** This enables the search function on the page. The search is case-sensitive, and will provide an error message if the search is invalid or does not have any applicable results.  */
    public void searchPart(ActionEvent actionEvent) {
        try {
            int searchInput = Integer.parseInt(search.getText());
            Part p = Inventory.lookupPart(Integer.parseInt(search.getText()));
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
            ObservableList p = Inventory.lookupPart(search.getText());
            // displays result of string search
            allPartTable.setItems(Inventory.lookupPart(search.getText()));
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

    /** This method validates all user input and saves the product to the observable list. It does this by first creating a placeholder product (mPart), it then updates the selected product in the Inventory with the information for mPart. */
    public void saveModifyProduct(ActionEvent actionEvent) throws IOException {
        // validation for correct input
        // checking that maximum is a number
        if (!intCheck(modMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Maximum must be a number");
            alert.showAndWait();
            return;
        }
        // checking that minimum is a number
        if (!intCheck(modMin.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Minimum must be a number");
            alert.showAndWait();
            return;
        }
        // checking that stock is a number
        if (!intCheck(modStock.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Inventory/Stock must be a number");
            alert.showAndWait();
            return;
        }

        // checking that price is a number
        if (!doubleCheck(modPrice.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Price must be a number");
            alert.showAndWait();
            return;
        }
        // checking that Name is a text string
        if (intCheck(modName.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Name must be a name");
            alert.showAndWait();
            return;
        }

        // validation for stock to fall between minimum and maximum
        if (Integer.parseInt(modMin.getText()) > Integer.parseInt(modStock.getText()) || Integer.parseInt(modStock.getText()) > Integer.parseInt(modMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Inventory must be greater than minimum and less than maximum.");
            alert.showAndWait();
            return;
        }
        // validation for min to be less than maximum
        if (Integer.parseInt(modMin.getText()) > Integer.parseInt(modMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Maximum must be greater than minimum");
            alert.showAndWait();
            return;
        }


        int pId = Integer.parseInt(modifyProductId.getText());
        // update product information
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            if (Inventory.getAllProducts().get(i).getId() == pId) {
                Product mPart = new Product(
                        1,
                        modName.getText(),
                        Double.parseDouble(modPrice.getText()),
                        Integer.parseInt(modStock.getText()),
                        Integer.parseInt(modMin.getText()),
                        Integer.parseInt(modMax.getText()));
                mPart.setId(pId);
                for (Part p : selectedP.getAssociatedParts()){
                    mPart.addAssociatedParts(p);
                }


                // maps data from "placeholder" associated parts list and removes any parts that were removed in the GUI.
                Set<Integer> ids = placeHolder.stream().map(Part::getId).collect(Collectors.toSet());

                for (Part p : selectedP.getAssociatedParts()) {
                    if (!ids.contains(p.getId())) {
                        mPart.deleteAssociatedPart(p);
                    }
                }

                // maps data from "placeholder" associated parts list and adds any parts that were added in the GUI.
                Set<Integer> idAdd = selectedP.getAssociatedParts().stream().map(Part::getId).collect(Collectors.toSet());

                for (Part p : placeHolder) {
                    if (!idAdd.contains(p.getId())) {
                        mPart.addAssociatedParts(p);
                    }
                }
                // UPDATED PRODUCT LISTING
                    Inventory.updateProduct(i, mPart);

                    // return to main screen
                    Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    root.setStyle("-fx-font-family: Times New Roman");
                    Scene scene = new Scene(root, 850, 600);
                    stage.setTitle("Back to First Screen");
                    stage.setScene(scene);
                    stage.show();
            }

        }
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
}