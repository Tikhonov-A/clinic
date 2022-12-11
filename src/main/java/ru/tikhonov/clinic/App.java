package ru.tikhonov.clinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ru.tikhonov.clinic.domain.Appointment;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class App extends Application {

    public static String currentUser;

    public static String currentAppointmentDate;
    public static String currentAppointmentTime;

    public static Stage stage;
    public static int currentAppointment;
    public static String currentProcedure;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        setScene("Клиника", getClass().getClassLoader().getResource("auth.fxml"));
    }
    //doctorEdit2Appointment
    public static void setScene(String title, URL url) {
        Parent root = null;
        try {
            root = FXMLLoader.load(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        App.stage.setTitle(title);
        App.stage.setScene(new Scene(root, 700, 400));
        App.stage.show();
    }

    public static void setAlert(String title, Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
