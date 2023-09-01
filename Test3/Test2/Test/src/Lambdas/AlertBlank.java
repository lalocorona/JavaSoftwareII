package Lambdas;

import javafx.scene.control.Alert;

public class AlertBlank implements AlertBlankInput{
    @Override
    public void showAlert(String title,String header, String content, Alert.AlertType alertType) {

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
