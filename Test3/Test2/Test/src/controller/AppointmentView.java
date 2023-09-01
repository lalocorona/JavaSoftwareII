package controller;

import Lambdas.AlertBlank;
import Lambdas.AlertBlankInput;
import Lambdas.AlertConfirmation;
import helper.JDBCAppointments;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * This method is the controller that loads all the appointments to a table view.
 */
public class AppointmentView implements Initializable {
    private AlertBlankInput alertService = new AlertBlank();
    public RadioButton weeklyApp;
    public RadioButton monthlyApp;
    private Parent scene;
    public TableView appointmentTable;
    public TableColumn appointmentID;
    public TableColumn title;
    public TableColumn description;
    public TableColumn location;
    public TableColumn type;
    public TableColumn startDate;
    public TableColumn endDate;
    public TableColumn customerID;
    public TableColumn userID;
    public TableColumn startTime;
    public TableColumn endTime;
    public TableColumn contactID;
    public Label user;

    /**This method logs out the user and returns to the login view
     *Lambda Expressions are used for confirmation, informational, and error messages
     * @param actionEvent
     * @throws IOException
     */
    public void logout(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginScreen.fxml")));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 500);
            stage.setTitle("Login Screen");
            stage.setScene(scene);
            stage.show();
        }

    /**
     * This method loads all the appointments when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         loadAppointments();

        Duration delay = Duration.seconds(2); // Adjust the time delay as needed
        Timeline timeline = new Timeline(
                new KeyFrame(delay, event -> alertAppointment())
        );
        timeline.play();

    }

    /**
     * This method loads all the appointments in the tableview and loads the columns
     */
    public void loadAppointments(){
        ObservableList<Appointment> aList;
        try {
            aList = JDBCAppointments.getAllAppointments();

            appointmentTable.setItems(aList);
            appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            location.setCellValueFactory(new PropertyValueFactory<>("location"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            contactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            userID.setCellValueFactory(new PropertyValueFactory<>("userID"));



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**This method loads the customer screen when the action event is clicked
     *
     * @param actionEvent
     * @throws IOException
     */
    public void customerScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 400);
        stage.setTitle("Customer View");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method loads the appointment add view screen when the button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void newAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentAddView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     */

    public void updateAppointment(ActionEvent actionEvent)  throws IOException{
        Appointment updateAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

        if(updateAppointment == null){
            /**
             * Lambda Expression
             */
            alertService.showAlert("Warning!","No selected Appointment","You must select an appointment", Alert.AlertType.ERROR);
        } else {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppointmentUpdateView.fxml"));
            scene = loader.load();

            AppointmentUpdateView controller = loader.getController();
            controller.updateSelectedAppointment(updateAppointment);
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method deletes an appointment. There is logic validation and checks an appointment is selected.
     * @param actionEvent
     * @throws SQLException
     */
    public void deleteAppointment(ActionEvent actionEvent) throws SQLException {
        Appointment deleteAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

        if(deleteAppointment == null){
            /**
             * Lambda Expression
             */
            alertService.showAlert("Warning!","No selected Appointment","You must select an appointment", Alert.AlertType.ERROR);

        }

        boolean valid = appointmentID();
        if(valid) {
            /**
             * Lambda Expression used here
             */
            AlertConfirmation.showConfirmation("Confirmation","Are you sure you want to delete this appointment",
                    () ->{
                System.out.println("Confirmed!");
                        String appointmentType = deleteAppointment.getType();
                        int appointmentID = deleteAppointment.getAppointmentID();
                        JDBCAppointments.deleteAppointment(appointmentID);

                        Alert info = new Alert(Alert.AlertType.INFORMATION);
                        info.setContentText("The appointment ID: " + appointmentID + " & Type: " + appointmentType + ", was successfully deleted");
                        info.showAndWait();

                        //This refreshes the table after a customer has been deleted
                        ObservableList<Appointment> aList = null;
                        try {
                            aList = FXCollections.observableArrayList(JDBCAppointments.getAllAppointments());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        appointmentTable.setItems(aList);
                    }
            );

        }
    }

    /**
     * This method returns a boolean value if the selected item has an appointment ID
     * @return
     */
    private boolean appointmentID(){

    ObservableList<String> appointments = appointmentTable.getSelectionModel().getSelectedItems();
        if(appointments.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * This method loads weekly appointments and displays them when a radio button is selected.
     * @param actionEvent
     */
    public void loadWeeklyAppointments(ActionEvent actionEvent) {
        ObservableList<Appointment> weeklyAppointment;
        ObservableList<Appointment> appointments;
        if (weeklyApp.isSelected()) {
            try {
                weeklyAppointment = JDBCAppointments.getWeeklyAppointments();
                appointmentTable.setItems(weeklyAppointment);

                appointmentTable.refresh();

                if(appointmentTable.getItems().isEmpty()){
                    /**
                     * Lambda Expression
                     */
                    alertService.showAlert("Information", null, "You Have no weekly appointments", Alert.AlertType.INFORMATION);

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else{
            try {
                appointments = JDBCAppointments.getAllAppointments();
                appointmentTable.setItems(appointments);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method loads monthly appointment appointments and displays them when a radio button is selected
     * @param actionEvent
     */
    public void loadMonthlyAppointments(ActionEvent actionEvent) {
        ObservableList<Appointment> monthlyAppointment;
        ObservableList<Appointment> appointments;
        if(monthlyApp.isSelected()){
            try{
                monthlyAppointment = JDBCAppointments.getMonthlyAppointments();
                appointmentTable.setItems(monthlyAppointment);

                appointmentTable.refresh();

                if(appointmentTable.getItems().isEmpty()){
                    /**
                     * Lambda Expression
                     */
                    alertService.showAlert("Information", null, "You Have no monthly appointments", Alert.AlertType.INFORMATION);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                appointments = JDBCAppointments.getAllAppointments();
                appointmentTable.setItems(appointments);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * This method loads the Report view when the Reports button is clicked
     * @param actionEvent
     * @throws IOException
     */
    public void loadReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ReportView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 800);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is used to create alerts if there is an upcoming appointment within 15 minutes of the user's localtime
     */
    private void alertAppointment(){
        LocalDateTime localDateTime = LocalDateTime.now();

        ZoneId estZoneID = ZoneId.of("America/New_York");
        ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());

        ZonedDateTime localZoneDateTime = ZonedDateTime.of(localDateTime, localZoneID);
        ZonedDateTime estZoneDateTime = localZoneDateTime.withZoneSameInstant(estZoneID);

        ZonedDateTime estDateTimePlus15 = estZoneDateTime.plusMinutes(15);

        String startESTTime = String.valueOf(estZoneDateTime.toLocalTime());
        String estTimePLus15 = String.valueOf(estDateTimePlus15.toLocalTime());

        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();


        try {
            ObservableList<Appointment> appointments = JDBCAppointments.getAllAppointments();

            if (appointments != null) {
                for (Appointment appointment: appointments) {
                    if (appointment.getStartTime().isAfter(ChronoLocalDateTime.from(estZoneDateTime)) && appointment.getStartTime().isBefore(ChronoLocalDateTime.from(estDateTimePlus15))) {
                        upcomingAppointments.add(appointment);

                        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                            alertService.showAlert("Notice", null, "You have an upcoming appointment \nAppointment_ID: " + appointment.getAppointmentID() + "\nOn date: " + appointment.getStartDate() + "\nTime: " + appointment.getStartTime(), Alert.AlertType.INFORMATION);

                        }
                    }
                }

                if (upcomingAppointments.size() < 1) {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    if(Locale.getDefault().getLanguage() == "fr"){
                        info.setContentText("Vous n’avez pas de rendez-vous à venir :");
                    }
                    else{
                    alertService.showAlert("Notice", null, "You have no upcoming appointments", Alert.AlertType.INFORMATION);
                    }
                }
            }
            else {
                if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                    alertService.showAlert("Notice", null, "Test", Alert.AlertType.INFORMATION);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
