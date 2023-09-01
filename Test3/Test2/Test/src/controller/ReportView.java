package controller;

import helper.JDBCAppointments;
import helper.JDBCContacts;
import helper.JDBCCountries;
import helper.JDBCCustomers;
import javafx.beans.Observable;
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
import model.Contact;
import model.Country;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReportView implements Initializable {
    public ComboBox<String> userInput;
    public RadioButton monthlyApp;
    public TableView appointmentTable;

    public TableColumn customerID;
    public TableColumn endDate;
    public TableColumn startDate;
    public TableColumn description;
    public TableColumn type;
    public TableColumn title;
    public TableColumn appointmentID;
    public ComboBox contactIDChoice;
    public ComboBox monthlyAppointment;
    public ComboBox typeAppointments;
    public Label count;
    public Label typeCount;
    public Label countryCount;
    public ComboBox countryList;

    public void logout(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginScreen.fxml")));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("Login Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method sets ComboBoxes when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactIDChoice.setItems(setCustomerList());
        monthlyAppointment.getItems().addAll(getMonths());
        typeAppointments.setItems(setTypeList());
        setCountryList();
        
    }

    /**
     * This method takes the user back to the appointment view when the back button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void backView(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 640);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
    }

    public void customerSchedule(ActionEvent actionEvent) throws SQLException {

    }

    /**
     * This method sets the customer schedule when a contact ID is selected
     * @param actionEvent
     */
    public void setCustomerSchedule(ActionEvent actionEvent) {
        Integer contactID = Integer.valueOf(String.valueOf(contactIDChoice.getSelectionModel().getSelectedItem()));

        if (contactID != null) {
            try {
                ObservableList<Appointment> customerSchedule = JDBCAppointments.getAppointmentByContactID(contactID);

                appointmentTable.setItems(customerSchedule);

                appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                title.setCellValueFactory(new PropertyValueFactory<>("title"));
                type.setCellValueFactory(new PropertyValueFactory<>("type"));
                description.setCellValueFactory(new PropertyValueFactory<>("description"));
                startDate.setCellValueFactory(new PropertyValueFactory<>("startTime"));
                endDate.setCellValueFactory(new PropertyValueFactory<>("endTime"));
                customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

                appointmentTable.refresh();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method counts the amount of appointments based on the month that is selected
     * @param actionEvent
     */
    public void countMonthlyAppointments(ActionEvent actionEvent) {
        String selectedMonth = (String) monthlyAppointment.getSelectionModel().getSelectedItem();

        System.out.println("The month selected was: " + selectedMonth);

        if (selectedMonth == "January"){
            selectedMonth = String.valueOf(1);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for January");
        } else if (selectedMonth == "February") {
            selectedMonth = String.valueOf(2);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for February");
        } else if (selectedMonth == "March") {
            selectedMonth = String.valueOf(3);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for March");
        } else if (selectedMonth == "April") {
            selectedMonth = String.valueOf(4);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for April");
        } else if (selectedMonth == "May") {
            selectedMonth = String.valueOf(5);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for May");
        } else if (selectedMonth == "June") {
            selectedMonth = String.valueOf(6);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for June");
        } else if (selectedMonth == "July") {
            selectedMonth = String.valueOf(7);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for July");
        } else if (selectedMonth == "August") {
            selectedMonth = String.valueOf(8);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for August");
        } else if (selectedMonth == "September") {
            selectedMonth = String.valueOf(9);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for September");
        } else if (selectedMonth == "October") {
            selectedMonth = String.valueOf(10);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for October");
        } else if (selectedMonth == "November") {
            selectedMonth = String.valueOf(11);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for November");
        } else if (selectedMonth == "December") {
            selectedMonth = String.valueOf(12);
            int recordCount = JDBCAppointments.getMonthlyAppointmentCount(Integer.parseInt(selectedMonth));
            count.setText("You have " + recordCount + " appointments for December");
        }
    }

    /**
     * This method counts the amount of appointments based on the type that is selected.
     * @param actionEvent
     * @throws SQLException
     */
    public void countTypeAppointments(ActionEvent actionEvent) throws SQLException {
        String type = (String) typeAppointments.getSelectionModel().getSelectedItem();

        System.out.println("The type selected was: " + type);


        if(type != null) {
            int recordCount = JDBCAppointments.getTypeAppointmentCount(type);

            typeCount.setText("You have " + recordCount + " for appointment type " + type);
        }

    }

    /**
     * This method counts the amount of customers based on the country that is selected
     * @param actionEvent
     * @throws SQLException
     */
    public void countryCount(ActionEvent actionEvent) throws SQLException {
        String country = (String) countryList.getSelectionModel().getSelectedItem();

        System.out.println("The country selected was: " + country);

        if(country != null){
            int recordCount = JDBCCustomers.getCustomerCount(country);
            countryCount.setText("There are " + recordCount + " customers in the country " + country);
        }
    }

    /**This functions sets the combo box with contact IDs
     *
     * @return a list of contacts
     */
    private ObservableList<String> setCustomerList(){
        ObservableList<String> cList = FXCollections.observableArrayList();

        try{
            ObservableList<Contact> contacts = JDBCContacts.getAllContacts();
            for (Contact contact: contacts){
                cList.add(String.valueOf(contact.getContactID()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return cList;
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

    /**
     * This method saves the types to an observable list
     * @return
     */
    private ObservableList<String> setTypeList(){
        ObservableList<String> tList = FXCollections.observableArrayList();

        try{
            ObservableList<Appointment> appointments = JDBCAppointments.getAllAppointments();
            for (Appointment appointment: appointments){
                tList.add(appointment.getType());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tList;
    }

    /**
     * This creates an array with months
     */
    private static final String [] months = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

    /**
     * This method is used to return the months
     * @return
     */
    public static String [] getMonths(){return  months;}


}

