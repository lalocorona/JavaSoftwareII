package controller;

import Lambdas.AlertBlank;
import Lambdas.AlertBlankInput;
import Lambdas.AlertConfirmation;
import helper.*;
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
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static java.time.LocalTime.parse;

/**This method controller is used to add appointments that are inputted by the user in the FXML scene.
 *
 */
public class AppointmentAddView implements Initializable {
    public ComboBox <String> endTimeList;
    public ComboBox <String> customerList;
    public TextField appointmentID;
    public TextField newTitle;
    public TextField newDescription;
    public TextField newLocation;
    public TextField newType;
    public DatePicker newStartDate;
    public DatePicker newEndDate;
    public ComboBox <String> startTimeList;
    public ComboBox <String> contactList;
    public TextField userID;
    public ComboBox <String> userList;


    /**Upon initialization there are five ComboBoxes that are set with data for the user to select.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimeList.getItems().addAll(TimeZoneConvertor.convertToMachineLocalTime(startTimeList));
        endTimeList.getItems().addAll(TimeZoneConvertor.convertToMachineLocalTime(endTimeList));
        setCustomerList();
        setContactList();
        setUserList();

    }

    /**This method sets the ComboBox userList with user IDs
     *
     */
    private void setUserList(){
        ObservableList<String> uList = FXCollections.observableArrayList();
        ObservableList<User> users = JDBCUsers.getAllUsers();

        for(User user: users){
            uList.add(String.valueOf(user.getUserID()));

        }
        userList.setItems(uList);
    }

    /**
     * This method sets the Combobox contactList with contacts names
     */
    private void setContactList(){
        ObservableList<String> cList = FXCollections.observableArrayList();
        ObservableList<Contact> contacts = JDBCContacts.getAllContacts();
        for (Contact contact: contacts){
            cList.add(String.valueOf(contact.getContactName()));
        }
        contactList.setItems(cList);

    }

    /**
     * This method sets the ComboBox customerList with customer IDs
     */
    private void setCustomerList(){
        ObservableList<String> cList = FXCollections.observableArrayList();

        ObservableList<Customer> customers = JDBCCustomers.getAllCostumers();
        for (Customer customer: customers){
            cList.add(String.valueOf(customer.getCustomerID()));
        }
        customerList.setItems(cList);

    }


