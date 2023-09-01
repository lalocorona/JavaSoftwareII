package controller;

import helper.JDBCCountries;
import helper.JDBCCustomers;
import helper.JDBCDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerUpdateView implements Initializable {

    public TextField updateCustomerName;
    public TextField updateAddress;
    public TextField updatePostalCode;
    public TextField updatePhoneNumber;
    public ComboBox<String> countryList;
    public ComboBox<String> divisionList;
    public TextField customerID;

    /**
     * This method sets the countryList with country names
     */
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
    /** This function changes the division's drop down based on which country is selected
     *
     * @throws SQLException
     */
    @FXML
    private void setDivision() {
        ObservableList<String> dlist = FXCollections.observableArrayList();

        ObservableList<Division> divisions = JDBCDivisions.getAllDivisons();
        if(divisions != null){
            for(Division division: divisions){
                dlist.add(division.getDivision());
            }
        }

        divisionList.setItems(dlist);

    }

    /**
     * This method is data that is passed on from the previous scene and sets all the text boxes and combo boxes
     * in the Customer Update View.
     * @param selectedCustomer
     */
    public void updateSelectedCustomer(Customer selectedCustomer){
            customerID.setText(Integer.toString(selectedCustomer.getCustomerID()));
            updateCustomerName.setText(selectedCustomer.getCustomerName());
            updateAddress.setText(selectedCustomer.getAddress());
            updatePostalCode.setText(selectedCustomer.getPostalCode());
            updatePhoneNumber.setText(selectedCustomer.getPhoneNumber());
            countryList.getSelectionModel().select(selectedCustomer.getCountry());
            divisionList.getSelectionModel().select(selectedCustomer.getDivision());


    }

    public void selectedDivision(ActionEvent actionEvent) {
    }

    /**
     * This method has logical validation and alerting functions. When all conditions are met,
     * the data inputted will be inserted to the mySQL database.
     * @param actionEvent
     */
    public void saveUpdatedCustomer(ActionEvent actionEvent) {

        try{
            int id = Integer.parseInt(customerID.getText());
            String name = updateCustomerName.getText();
            String address = updateAddress.getText();
            String postalCode = updatePostalCode.getText();
            String phoneNumber = updatePhoneNumber.getText();
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

                JDBCCustomers.updateCustomer(id, name, address, postalCode, phoneNumber, division);

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
     * This method will return to the customer view when the Cancel button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 450);
        stage.setTitle("Add New Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will set the country list and division list when initialization
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCountryList();
        setDivision();
    }

    public void setDivisionByCountry(ActionEvent actionEvent) {
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
}
