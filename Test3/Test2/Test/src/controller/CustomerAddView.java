package controller;

import helper.JDBCCountries;
import helper.JDBCCustomers;
import helper.JDBCDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerAddView implements Initializable {
    public ComboBox<String> divisionList;
    public ComboBox<String> countryList;
    public TextField newCustomerName;
    public TextField newAddress;
    public TextField newPostalCode;
    public TextField newPhoneNumber;
    public TextField customerID;

    /**This initializes the scene and loads that countries in the combo box.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCountryList();
    }

    /** This function changes the division's drop down based on which country is selected
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void setDivision(ActionEvent actionEvent) throws SQLException {
        //Used to check which country the user selected
        String countryNameSelected = countryList.getValue();
        System.out.println("You selected a country: " + countryNameSelected);

        ObservableList<String> dlist = FXCollections.observableArrayList();

        try {
            ObservableList<Division> divisions = JDBCDivisions.getDivisionByCountryName(countryList.getSelectionModel().getSelectedItem());
            if(divisions != null){
                for(Division division: divisions){
                    dlist.add(division.getDivision());
                }
            }

            divisionList.setItems(dlist);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * This function sets the country list when initializing
     * **/
    private void setCountryList(){
        ObservableList<String> cList = FXCollections.observableArrayList();

        try {
            ObservableList<Country> countries = JDBCCountries.getAllCountries();
            for (Country country: countries){
                cList.add(country.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        countryList.setItems(cList);

    }

    public void selectedDivision(ActionEvent actionEvent) {
    }

    /**This function takes all the inputs from the users and features alerts for incorrect input.
     * Then the function is saved after the button save is hit.
     *
     * @param actionEvent
     */
    public void saveNewCustomer(ActionEvent actionEvent) {
        //This is for getting the last customer ID to help with the auto-generation
        int i = 0;
        for(Customer customer: JDBCCustomers.getAllCostumers()) {
            if (customer.getCustomerID() > 1) {
                i = customer.getCustomerID();
                //System.out.println("The current customer ID is " + i);
            }
        }

            try{
                String name = newCustomerName.getText();
                String address = newAddress.getText();
                String postalCode = newPostalCode.getText();
                String phoneNumber = newPhoneNumber.getText();
                String country = countryList.getValue();
                String division = divisionList.getValue();

                if (name.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot leave the customer name blank!");
                    alert.showAndWait();
                    return;

                } else if (address.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot leave the address blank!");
                    alert.showAndWait();
                    return;

                } else if (postalCode.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot leave the postal code blank!");
                    alert.showAndWait();
                    return;
                } else if (postalCode.length() > 6 ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Postal code cannot be more than 6 numbers!");
                    alert.showAndWait();
                    return;
                } else if (phoneNumber.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You cannot leave the phone number blank!");
                    alert.showAndWait();
                    return;
                } else if (phoneNumber.length() != 11) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Phone number needs to be 11 numbers long!");
                    alert.showAndWait();
                    return;
                } else if (countryList.getSelectionModel().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please select a country first!");
                    alert.showAndWait();
                    return;
                }else if(divisionList.getSelectionModel().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please select a division first!");
                    alert.showAndWait();
                    return;
                }


                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to save?");
                Optional<ButtonType> optional = alert.showAndWait();

                if (optional.get() == ButtonType.OK) {

                    JDBCCustomers.insertCustomer(i + 1, name, address, postalCode, phoneNumber, division);

                    Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 1040, 400);
                    stage.setTitle("Add New Customer");
                    stage.setScene(scene);
                    stage.show();
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }



        }

    /**
     * This function clears the input in the scene
     * @param actionEvent
     * @throws IOException
     */
    public void clearCustomerPage(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerAddView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 450);
        stage.setTitle("Add New Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will return to the customer view when the Cancel button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> optional = alert.showAndWait();

        if (optional.get() == ButtonType.OK){
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1024, 640);
            stage.setTitle("Add New Customer");
            stage.setScene(scene);
            stage.show();
        }
        else{
        }

    }
}

