package Lambdas;

import javafx.scene.control.Alert;

/**
 * This interface is used to show an alert and has input parameters
 */
@FunctionalInterface
public interface AlertBlankInput {
    void showAlert(String title, String header, String content, Alert.AlertType alertType);
}
