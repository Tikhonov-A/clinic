package ru.tikhonov.clinic.view.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;

public class AdminAddBonus {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField bonusField;

    @FXML
    private ComboBox<String> personField;

    @FXML
    private Button chooseButton;

    @FXML
    private TextArea commentField;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminGui.fxml"));
        });

        bonusField.setDisable(true);
        commentField.setDisable(true);

        personField.setItems(AdminDao.getPersonal());

        chooseButton.setOnAction(actionEvent -> {
            if (personField.getValue() == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Выберите категорию");
            } else {
                bonusField.setDisable(false);
                commentField.setDisable(false);
            }
        });

        addButton.setOnAction(actionEvent -> {
            if (bonusField.getText().equals("") || commentField.getText().equals("")) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Введите все данные");
            } else {
                try {
                    String person = personField.getValue();
                    AdminDao.addBonus(Long.parseLong(person.substring(0, person.indexOf(' '))),
                            Double.parseDouble(bonusField.getText()), commentField.getText());
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
