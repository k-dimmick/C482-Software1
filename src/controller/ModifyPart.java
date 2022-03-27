package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPart  implements Initializable {
    public RadioButton inHouse;
    public RadioButton outsourced;
    public Label changeWithRadio;
    public TextField modifyPartID;
    public TextField modName;
    public TextField modStock;
    public TextField modPrice;
    public TextField modMax;
    public TextField modMachineId;
    public TextField modMin;
    public Button modSave;
    public Button cancel;
    private Object InHouse;
    private Object Outsourced;


    /** This method checks if input is an integer. This is accomplished by checking if the user input is parsable.  */
    private boolean intCheck (String string){
        try {
            int searchInput = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    /** This method pre-populates the data into the user input fields. This is done by pulling the selected part, and parsing each variable to its corresponding text field.   */
    public void initData (Part selectedPart) {
        if (InHouse.class.isAssignableFrom(selectedPart.getClass())) {
            InHouse part = (InHouse) selectedPart;
            inHouse.setSelected(true);
            modifyPartID.setText(Integer.toString(selectedPart.getId()));
            modName.setText(selectedPart.getName());
            modPrice.setText(Double.toString(part.getPrice()));
            modStock.setText(Integer.toString(part.getStock()));
            modMin.setText(Integer.toString(part.getMin()));
            modMax.setText(Integer.toString(part.getMax()));
            modMachineId.setText(Integer.toString(part.getMachineId()));
            changeWithRadio.setText("Machine ID");
        } else {
            Outsourced part = (Outsourced) selectedPart;
            outsourced.setSelected(true);
            modifyPartID.setText(Integer.toString(selectedPart.getId()));
            modName.setText(selectedPart.getName());
            modPrice.setText(Double.toString(part.getPrice()));
            modStock.setText(Integer.toString(part.getStock()));
            modMin.setText(Integer.toString(part.getMin()));
            modMax.setText(Integer.toString(part.getMax()));
            changeWithRadio.setText("Company Name");
            modMachineId.setText(part.getCompanyName());

        }
    }




    /** This method initializes the page. This is the first method that is called. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


    /** This method cancels the screen. It deletes all entered (but not saved) data and returns to the main screen.  */
    public void cancelReturn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: Times New Roman");
        Scene scene = new Scene(root, 850, 600);
        stage.setTitle("Back to First Screen");
        stage.setScene(scene);
        stage.show();
    }

    /** This changes the text value of the label of the last input. If the In House radio button is selected, the text will display "Machine ID".  */
    public void onInHouse(ActionEvent actionEvent) {
        changeWithRadio.setText("Machine ID");
    }


    /** This changes the text value of the label of the last input. If the outsourced radio button is selected, the text will display "Company Name".  */
    public void onOutsourced(ActionEvent actionEvent) {
        changeWithRadio.setText("Company Name");
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


    /** This method validates all user input and saves the updated part data to the correct instance of the Observable List. This method also pushes alerts when input is invalid; if valid, it will save and return to main screen.      */
    public void modifySavePart(ActionEvent actionEvent) throws IOException {
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

        // checking that in house machine ID is a number
        if (!intCheck(modMachineId.getText())){
            if (inHouse.isSelected()){
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText("Machine ID must be a number");
                alert.showAndWait();
                return;
            } else {
            }
        }
        // checking that outsourced company name is a text string
        if (intCheck(modMachineId.getText())){
            if (outsourced.isSelected()){
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText("Company Name must be a name");
                alert.showAndWait();
                return;
            }
        }
        // checking that price is a number
        if (!doubleCheck(modPrice.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Price must be a number");
            alert.showAndWait();
            return;
        }
        // checking that name is a text string
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

        /*
        First IF statement pulls the existing part at its given index in the Observable List.
        Confirms if part is Outsourced or InHouse.
        creates a placeholder part (mPart) for the applicable type (In House or Outsourced), generating a new ID number.
        Then updates the selected part in the Inventory and return to main screen

         */
        int pId = Integer.parseInt(modifyPartID.getText());
        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
            if (Inventory.getAllParts().get(i).getId() == pId){
                if (intCheck(modMachineId.getText())) {
                    //In House
                    InHouse mPart = new InHouse(
                            1,
                            modName.getText(),
                            Double.parseDouble(modPrice.getText()),
                            Integer.parseInt(modStock.getText()),
                            Integer.parseInt(modMin.getText()),
                            Integer.parseInt(modMax.getText()),
                            Integer.parseInt(modMachineId.getText()));
                    mPart.setId(pId);
                    Inventory.updatePart(i, mPart);
                } else {
                    Outsourced mPart = new Outsourced(
                            1,
                            modName.getText(),
                            Double.parseDouble(modPrice.getText()),
                            Integer.parseInt(modStock.getText()),
                            Integer.parseInt(modMin.getText()),
                            Integer.parseInt(modMax.getText()),
                            modMachineId.getText());
                    mPart.setId(pId);
                    Inventory.updatePart(i, mPart);
                }

                // return to main screen
                Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                root.setStyle("-fx-font-family: Times New Roman");
                Scene scene = new Scene(root, 850, 600);
                stage.setTitle("Back to First Screen");
                stage.setScene(scene);
                stage.show();
            }
        }
    }
}
