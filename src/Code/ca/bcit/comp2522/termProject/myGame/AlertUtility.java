package ca.bcit.comp2522.termProject.myGame;

import javafx.scene.control.Alert;

public class AlertUtility {
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}