    /**
     * This method will save an appointment using user input from text-fields, combo boxes, and date-picker.
     * There is also logic validation for null inputs, as well as, time/date validation. When all conditions are met,
     * the data will be inserted into the mySQL database using JDBCAppointments.insert() method.
     * @param actionEvent
     * The Lambda expression here is used to create alerts.
     */
    public void saveAppointment(ActionEvent actionEvent) {
        AlertBlankInput alertDisplay = (title, header1, content1, alertType1) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header1);
            alert.setContentText(content1);
            alert.showAndWait();
        };

        try {
            String title = newTitle.getText();
            String description = newDescription.getText();
            String location = newLocation.getText();
            String type = newType.getText();



            if (title.isEmpty()) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the title blank!", Alert.AlertType.ERROR);
                return;
            } else if (description.isEmpty()) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the description blank!", Alert.AlertType.ERROR);
                return;
            } else if (location.isEmpty()) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the location blank!", Alert.AlertType.ERROR);
                return;
            } else if (type.isEmpty()) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the type blank!", Alert.AlertType.ERROR);
                return;
            }


            if (newStartDate.getValue() == null) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the start date blank!", Alert.AlertType.ERROR);
                return;
            } else if (newEndDate.getValue() == null) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the end date blank!", Alert.AlertType.ERROR);
                return;
            } else if (startTimeList.getValue() == null) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the start time blank!", Alert.AlertType.ERROR);
                return;
            } else if (endTimeList.getValue() == null) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the end time blank!", Alert.AlertType.ERROR);
                return;
            }

            LocalDate startDate = newStartDate.getValue();
            LocalDate endDate = newEndDate.getValue();
            LocalTime startTime = parse(startTimeList.getSelectionModel().getSelectedItem());
            LocalTime endTime = parse(endTimeList.getSelectionModel().getSelectedItem());



            if (customerList.getSelectionModel().getSelectedItem() == null) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the customer blank!", Alert.AlertType.ERROR);
                return;
            } else if (userList.getSelectionModel().getSelectedItem() == null) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the user blank!", Alert.AlertType.ERROR);
                return;
            } else if (contactList.getSelectionModel().getSelectedItem() == null) {
                alertDisplay.showAlert("Error", "Missing Value", "You cannot leave the contact blank!", Alert.AlertType.ERROR);
                return;
            }

            int customerID = Integer.parseInt(customerList.getSelectionModel().getSelectedItem());
            int user_ID = Integer.parseInt(userList.getSelectionModel().getSelectedItem());
            String contact = (contactList.getSelectionModel().getSelectedItem());



            System.out.println("This is what was entered: " + title + " " + description + " " + location + " " + type + " " + startDate + " " + endDate + " " + startTime + " " + endTime + " " + customerID + " " + contact + ".");

            ZoneId estZoneID = ZoneId.of("America/New_York");
            ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
            ZonedDateTime localStartTime = ZonedDateTime.of(startDate,startTime, localZoneID);
            ZonedDateTime localEndTime = ZonedDateTime.of(endDate, endTime, localZoneID);

            ZonedDateTime startToEST = localStartTime.withZoneSameInstant(estZoneID);
            ZonedDateTime endToEST = localEndTime.withZoneSameInstant(estZoneID);

            System.out.println("The new time is " + startToEST);
            System.out.println("The new end time is" + endToEST);

            String sDate = String.valueOf(startToEST.toLocalDate());
            String sTime = String.valueOf(startToEST.toLocalTime());
            String sDateTime = sDate + " " + sTime;


            String eDate = String.valueOf(endToEST.toLocalDate());
            String eTime = String.valueOf(endToEST.toLocalTime());
            String eDateTime = eDate + " " + eTime;

            System.out.println(sDateTime);
            System.out.println(eDateTime);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime parsedStartDateTime = LocalDateTime.parse(sDateTime, formatter);
            LocalDateTime parsedEndDateTime = LocalDateTime.parse(eDateTime, formatter);


            boolean valid = dateTester();
            if(valid) {
                AlertConfirmation.showConfirmation("Confirmation","Are you sure you want to save?",
                        () -> {
                    System.out.println("Confirmed!");
                            JDBCAppointments.insertAppointment(title, description, location, type, parsedStartDateTime, parsedEndDateTime, customerID, user_ID, contact);

                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root, 1400, 640);
                            stage.setTitle("Add New Customer");
                            stage.setScene(scene);
                            stage.show();

                        }
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This method will cancel adding the appointment and return to the Appointment View
     *
     * @param actionEvent
     * @throws IOException
     */
    public void cancelAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 640);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
    }

    /**This method is used to test the dates and times using logic validation.
     * This will ensure that the date and time that is selected from the user
     * is correct.
     *
     * @return a boolean value of true or false if the conditions are met.
     */
    private boolean dateTester() {
        AlertBlankInput alertDisplay = (title, header1, content1, alertType1) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header1);
            alert.setContentText(content1);
            alert.showAndWait();
        };

        LocalDate startDateTest = (newStartDate.getValue());
        LocalDate endDateTest = (newEndDate.getValue());

