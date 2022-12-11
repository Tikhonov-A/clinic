package ru.tikhonov.clinic.view.controllers.patient;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.PatientDao;

public class PatientBuyProcedure {

    @FXML
    private Button backButton;

    @FXML
    private Button buyButton;

    @FXML
    private ComboBox<String> procedurePicker;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Аутентификация", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        procedurePicker.setItems(PatientDao.getProcedures());

        buyButton.setOnAction(actionEvent -> {
            if (procedurePicker.getValue() == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Выберите процедуру");
            } else {
                try {
                    String procedure = procedurePicker.getValue();
                    String procedureRes = procedure.substring(0, procedure.indexOf(' '));
                    PatientDao.buyProcedure(procedureRes);
                    App.setAlert("Покупка", Alert.AlertType.CONFIRMATION, "Куплено успешно");
                    App.setScene("Аутентификация", getClass().getClassLoader().getResource("patientGui.fxml"));
                } catch (DaoException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
                }
            }
        });

    }

}