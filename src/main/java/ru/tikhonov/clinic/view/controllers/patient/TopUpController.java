package ru.tikhonov.clinic.view.controllers.patient;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.PatientDao;

public class TopUpController {


    @FXML
    private Button button;

    @FXML
    private TextField textField;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        button.setOnAction(actionEvent -> {
            double cash = 0;
            try {
                cash = Double.parseDouble(textField.getText().trim());
                PatientDao.addCash(cash);
                App.setAlert("Пополнение баланса", Alert.AlertType.CONFIRMATION, "Баланс пополнен");
            } catch (NumberFormatException e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Неправильно введены данные");
            }
        });

    }

}