/*        //This is used to test if the date selected is on a weekend
        if(startDateTest != null){
            DayOfWeek weekend = startDateTest.getDayOfWeek();
            if(weekend == DayOfWeek.SATURDAY || weekend == DayOfWeek.SUNDAY){
                alertDisplay.showAlert("Error", "Weekend Day selected!", "Business Hours are Mon-Friday \n 8:00am - 10:00pm ET", Alert.AlertType.ERROR);
                return false;
            }

        }*/

        if (startDateTest.isAfter(endDateTest)) {
            alertDisplay.showAlert("Error", "Date Error","The start date must be before the end date!", Alert.AlertType.ERROR );
            return false;
        } else if (startDateTest.isBefore(endDateTest)) {
            alertDisplay.showAlert("Error", "Date Error","The dates must be the same!", Alert.AlertType.ERROR );
            return false;
        }

        LocalTime startTimeTest = parse(startTimeList.getSelectionModel().getSelectedItem());
        LocalTime endTimeTest = parse(endTimeList.getSelectionModel().getSelectedItem());

        if(startTimeTest.isAfter(endTimeTest)){
            alertDisplay.showAlert("Error", "Date Error","The start time must be before the end time!", Alert.AlertType.ERROR );
            return false;
        }
        else if (startTimeTest.equals(endTimeTest)){
            alertDisplay.showAlert("Error", "Date Error","Times cannot be equal to each other.", Alert.AlertType.ERROR );
            return false;
        }

        ZoneId estZoneID = ZoneId.of("America/New_York");
        ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime localStartTime = ZonedDateTime.of(startDateTest,startTimeTest, localZoneID);
        ZonedDateTime localEndTime = ZonedDateTime.of(endDateTest, endTimeTest, localZoneID);

        ZonedDateTime startToEST = localStartTime.withZoneSameInstant(estZoneID);
        ZonedDateTime endToEST = localEndTime.withZoneSameInstant(estZoneID);

        String sDate = String.valueOf(startToEST.toLocalDate());
        String sTime = String.valueOf(startToEST.toLocalTime());
        String sDateTime = sDate + " " + sTime;


        String eDate = String.valueOf(endToEST.toLocalDate());
        String eTime = String.valueOf(endToEST.toLocalTime());
        String eDateTime = eDate + " " + eTime;


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime parsedStartDateTime = LocalDateTime.parse(sDateTime, formatter);
        LocalDateTime parsedEndDateTime = LocalDateTime.parse(eDateTime, formatter);

        //This code is used to check if the selected time is between business hours
        if(parsedStartDateTime.toLocalTime().isBefore(LocalTime.of(8,0))){
            alertDisplay.showAlert("Error", "Outside of Business Hours", "Business Hours are Mon-Friday \n 8:00am - 10:00pm ET \n Including Weekends", Alert.AlertType.ERROR);
            return false;
        } else if (parsedStartDateTime.toLocalTime().isAfter(LocalTime.of(22,00)) || parsedEndDateTime.toLocalTime().isAfter(LocalTime.of(22,00)))  {
            alertDisplay.showAlert("Error", "Outside of Business Hours", "Business Hours are Mon-Friday \n 8:00am - 10:00pm ET \n Including Weekends", Alert.AlertType.ERROR);
            return false;
        }


        LocalDateTime customerStartDateChecker;
        LocalDateTime customerEndDateChecker;

        try{
            ObservableList<Appointment> appointments = JDBCAppointments.getAppointmentInfoByCustomerID(Integer.parseInt(customerList.getSelectionModel().getSelectedItem()));
            for(Appointment appointment: appointments) {
                customerStartDateChecker = appointment.getStartDate().atTime(appointment.getStartTime().toLocalTime());
                customerEndDateChecker = appointment.getEndDate().atTime(appointment.getEndTime().toLocalTime());

                System.out.println("This is an existing appointment start date time" + customerStartDateChecker);
                System.out.println("This is an existing appointment end date time" + customerEndDateChecker);

                System.out.println("This is what the start time proposed is: " + parsedStartDateTime);


                if ((customerStartDateChecker.isAfter(parsedStartDateTime) && customerEndDateChecker.isBefore(parsedStartDateTime))) {
                    alertDisplay.showAlert("Error", "Date Error","This appointment date overlaps with another customer", Alert.AlertType.ERROR );
                    return false;
                } else if (customerEndDateChecker.isAfter(parsedStartDateTime) && customerEndDateChecker.isBefore(parsedEndDateTime)) {
                    alertDisplay.showAlert("Error", "Date Error","This appointment date overlaps with another customer", Alert.AlertType.ERROR );
                    return false;
                } else if (parsedStartDateTime.isAfter(customerStartDateChecker) && parsedEndDateTime.isBefore(customerEndDateChecker)) {
                    alertDisplay.showAlert("Error", "Date Error","This appointment date overlaps with another customer", Alert.AlertType.ERROR );
                    return false;
                } else if (parsedEndDateTime.isEqual(customerEndDateChecker) && parsedStartDateTime.isEqual(customerStartDateChecker)) {
                    alertDisplay.showAlert("Error", "Date ERror", "This appointment date overlaps with the same customer", Alert.AlertType.ERROR);
                    return false;
                }
                else if (parsedStartDateTime.isEqual(customerEndDateChecker) || (parsedEndDateTime.isEqual(customerStartDateChecker))){
                    alertDisplay.showAlert("Error", "Date ERROR", "This appointment date overlaps with the same customer", Alert.AlertType.ERROR);
                    return false;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }
}

