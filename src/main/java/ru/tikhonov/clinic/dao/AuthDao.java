package ru.tikhonov.clinic.dao;

import javafx.scene.control.Alert;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.domain.PasswordUtils;
import ru.tikhonov.clinic.domain.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDao {


    public static UserInfo authentificate(String username, String plainPassword) {
        String sql1 = "SELECT hashed_password, salt, NULL as role FROM Patient WHERE username = ? UNION " +
                      "SELECT Personal.hashed_password, Personal.salt, Role.role FROM Personal " +
                      "JOIN Role on Role.id=Personal.role_id " +
                      "WHERE username = ?";
        UserInfo userInfo = null;
        try (Connection connection = DaoFactory.getConnection(); PreparedStatement statement = connection.prepareStatement(sql1)) {
            statement.setString(1, username);
            statement.setString(2, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    try {
                        userInfo = new UserInfo(username,
                                resultSet.getString("hashed_password"),
                                resultSet.getString("salt"),
                                resultSet.getString("role"));
                    } catch (SQLException e) {
                        userInfo = new UserInfo(username,
                                resultSet.getString("hashed_password"),
                                resultSet.getString("salt"),
                                null);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //System.err.println("Cannot close statement or connection");
        }
        if (userInfo == null) {
            App.setAlert("Ошбика", Alert.AlertType.INFORMATION, "Вы не зарегестрированы");
            return null;
        }
        if (!PasswordUtils.verifyThePlainTextPassword(plainPassword, userInfo.hashed_password, userInfo.salt)) {
            App.setAlert("Ошбика", Alert.AlertType.INFORMATION, "Неправильный пароль");
            return null;
        } else {
            return userInfo;
        }
    }

}
