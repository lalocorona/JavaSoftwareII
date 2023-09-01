package controller;

import helper.JDBCAppointments;
import helper.JDBCCustomers;
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
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.JDBCCustomers.getAllCostumers;
public class CustomerView implements Initializable {
    public TableView<Customer> customerTable;
    public TableColumn customerID;
    public TableColumn customerName;
    public TableColumn address;
    public TableColumn postalCode;
    public TableColumn phone;
    public TableColumn divisionID;
    public TableColumn countryName;
    public TableColumn divisionName;
    private Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Customer> cList;

        try{
            cList = JDBCCustomers.getAllCostumers();
            customerTable.setItems(cList);

            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
            divisionName.setCellValueFactory(new PropertyValueFactory<>("division"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This function loads the Customer Add View creating a new scene
     *
     * @param actionEvent
     * @throws IOException
     */
    public void addNewCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerAddView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 450);
        stage.setTitle("Add New Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**This function loads the previous scene Appointment View
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 600);
        stage.setTitle("Add New Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**When a customer is selected from the table, it will load the selected customer to the Update View.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void updateCustomer(ActionEvent actionEvent) throws IOException {
        Customer updateCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (updateCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select a customer!");
            alert.showAndWait();
        } else {

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerUpdateView.fxml"));
            scene = loader.load();

            CustomerUpdateView controller = loader.getController();
            controller.updateSelectedCustomer(updateCustomer);
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method has logic that checks if the selected customer has an existing appointment
     * @param deleteCustomer
     * @return A true value if a customer has an existing appointment and false if there isn't an appointment
     */
    private boolean appointmentChecker(Customer deleteCustomer) {
        try {
            ObservableList appointCheck = JDBCAppointments.getAppointmentByCustomerID(deleteCustomer.getCustomerID());
            if (appointCheck != null && appointCheck.size() < 1) {
                return true;
            } else {
                return false;
            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * This function has logical validation on the condition that if a customer has an appointment
     * it won't delete the customer.
     * @param actionEvent
     * @throws SQLException
     */
    public void deleteCustomer(ActionEvent  actionEvent) throws SQLException {
        Customer deleteCustomer = customerTable.getSelectionModel().getSelectedItem();

        if(deleteCustomer == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select a customer!");
            alert.showAndWait();
        }

        boolean valid = appointmentChecker(deleteCustomer);

        if (valid){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this customer?");
            Optional<ButtonType> optional = alert.showAndWait();

            if(optional.get() == ButtonType.OK){
                int id = deleteCustomer.getCustomerID();
                JDBCCustomers.deleteCustomer(id);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setContentText("The customer was successfully deleted");
                info.showAndWait();

                //This refreshes the table after a customer has been deleted
                ObservableList<Customer> cList = FXCollections.observableArrayList(JDBCCustomers.getAllCostumers());
                customerTable.setItems(cList);

            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("This customer has appointment! It cannot be deleted.");
            Optional<ButtonType> optional = alert.showAndWait();
            System.out.println("A customer with an appointment was selected.");
        }

    }
}
