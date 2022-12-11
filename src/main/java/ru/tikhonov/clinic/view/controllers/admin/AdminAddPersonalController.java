package ru.tikhonov.clinic.view.controllers.admin;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;

public class AdminAddPersonalController {

    @FXML
    public TextField innField;

    @FXML
    private Button backButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField patronymicField;

    @FXML
    private ChoiceBox<String> specialityField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField surnameField;

    @FXML
    private RadioButton adminRadio;

    @FXML
    private RadioButton doctorRadio;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminGui.fxml"));
        });

        specialityField.setDisable(true);

        doctorRadio.setOnAction(actionEvent -> {
            specialityField.setDisable(false);
        });

        adminRadio.setOnAction(actionEvent -> {
            specialityField.setDisable(true);
        });

        specialityField.setItems(AdminDao.getSpecialities());

        registerButton.setOnAction(actionEvent -> {
            try {
                String name = nameField.getText().trim();
                String surname = surnameField.getText().trim();
                String patronymic = patronymicField.getText().trim();
                String login = loginField.getText().trim();
                String password = passwordField.getText().trim();
                Long inn = Long.parseLong(innField.getText().trim());
                if(adminRadio.isSelected()) {
                    AdminDao.insertPersonal(
                            name, surname, patronymic,
                            login, password, null, 1, inn
                    );
                } else if(doctorRadio.isSelected()) {
                    String speciality = specialityField.getValue();
                    if (speciality.equals("")) {
                        throw new RuntimeException("Не выбрана специальность");
                    }
                    AdminDao.insertPersonal(
                            name, surname, patronymic,
                            login, password, speciality, 2, inn
                    );
                } else {
                    throw new RuntimeException("Не нажата кнопка");
                }
                App.setAlert("Регситрация", Alert.AlertType.CONFIRMATION, "Регистрация проошла успешно");
            } catch(NumberFormatException e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "ИНН должен быть целый числом");
            } catch(DaoException e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Dao exception");
            } catch(RuntimeException e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
            }
        });
    }
}
