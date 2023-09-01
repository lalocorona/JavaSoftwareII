package controller;

import Lambdas.AlertBlank;
import Lambdas.AlertBlankInput;
import Lambdas.WriteToFile;
import Lambdas.WritingToFile;
import helper.JDBCAppointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import helper.JDBCUsers;
import model.Appointment;

import java.io.IOException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.io.*;

/**
 * This controller is used to control the login functions of the application. It has logical validation
 * checks with the MySQL database to ensure that the correct username and password matches. All login attempts
 * are written to a file called "login_activity.txt".
 *
 * There is also Lambda implementation for writing to the file.
 */
public class Login implements Initializable {
    public Label timeLabel;
    private AlertBlankInput alertService = new AlertBlank();

    public Button login;
    public Label LoginLabel;
    public Label timeZoneLabel;
    public Label zoneID;
    private Parent scene;
    public Label TheLabel;
    public TextField userNameInput;
    public PasswordField passwordInput;
    public Label timeZoneChange;


    /**
     * This initializes the program as well as changing your timezone based on the system's Time Zone
     **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TheLabel.setText("Language English");
        zoneID.setText(ZoneId.systemDefault().getId());
        timeZoneChange.setText(TimeZone.getDefault().getDisplayName());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        timeLabel.setText((LocalDateTime.now().format(formatter)));

        String curLanguage = Locale.getDefault().getLanguage();

        if(curLanguage == "fr"){
            userNameInput.setPromptText("Utilisatuer");
            passwordInput.setPromptText("Passe");
            TheLabel.setText("Language French");
            LoginLabel.setText("Connexion utilisateur");
            timeZoneLabel.setText("Fuseau horaire actuel");
            login.setText("Connexion");

        }
    }

    /**
     * Lambda takes in a parameter and writes the content to a file
     */
    interface WritingToFile {
        void write(String content) throws IOException;
    }

    /**
     * This method has logical validation, checking if the inputs in the login form
     * match the username and password in the database. If valid the user is logged in to the scheduling application.
     * @param actionEvent
     * @throws SQLException
     */
    public void loginUser(ActionEvent actionEvent) throws SQLException, IOException {
        boolean valid = JDBCUsers.selectAllUser(userNameInput.getText(), passwordInput.getText());

        try {
            if (valid) {

                String filePath = ("login_activity.txt");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());

                /**Lambda Expression used here*/
                WritingToFile write = (content) -> {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
                        writer.write(content);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                };

                String content = "User attempted to log in Username: " + userNameInput.getText() +
                        ". Password: " + passwordInput.getText() + ", at Timestamp: " + simpleDateFormat.format(date) +
                        " was successful";
                try{
                    write.write(content);
                    System.out.println("Login Successful data being written");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                Stage stage = (Stage)((Button) actionEvent.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Appointment View");
                stage.show();

                System.out.println("The username matches!");
            }

            else if (userNameInput.getText().isEmpty() && passwordInput.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(Locale.getDefault().getLanguage() == "fr") {
                    alert.setContentText("Vous ne pouvez pas laisser les deux champs vides");
                }
                else{
                    alert.setContentText("You cannot leave both fields blank");
                }
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(Locale.getDefault().getLanguage() == "fr") {
                    alert.setContentText("Erreur de nom d’utilisateur ou de mot de passe incorrect.");
                }
                else{
                    /**Lambda Expression used here*/
                    String filePath = ("login_activity.txt");

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());

                    /**Lambda Expression used here*/
                    WritingToFile write = (content) -> {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
                            writer.write(content);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    };

                    String content = "User attempted to log in Username: " + userNameInput.getText() + ". Password: " + passwordInput.getText() + ", at Timestamp: " + simpleDateFormat.format(date) + " was successful";
                    try{
                        write.write(content);
                        System.out.println("Login Unsuccessful data being written");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    alert.setContentText("Error incorrect username or password.");
                }
                alert.showAndWait();
            }
            }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Getting an error in trying to log in.");
            alert.showAndWait();
        }

        JDBCUsers.convertUserName(userNameInput.getText());

    }

    private void write(String logMessage) {
    }

    private void alertAppointment(){
        LocalDateTime localDateTime = LocalDateTime.now();

        ZoneId estZoneID = ZoneId.of("America/New_York");
        ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());

        ZonedDateTime localZoneDateTime = ZonedDateTime.of(localDateTime, localZoneID);
        ZonedDateTime estZoneDateTime = localZoneDateTime.withZoneSameInstant(estZoneID);

        ZonedDateTime estDateTimePlus15 = estZoneDateTime.plusMinutes(15);


        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();


        try {
            ObservableList<Appointment> appointments = JDBCAppointments.getAllAppointments();

            if (appointments != null) {
                for (Appointment appointment: appointments) {
                    if (appointment.getStartTime().isAfter(ChronoLocalDateTime.from(estZoneDateTime)) && appointment.getStartTime().isBefore(ChronoLocalDateTime.from(estDateTimePlus15))) {
                        upcomingAppointments.add(appointment);

                        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                            alertService.showAlert("Notice", null, "You have an appointmentID: " + appointment.getAppointmentID() + " On date: " + appointment.getStartDate(), Alert.AlertType.INFORMATION);

                        }
                    }
                }

                if (upcomingAppointments.size() < 1) {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    if(Locale.getDefault().getLanguage() == "fr"){
                        info.setContentText("Vous n’avez pas de rendez-vous à venir :");
                        info.showAndWait();
                    }
                    else{
                        alertService.showAlert("Notice", null, "You have no upcoming appointments", Alert.AlertType.INFORMATION);
                    }

                    }
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
