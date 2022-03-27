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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class AddPart implements Initializable {

     public RadioButton inHouse;
     public RadioButton outsourced;
     public Label changeWithRadio;
     public TextField addPartId;
     public TextField addPartName;
     public TextField addPartStock;
     public TextField addPartCost;
     public TextField addPartMax;
     public TextField addPartMachineId;
     public TextField addPartMin;
     public Button saveAddPart;

    /** This method initializes the page. This is the first method that is called. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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


    /** This method validates all user input and saves the part to the observable list. This method also pushes alerts when input is invalid; if valid, it will save and return to main screen. */
    public void saveToFirstScreen(ActionEvent actionEvent) throws IOException {

        // validation for correct input
        // checking that maximum is a number
        if (!intCheck(addPartMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Maximum must be a number");
            alert.showAndWait();
            return;
        }

        // checking that minimum is a number
        if (!intCheck(addPartMin.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Minimum must be a number");
            alert.showAndWait();
            return;
        }
        // checking that stock is a number
        if (!intCheck(addPartStock.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Inventory/Stock must be a number");
            alert.showAndWait();
            return;
        }
        // checking that IN HOUSE machine ID is a number
        if (!intCheck(addPartMachineId.getText())){
            if (inHouse.isSelected()){
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText("Machine ID must be a number");
                alert.showAndWait();
                return;
            } else {
            }
        }
        // checking that Company Name is a text string
        if (intCheck(addPartMachineId.getText())){
            if (outsourced.isSelected()){
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText("Company Name must be a name");
                alert.showAndWait();
                return;
            }
        }
        // checking that price  is a double
        if (!doubleCheck(addPartCost.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Price must be a number");
            alert.showAndWait();
            return;
        }

        // checking that name is a text string
        if (intCheck(addPartName.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Name must be a name");
            alert.showAndWait();
            return;
        }

        // validation for stock to fall between minimum and maximum
        if (Integer.parseInt(addPartMin.getText()) > Integer.parseInt(addPartStock.getText()) || Integer.parseInt(addPartStock.getText()) > Integer.parseInt(addPartMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Inventory must be greater than minimum and less than maximum.");
            alert.showAndWait();
            return;
        }
        // validation for min to be less than maximum
        if (Integer.parseInt(addPartMin.getText()) > Integer.parseInt(addPartMax.getText())){
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Maximum must be greater than minimum");
            alert.showAndWait();
            return;
        }


        // add inhouse part
        if (inHouse.isSelected()){
            Inventory.addPart(new InHouse(
                    InHouse.getIdentifier(),

                    // string
                    addPartName.getText(),

                    // double

                    Double.parseDouble(addPartCost.getText()),

                    // int
                    Integer.parseInt(addPartStock.getText()),
                    Integer.parseInt(addPartMin.getText()),
                    Integer.parseInt(addPartMax.getText()),
                    Integer.parseInt(addPartMachineId.getText())));
           }


    // add outsourced part
        if (outsourced.isSelected()){
            Inventory.addPart(new Outsourced(
                    //System set int
                    Outsourced.getIdentifier(),
                    addPartName.getText(),
                    Double.parseDouble(addPartCost.getText()),
                    Integer.parseInt(addPartStock.getText()),
                    Integer.parseInt(addPartMin.getText()),
                    Integer.parseInt(addPartMax.getText()),
                    addPartMachineId.getText()));
        }

        // returns to main screen
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        root.setStyle("-fx-font-family: Times New Roman");
        Scene scene = new Scene(root, 850, 600);
        stage.setTitle("Back to First Screen");
        stage.setScene(scene);
        stage.show();


    }




    /** This method cancels the screen. It deletes all entered (but not saved) data and returns to the main screen.  */
    public void cancelToFirstScreen(ActionEvent actionEvent) throws IOException {
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


    /** This changes the text value of the label of the last input. If the Outsourced radio button is selected, the text will display "Company Name".  */
     public void onOutsourced(ActionEvent actionEvent) {
        changeWithRadio.setText("Company Name");
    }
}
