package ru.tikhonov.clinic.view.controllers.patient;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.PatientDao;

public class PatientMakeReview {

    @FXML
    public TextArea textArea;

    @FXML
    private Button backButton;

    @FXML
    private Button enrollButton;

    @FXML
    private ComboBox<String> findPicker;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Аутентификация", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        findPicker.setItems(PatientDao.findMineAppointments());

        enrollButton.setOnAction(actionEvent -> {
            String fullDate = findPicker.getValue();
            if (fullDate == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Не выбран прием");
            } else {
                App.currentAppointmentDate = fullDate.substring(0, 10);
                App.currentAppointmentTime = fullDate.substring(11, 16);
                String review = textArea.getText();
                PatientDao.makeReview(review);
                App.setAlert("Отзыв", Alert.AlertType.INFORMATION, "Отзыв оставлен успешно");
            }
        });

    }

}
