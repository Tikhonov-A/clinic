package ru.tikhonov.clinic.view.controllers.doctor;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.DoctorDao;
import ru.tikhonov.clinic.dao.PatientDao;
import ru.tikhonov.clinic.domain.Appointment;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DoctorEdit1Appointment {

    @FXML
    private Button backButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private Button editAppointment;

    @FXML
    private Button findAppointment;

    @FXML
    private ComboBox<String> appointments;

    @FXML
    void initialize() {

        appointments.setDisable(true);

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorGui.fxml"));
        });

        findAppointment.setOnAction(actionEvent -> {
            try {
                LocalDate.parse(dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                String date = dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                try {
                    ObservableList<String> listAppointments = DoctorDao.findAppointments(date);
                    if (!listAppointments.isEmpty()) {
                        appointments.setItems(listAppointments);
                        appointments.setDisable(false);
                    }
                } catch (DaoException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
                }
            } catch (Exception e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Нет такой даты");
            }
        });

        editAppointment.setOnAction(actionEvent -> {
            String appointmentDatetime = appointments.getValue();
            App.currentAppointmentDate = appointmentDatetime.substring(0, 10);
            App.currentAppointmentTime = appointmentDatetime.substring(11, 16);
            System.out.println(App.currentAppointmentDate);
            System.out.println(App.currentAppointmentTime);
            if (appointmentDatetime == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Выберните прием");
            } else {
                App.setScene("Доктор", getClass().getClassLoader().getResource("doctorEdit2Appointment.fxml"));
            }
        });

    }
}
