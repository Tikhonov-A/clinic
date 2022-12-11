package ru.tikhonov.clinic.view.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;

public class AdminAddSpeciality {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Аутентификация", getClass().getClassLoader().getResource("adminGui.fxml"));
        });

        addButton.setOnAction(actionEvent -> {
            if (nameField.getText().equals("") || priceField.getText().equals("")) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Введите все данные");
            } else {
                try {
                    AdminDao.addSpeciality(nameField.getText(), Double.parseDouble(priceField.getText()));
                    App.setAlert("Добавление", Alert.AlertType.CONFIRMATION, "Успешно добавлено");
                } catch (NumberFormatException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, "Цена должна быть числом");
                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

}
