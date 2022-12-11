package ru.tikhonov.clinic.view.controllers.admin;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;

public class AdminAddDiagnosis {


    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> categoryPicker;

    @FXML
    private Button chooseButton;

    @FXML
    private TextField nameField;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminGui.fxml"));
        });

        nameField.setDisable(true);

        categoryPicker.setItems(AdminDao.getSpecialities());

        chooseButton.setOnAction(actionEvent -> {
            if (categoryPicker.getValue() == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Выберите категорию");
            } else {
                nameField.setDisable(false);
            }
        });

        addButton.setOnAction(actionEvent -> {
            if (nameField.getText().equals("")) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Введите все данные");
            } else {
                try {
                    AdminDao.addDiagnosis(categoryPicker.getValue(), nameField.getText());
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