package ru.tikhonov.clinic.view.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.AuthDao;
import ru.tikhonov.clinic.domain.UserInfo;

public class AuthController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button signUpButton;

    @FXML
    void initialize()  {
        registerButton.setOnAction(actionEvent -> {
            registerButton.getScene().getWindow().hide();
            App.setScene("Регистрация", getClass().getClassLoader().getResource("registration.fxml"));
        });

        signUpButton.setOnAction(actionEvent -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();
            UserInfo userInfo = AuthDao.authentificate(login, password);
            if (userInfo != null) {
                App.currentUser = userInfo.username;
                if (userInfo.role == null) {
                    registerButton.getScene().getWindow().hide();
                    App.setScene("Пациент", getClass().getClassLoader().getResource("patientGui.fxml"));
                } else if (userInfo.role.equals("admin")) {
                    registerButton.getScene().getWindow().hide();
                    App.setScene("Админ", getClass().getClassLoader().getResource("adminGui.fxml"));
                } else {
                    registerButton.getScene().getWindow().hide();
                    App.setScene("Доктор", getClass().getClassLoader().getResource("doctorGui.fxml"));
                }
            }
        });

    }

}
