package Main;


import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    /**This class sets the stage and loads the login screen
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentView.fxml"));
        //primaryStage.setTitle("Login View");
        //primaryStage.setScene(new Scene(root,1400, 600));
        primaryStage.setScene(new Scene(root,600, 500));
        primaryStage.show();

    }

    /**This class uses the model class from JDBC to make a connection to the mySQL database
     *
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

        launch(args);
    }
}