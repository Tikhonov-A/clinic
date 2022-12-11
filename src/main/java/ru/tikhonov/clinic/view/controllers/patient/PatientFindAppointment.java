package ru.tikhonov.clinic.view.controllers.patient;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.DoctorDao;
import ru.tikhonov.clinic.dao.PatientDao;

public class PatientFindAppointment {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> doctorField;

    @FXML
    private Button enrollButton;

    @FXML
    private Button findButton;

    @FXML
    private ComboBox<String> itogPicker;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Аутентификация", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        itogPicker.setDisable(true);

        doctorField.setItems(AdminDao.getSpecialities());

        findButton.setOnAction(actionEvent -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                String doctor = doctorField.getValue();
                App.currentAppointmentDate = dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                try {
                    ObservableList<String> listAppointments = PatientDao.findAppointments(doctor, date);
                    if (!listAppointments.isEmpty()) {
                        itogPicker.setItems(listAppointments);
                        itogPicker.setDisable(false);
                    } else {
                        App.setAlert("Запись на прием", Alert.AlertType.INFORMATION, "Нет номерков");
                    }
                } catch (DaoException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
                }
            } catch (Exception e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Нет такой даты");
            }
        });

        enrollButton.setOnAction(actionEvent -> {
            String fullDate = itogPicker.getValue();
            if (fullDate == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Не выбран прием");
            } else {
                App.currentAppointmentDate = fullDate.substring(0, 10);
                App.currentAppointmentTime = fullDate.substring(11, 16);
                PatientDao.enrollToAppointment(fullDate);
                App.setAlert("Запись на прием", Alert.AlertType.CONFIRMATION, "Вы записались успешно");
            }
        });

    }
}