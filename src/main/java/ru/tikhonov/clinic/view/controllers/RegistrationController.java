package ru.tikhonov.clinic.view.controllers;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.PatientDao;

public class RegistrationController {

    @FXML
    private Button backButton;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private TextField loginField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField patronymicField;

    @FXML
    private TextField polisField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField surnameField;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            registerButton.getScene().getWindow().hide();
            App.setScene("Авторизация", getClass().getClassLoader().getResource("auth.fxml"));
        });

        registerButton.setOnAction(actionEvent -> {
            try {
                String name = nameField.getText().trim();
                String surname = surnameField.getText().trim();
                String patronymic = patronymicField.getText().trim();
                Date birthday = Date.valueOf(birthdayField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                long polis = Long.parseLong(polisField.getText().trim());
                String login = loginField.getText().trim();
                String password = passwordField.getText().trim();
                try {
                    PatientDao.insertPatient(name, surname, patronymic, birthday, polis, login, password);
                    App.setAlert("Регистрация", Alert.AlertType.CONFIRMATION, "Регистрация прошла успешно");
                    App.setScene("Авторизация", getClass().getClassLoader().getResource("auth.fxml"));
                } catch(DaoException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
                }
            } catch(Exception e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Неправильно введены данные");
            }
        });

    }
}