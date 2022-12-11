package ru.tikhonov.clinic.view.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;

public class AdminAddMedicineCategory {


    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField categoryField;

    @FXML
    void initialize() {

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminGui.fxml"));
        });

        addButton.setOnAction(actionEvent -> {
            if (categoryField.getText().equals("")) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Введите имя");
            } else {
                try {
                    AdminDao.addMedicineCategory(categoryField.getText());
                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
                App.setAlert("Добавление", Alert.AlertType.CONFIRMATION, "Успешно добавлено");
            }
        });

    }

}
