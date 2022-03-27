package main;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;


public class Main extends Application {


    /** This sets the stage and stylizes the program.      */

    @Override
    public void start(Stage primaryStage) throws Exception {
        addTestData();
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        root.setStyle("-fx-font-family: Times New Roman");
        primaryStage.setTitle("First Screen");
        primaryStage.setScene(new Scene(root, 850, 600));
        primaryStage.show();
    }


    /** This contains the test data used to prepopulate the tables in the program.      */
    private void addTestData(){
        Part crochet = new InHouse(1, "K Crochet Hook", 5.99, 4, 6, 12, 5554);
        Part c2 = new InHouse(3, "J Crochet Hook", 9.99, 4, 1,9, 5555);
        Part sn = new Outsourced(6, "Sewing Needle", 3.99, 15, 10, 25, "Singer");
        Product sma = new Product(544, "Sewing Machine Arm", 46, 2, 1,9);
        Product ybw = new Product(6, "Yarn Ball Winder", 55, 1, 1, 3);
        Inventory.addPart(crochet);
        Inventory.addPart(c2);
        Inventory.addPart(sn);
        Inventory.addProduct(sma);
        Inventory.addProduct(ybw);
    }

    /** This launches the program.      */
    public static void main(String[] args) {
        launch(args);
    }




}

