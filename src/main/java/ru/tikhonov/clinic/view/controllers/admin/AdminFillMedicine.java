package ru.tikhonov.clinic.view.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;

public class AdminFillMedicine {
    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> medicinePicker;

    @FXML
    private Button chooseButton;

    @FXML
    private TextField countField;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminGui.fxml"));
        });

        countField.setDisable(true);

        medicinePicker.setItems(AdminDao.getMedicine());

        chooseButton.setOnAction(actionEvent -> {
            if (medicinePicker.getValue() == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Выберите лекарство");
            } else {
                countField.setDisable(false);
            }
        });

        addButton.setOnAction(actionEvent -> {
            if (medicinePicker.getValue() == null || countField.getText().equals("")) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Введите все данные");
            } else {
                try {
                    AdminDao.fillMedicine(medicinePicker.getValue(), Integer.parseInt(countField.getText()));
                    App.setAlert("Добавление", Alert.AlertType.CONFIRMATION, "Успешно добавлено");
                } catch (NumberFormatException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, "Количество должна быть числом");
                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

}