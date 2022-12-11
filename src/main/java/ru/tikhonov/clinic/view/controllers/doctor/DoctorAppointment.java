package ru.tikhonov.clinic.view.controllers.doctor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import ru.tikhonov.clinic.App;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.DoctorDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DoctorAppointment {

    @FXML
    private Button approveButton;

    @FXML
    private Button backButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField timeField;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorGui.fxml"));
        });

        approveButton.setOnAction(actionEvent -> {
            try {
                String date = dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-dd-MM"));
                LocalTime time = LocalTime.parse(timeField.getText());
                try {
                    DoctorDao.insertAppointment(App.currentUser, date, timeField.getText());
                    App.setAlert("Прием", Alert.AlertType.CONFIRMATION, "Прием назначен");
                    App.setScene("Доктор", getClass().getClassLoader().getResource("doctorGui.fxml"));
                } catch (DaoException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
                }
            } catch (Exception e) {
                //e.printStackTrace();
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Неправильно введены данные");
            }
        });

    }

}
