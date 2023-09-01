package Lambdas;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * This Lambda interface is used to create the confirmation alerts
 */
public interface AlertConfirmation {
    static void showConfirmation(String title, String message, Runnable onConfirm){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            onConfirm.run();
        }
    }
}
