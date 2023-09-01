package controller;

import Lambdas.AlertBlank;
import Lambdas.AlertBlankInput;
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
import java.time.temporal.TemporalAccessor;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static java.time.LocalTime.parse;

/**
 * This method is the controller that aids in updating appointment data to the mySQL database.
 */
public class AppointmentUpdateView implements Initializable {
    private ZonedDateTime convertToTimeZone(LocalDateTime time, String zoneId) {
        return ZonedDateTime.of(time, ZoneId.of(zoneId));
    }
    private AlertBlankInput alertService = new AlertBlank();
    private static Appointment selectedAppointment;
    public TextField appointmentID;
    public TextField updateTitle;
    public TextField updateDescription;
    public TextField updateLocation;
    public TextField updateType;
    public DatePicker updateStartDate;
    public DatePicker updateEndDate;
    public ComboBox<String> startTimeList;
    public ComboBox<String> endTimeList;
    public ComboBox <String> customerList;
    public ComboBox <String> contactList;
    public ComboBox <String> userList;

    /**
     * This method sets the ComboBox userList with user IDs
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
     * This method sets the ComboBox contactList with contact names
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
     * This method updates the appointment data that the user inputted and includes
     * the same logic validation as add an appointment.
     * @param actionEvent
     */
    public void updatedAppointment(ActionEvent actionEvent) {
        AlertBlankInput alertDisplay = (title, header1, content1, alertType1) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header1);
            alert.setContentText(content1);
            alert.showAndWait();
        };

        try {
            int appointment = Integer.parseInt(appointmentID.getText());
            String title = updateTitle.getText();
            String description = updateDescription.getText();
            String location = updateLocation.getText();
            String type = updateType.getText();



            if (title.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the title blank!");
                alert.showAndWait();
                return;
            } else if (description.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the description blank!");
                alert.showAndWait();
                return;
            } else if (location.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the location blank!");
                alert.showAndWait();
                return;
            } else if (type.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the type blank!");
                alert.showAndWait();
                return;
            }



            if (updateStartDate.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the start date blank!");
                alert.showAndWait();
                return;
            } else if (updateEndDate.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the end date blank!");
                alert.showAndWait();
                return;
            } else if (startTimeList.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the start time blank!");
                alert.showAndWait();
                return;
            } else if (endTimeList.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the end time blank!");
                alert.showAndWait();
                return;
            }

            LocalDate startDate = updateStartDate.getValue();
            LocalDate endDate = updateEndDate.getValue();
            LocalTime startTime = LocalTime.parse(startTimeList.getSelectionModel().getSelectedItem());
            LocalTime endTime = LocalTime.parse(endTimeList.getSelectionModel().getSelectedItem());



            if (customerList.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the customer blank!");
                alert.showAndWait();
                return;
            } else if (userList.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the user blank!");
                alert.showAndWait();
                return;
            } else if (contactList.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot leave the contact blank!");
                alert.showAndWait();
                return;
            }

            int customerID = Integer.parseInt(customerList.getSelectionModel().getSelectedItem());
            int user_ID = Integer.parseInt(userList.getSelectionModel().getSelectedItem());
            String contact = contactList.getSelectionModel().getSelectedItem();


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

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to save?");
                Optional<ButtonType> optional = alert.showAndWait();

                if (optional.get() == ButtonType.OK) {

                    JDBCAppointments.updateAppointment(appointment, title, description, location, type, parsedStartDateTime, parsedEndDateTime, customerID, user_ID, contact);

                    Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 1400, 640);
                    stage.setTitle("Appointments");
                    stage.setScene(scene);
                    stage.show();
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to test the dates and times using logic validation.
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


        int appointment_ID = Integer.parseInt(appointmentID.getText());

        LocalDate startDateTest = (updateStartDate.getValue());
        LocalDate endDateTest = (updateEndDate.getValue());


        if (startDateTest.isAfter(endDateTest)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The start date must be before the end date!");
            alert.showAndWait();
            return false;
        } else if (startDateTest.isBefore(endDateTest)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The dates must be the same!");
            alert.showAndWait();
            return false;
        }

/*        //This is used to test if the date selected is on a weekend
        if(startDateTest != null){
            DayOfWeek weekend = startDateTest.getDayOfWeek();
            if(weekend == DayOfWeek.SATURDAY || weekend == DayOfWeek.SUNDAY){
                alertDisplay.showAlert("Error", "Weekend Day selected!", "Business Hours are Mon-Friday \n 8:00am - 10:00pm ET", Alert.AlertType.ERROR);
                return false;
            }

        }*/



        LocalTime startTimeTest = parse(startTimeList.getSelectionModel().getSelectedItem());
        LocalTime endTimeTest = parse(endTimeList.getSelectionModel().getSelectedItem());

        if(startTimeTest.isAfter(endTimeTest)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The start time must be before the end time!");
            alert.showAndWait();
            return false;
        }
        else if (startTimeTest.equals(endTimeTest)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Times cannot be equal to each other.");
            alert.showAndWait();
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

        try {
            ObservableList<Appointment> appointments = JDBCAppointments.checkAppointmentOverlap(Integer.parseInt(customerList.getSelectionModel().getSelectedItem()), appointment_ID);
            for (Appointment appointment : appointments) {
                customerStartDateChecker = appointment.getStartDate().atTime(appointment.getStartTime().toLocalTime());
                customerEndDateChecker = appointment.getEndDate().atTime(appointment.getEndTime().toLocalTime());

                System.out.println("This is an existing appointment start date time" + customerStartDateChecker);
                System.out.println("This is an existing appointment end date time" + customerEndDateChecker);

                System.out.println("This is what the start time proposed is: " + parsedStartDateTime);


                if ((customerStartDateChecker.isAfter(parsedStartDateTime) && customerEndDateChecker.isBefore(parsedStartDateTime))) {
                    alertService.showAlert("Error", "Date Error", "This appointment date overlaps with another customer", Alert.AlertType.ERROR);
                    return false;
                } else if (customerEndDateChecker.isAfter(parsedStartDateTime) && customerEndDateChecker.isBefore(parsedEndDateTime)) {
                    alertService.showAlert("Error", "Date Error", "This appointment date overlaps with another customer", Alert.AlertType.ERROR);
                    return false;
                } else if (parsedStartDateTime.isAfter(customerStartDateChecker) && parsedEndDateTime.isBefore(customerEndDateChecker)) {
                    alertService.showAlert("Error", "Date Error", "This appointment date overlaps with another customer", Alert.AlertType.ERROR);
                    return false;
                } else if (parsedEndDateTime.isEqual(customerEndDateChecker) && parsedStartDateTime.isEqual(customerStartDateChecker)) {
                    alertService.showAlert("Error", "Date ERror", "This appointment date overlaps with the same customer", Alert.AlertType.ERROR);
                    return false;
                } else if (parsedStartDateTime.isEqual(customerEndDateChecker) || (parsedEndDateTime.isEqual(customerStartDateChecker))) {
                    alertService.showAlert("Error", "Date ERROR", "This appointment date overlaps with the same customer", Alert.AlertType.ERROR);
                    return false;
                }
            }
            } catch(SQLException ex){
                throw new RuntimeException(ex);
            }
            return true;
        }

    /**This method will cancel updating the appointment and return to the Appointment View
     *
     * @param actionEvent
     * @throws IOException
     */
    public void cancelAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AppointmentView.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 640);
        stage.setTitle("Appointment View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method loads an appointment selected from the AppointmentView controller and passes it to this controller
     * and loads the data in the text boxes and ComboBoxes.
     * @param appointment
     */
    public void updateSelectedAppointment(Appointment appointment) {
        selectedAppointment = appointment;


        //This is used to convert the selected time date that's in EST to your local timezone
        ZoneId estZoneID = ZoneId.of("America/New_York");
        ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime localStartTime = ZonedDateTime.of(appointment.getStartDate(), LocalTime.from(appointment.getStartTime()), estZoneID);
        ZonedDateTime localEndTime = ZonedDateTime.of(appointment.getEndDate(), LocalTime.from(appointment.getEndTime()), estZoneID);

        ZonedDateTime startToEST = localStartTime.withZoneSameInstant(localZoneID);
        ZonedDateTime endToEST = localEndTime.withZoneSameInstant(localZoneID);

        //System.out.println("This is the zone: " + startToEST);
        //System.out.println("This is the zone: " + endToEST);


        LocalTime newStartTime = startToEST.toLocalTime();
        LocalTime newEndTime = endToEST.toLocalTime();
        //System.out.println("This is the new start zone: " + newStartTime);
        //System.out.println("This is the new end zone: " + newEndTime);


        appointmentID.setText(Integer.toString(appointment.getAppointmentID()));
        updateTitle.setText(selectedAppointment.getTitle());
        updateDescription.setText(selectedAppointment.getDescription());
        updateLocation.setText(selectedAppointment.getLocation());
        updateType.setText(selectedAppointment.getType());
        updateStartDate.setValue(selectedAppointment.getStartDate());
        updateEndDate.setValue(selectedAppointment.getEndDate());
        startTimeList.setValue((String.valueOf(newStartTime)));
        endTimeList.getSelectionModel().select(String.valueOf(newEndTime));
        customerList.getSelectionModel().select((selectedAppointment.getCustomerID() -1));
        contactList.getSelectionModel().select(selectedAppointment.getContactID() - 1 );
        userList.getSelectionModel().select(Integer.valueOf(selectedAppointment.getUserID() - 1));
    }

    /**
     * This method sets all the ComboBoxes on initialization
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setContactList();
        setCustomerList();
        setUserList();
        startTimeList.getItems().addAll(TimeZoneConvertor.convertToMachineLocalTime(startTimeList));
        endTimeList.getItems().addAll(TimeZoneConvertor.convertToMachineLocalTime(endTimeList));
    }
}
