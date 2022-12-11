package ru.tikhonov.clinic.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.tikhonov.clinic.domain.PasswordUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    public static void insertPersonal(String name, String surname,
                                      String patronymic, String username,
                                      String password, String speciality, int role, Long innNumber) throws  DaoException {
        String sql = "INSERT INTO Personal(name, surname, patronymic, " +
                "role_id, username, hashed_password, salt, speciality_id, inn) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, patronymic);
            statement.setInt(4, role);
            statement.setString(5, username);
            String salt = PasswordUtils.generateSalt(24).get();
            statement.setString(6, PasswordUtils.hashThePlainTextPassword(password, salt).get());
            statement.setString(7, salt);
            statement.setLong(9, innNumber);
            if (role == 1) {
                statement.setString(8,null);
            } else {
                int special = getSpeciality(speciality);
                if (special != 0) {
                    statement.setInt(8, special);
                } else {
                    throw new DaoException("Нет такой специальности");
                }
            }
            try {
                statement.executeUpdate();
            } catch(SQLException e) {
                e.printStackTrace();
                throw new DaoException("Такое имя ползователя уже существует");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    private static int getSpeciality(String speciality) {
        String sql = "SELECT id FROM Speciality WHERE name = ?";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, speciality);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt("id");
                }
            } catch(SQLException e) {
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
        return 0;
    }

    public static ObservableList<String> getSpecialities() {
        String sql = "SELECT Speciality.name FROM Speciality";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while(resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Cannot close sql connection, statement or result set");
        }
        return FXCollections.observableList(result);
    }

    public static void addSpeciality(String name, double money) throws  DaoException {
        String sql = "INSERT INTO Speciality(name, appointment_income) VALUES(?, ?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            statement.setDouble(2, money);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static void addDiagnosis(String speciality, String name) throws  DaoException {
        String sql = "INSERT INTO Diagnosis VALUES((SELECT Speciality.id FROM Speciality WHERE Speciality.name = ?), ?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, speciality);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static void addProcedure(String speciality, String name, double price) throws  DaoException {
        String sql = "INSERT INTO [Procedure] VALUES((SELECT Speciality.id FROM Speciality WHERE Speciality.name = ?), ?, ?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, speciality);
            statement.setString(2, name);
            statement.setDouble(3, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static void addMedicine(String category, String name, double price) throws  DaoException {
        String sql = "INSERT INTO Medcine VALUES(?, (SELECT Medcine_category.id FROM Medcine_category WHERE Medcine_category.category = ?), ?, ?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            statement.setString(2,category);
            statement.setDouble(3, 0);
            statement.setDouble(4, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close statement or connection");
        }
    }

    public static void addMedicineCategory(String category) throws  DaoException {
        String sql = "INSERT INTO Medcine_category VALUES(?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static void addBonus(long inn, double bonus, String comment) throws  DaoException {
        String sql = "INSERT INTO Bonus VALUES((SELECT Personal.id FROM Personal WHERE " +
                "Personal.inn = ?), ?, ?, ?) ";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, inn);
            statement.setDouble(2,bonus);
            statement.setString(3, comment);
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            statement.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close statement or connection");
        }
    }

    public static ObservableList<String> getMedicineCategory() {
        String sql = "SELECT Medcine_category.category FROM Medcine_category";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while(resultSet.next()) {
                result.add(resultSet.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Cannot close sql connection, statement or result set");
        }
        return FXCollections.observableList(result);
    }

    public static ObservableList<String> getPersonal() {
        String sql = "SELECT Personal.inn, CONCAT(Personal.[name], ' ', Personal.surname, ' ', Personal.patronymic) as FIO " +
                "FROM Personal";

        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while(resultSet.next()) {
                result.add(resultSet.getString("inn") + " " + resultSet.getString("FIO"));
            }
        } catch (SQLException e) {
            System.err.println("Cannot close sql connection, statement or result set");
        }
        return FXCollections.observableList(result);
    }

    public static ObservableList<String> getMedicine() {
        String sql = "SELECT Medcine.name FROM Medcine";

        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while(resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Cannot close sql connection, statement or result set");
        }
        return FXCollections.observableList(result);
    }

    public static void fillMedicine(String medicine, int countNum) throws DaoException {
        String sql = "UPDATE Medcine SET Medcine.[count] = Medcine.[count] + ? WHERE Medcine.name = ?";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, countNum);
            statement.setString(2, medicine);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close statement or connection");
        }
    }
}
