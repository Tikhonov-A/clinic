package ru.tikhonov.clinic.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.domain.*;

import javax.print.DocFlavor;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PatientDao {
    public static void insertPatient(String name, String surname, String patronymic, Date birthday,
                                     long polis_number, String username, String password) throws  DaoException {
        String sql = "INSERT INTO Patient(name, surname, patronymic, birthday, polis_number, " +
                "username, hashed_password, salt) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection connection = DaoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, patronymic);
            statement.setDate(4, birthday);
            statement.setLong(5, polis_number);
            statement.setString(6, username);
            String salt = PasswordUtils.generateSalt(24).get();
            statement.setString(7, PasswordUtils.hashThePlainTextPassword(password, salt).get());
            statement.setString(8, salt);
            try {
                statement.execute();
            } catch(SQLException e) {
                throw new DaoException("Такое имя ползователя уже существует");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static Patient findByUsername(String username) throws  DaoException {
        String sql = "SELECT * FROM Patient WHERE Patient.username = ?";
        Patient patient = null;
        try (Connection connection = DaoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    patient = new Patient(
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("patronymic"),
                            resultSet.getDate("birthday"),
                            resultSet.getLong("polis_number"),
                            resultSet.getDouble("cash")
                    );
                }
            } catch (SQLException e) {
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
        if (patient == null) {
            throw new DaoException("Cannot find patient with that username");
        }
        return patient;
    }

    public static void addCash(double cash) {
        String sql = "Update Patient SET cash = cash + ? WHERE Patient.username = ?";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDouble(1, cash);
            statement.setString(2, App.currentUser);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Cannot close result set");
        }
    }

    public static double getCash() {
        String sql = "SELECT Patient.cash FROM Patient WHERE Patient.username = ?";
        double result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getDouble("cash");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ObservableList<Operations> getOperations() {
        String sql = "SELECT Appointment.[date] as date, [Procedure].[name] AS product, [Procedure].price AS price FROM Patient\n" +
                "JOIN Appointment ON Patient.id = Appointment.patient_id\n" +
                "JOIN AppointmentProcedure ON AppointmentProcedure.appointment_id = Appointment.id\n" +
                "JOIN [Procedure] ON AppointmentProcedure.procedure_id = [Procedure].id\n" +
                "WHERE Patient.username = ? " +
                "UNION " +
                "SELECT Order_medcine.[date] as date, Medcine.[name] AS product, Medcine.price AS price FROM Patient\n" +
                "JOIN Order_medcine ON Patient.id = Order_medcine.patient_id " +
                "JOIN Medcine ON Medcine.id = Order_medcine.medcine_id " +
                "WHERE Patient.username = ?";

        List<Operations> operations = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, App.currentUser);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    operations.add(new Operations(
                            resultSet.getString("date").substring(0, 16),
                            resultSet.getString("product"),
                            resultSet.getDouble("price")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close connection or statement");
        }
        return FXCollections.observableList(operations);
    }


    public static ObservableList<Diseases> getDiseases() {
        String sql = "SELECT Appointment.[date] as date, Diagnosis.[name] as diagnosis FROM Patient\n" +
                "JOIN Appointment ON Appointment.patient_id = Patient.id\n" +
                "LEFT JOIN AppointmentDiagnosis ON AppointmentDiagnosis.appointment_id = Appointment.id\n" +
                "LEFT JOIN Diagnosis ON Diagnosis.id = AppointmentDiagnosis.diagnosis_id\n" +
                "WHERE Patient.username= ?";

        List<Diseases> diseases = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    diseases.add(new Diseases(
                            resultSet.getString("date").substring(0, 16),
                            resultSet.getString("diagnosis")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close connection or statement");
        }
        return FXCollections.observableList(diseases);
    }

    public static ObservableList<String> findAppointments(String doctor, LocalDate date) throws  DaoException {
        String sql = "SELECT Appointment.date FROM Speciality " +
                "JOIN Personal ON Personal.speciality_id = Speciality.id " +
                "JOIN Appointment on Personal.id = Appointment.personal_id " +
                "WHERE Speciality.name = ? and CONVERT(date, Appointment.[date]) LIKE ? and Appointment.status_id = 3";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, doctor);
            statement.setString(2, App.currentAppointmentDate);
            System.out.println(doctor);
            System.out.println(date.getYear() + "-" + date.getDayOfMonth() + "-" +date.getMonthValue());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getString("date").substring(0, 16));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return FXCollections.observableList(result);
    }

    public static int enrollToAppointment(String fullDate) {

        String sql = "UPDATE Appointment SET patient_id = (SELECT Patient.id FROM Patient WHERE Patient.username = ?)," +
                "Appointment.status_id = 2" +
                "WHERE Appointment.id = (SELECT Appointment.id FROM Appointment " +
                "WHERE CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?)";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement or result set");
        }
        return result;
    }

    public static ObservableList<String> findMineAppointments() {
        String sql = "SELECT Appointment.date FROM Appointment " +
                "JOIN Patient ON Patient.id = Appointment.patient_id " +
                "WHERE Patient.username = ? and Appointment.status_id = 1";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getString("date").substring(0, 16));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return FXCollections.observableList(result);
    }

    public static int makeReview(String review) {
        String sql = "UPDATE Appointment SET review = ? " +
                "WHERE Appointment.id = (SELECT Appointment.id FROM Appointment " +
                "WHERE CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?)";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, review);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
            System.out.println("%" + App.currentAppointmentTime + "%");
            System.out.println(App.currentAppointmentDate + "%");
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement or result set");
        }
        return result;

    }

    public static ObservableList<String> getCategories() {
        String sql = "SELECT Medcine_category.category FROM Medcine_category";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getString("category"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return FXCollections.observableList(result);
    }

    public static ObservableList<String> getMedicine(String category) {
        String sql = "SELECT Medcine.name FROM Medcine " +
                "WHERE category_id = (SELECT Medcine_category.id FROM Medcine_category " +
                "WHERE Medcine_category.category = ?)";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return FXCollections.observableList(result);

    }

    public static void buyMedicine(String product, int count) throws DaoException {
        try (Connection connection = DaoFactory.getConnection();
        ) {
            if (PatientDao.getMedicineCount(product) < count) {
                throw new DaoException("Нет такого количества");
            }

            if (PatientDao.getPatientCash() < count * PatientDao.getMedcinePrice(product)) {
                throw new DaoException("Недостаточно средств");
            }
            connection.setAutoCommit(false);
            try {
                PatientDao.insertOrderMedcine(product, count);
                PatientDao.getMoney(count * PatientDao.getMedcinePrice(product));
                PatientDao.decreaseCount(count, product);
            } catch(Exception e) {
                connection.rollback();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
    }

    private static int decreaseCount(int count, String product) throws  Exception {
        String sql = "UPDATE Medcine SET count = count - ? " +
                "WHERE Medcine.name = ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setDouble(1, count);
            statement.setString(2, product);
            result = statement.executeUpdate();
        }
        return result;
    }

    private static int getMoney(double cash) throws  Exception {
        String sql = "UPDATE Patient SET cash = cash - ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setDouble(1, cash);
            result = statement.executeUpdate();
        }
        return result;
    }

    private static void insertOrderMedcine(String product, int count) throws  Exception {
        String sql = "INSERT INTO Order_medcine VALUES ((" +
                "SELECT Patient.id FROM Patient WHERE Patient.username = ?), " +
                "(SELECT Medcine.id FROM Medcine WHERE Medcine.name = ?), ?, ?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, product);
            statement.setInt(3, count);
            statement.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-dd-MM"))
                    + " " + LocalDateTime.now().toString().substring(11, 16));
            statement.executeUpdate();

        }
    }

    private static double getPatientCash() {
        String sql = "SELECT Patient.cash as cash FROM Patient " +
                "WHERE Patient.username = ?";
        double cash = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    cash = rs.getDouble("cash");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return cash;
    }

    private static double getMedcinePrice(String product) {
        String sql = "SELECT Medcine.price as price FROM Medcine " +
                "WHERE Medcine.name = ?";
        double price = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, product);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("price");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return price;
    }

    private static int getMedicineCount(String medicine) throws  DaoException {
        String sql = "SELECT Medcine.count as count FROM Medcine " +
                "WHERE Medcine.name = ?";
        int count = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, medicine);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("count");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return count;
    }

    public static ObservableList<String> getProcedures() {
        String sql = "SELECT [Procedure].[name], Appointment.id, [Procedure].price FROM [Procedure] " +
                "JOIN AppointmentProcedure ON AppointmentProcedure.procedure_id = [Procedure].[id] " +
                "JOIN Appointment ON Appointment.id = AppointmentProcedure.appointment_id " +
                "JOIN Patient ON Patient.id = Appointment.patient_id " +
                "WHERE Patient.username = ? and AppointmentProcedure.status_id = ?";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            statement.setInt(2, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    App.currentAppointment = resultSet.getInt("id");
                    App.currentProcedure = resultSet.getString("name");
                    result.add(resultSet.getString("name") + " " + resultSet.getString("price").substring(0, 6) + " рублей");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return FXCollections.observableList(result);
    }

    public static void buyProcedure(String procedure) throws DaoException {
        try (Connection connection = DaoFactory.getConnection();
        ) {
            if (PatientDao.getPatientCash() < PatientDao.getProcedurePrice(procedure)) {
                throw new DaoException("Недостаточно средств");
            }
            connection.setAutoCommit(false);
            try {
                PatientDao.changeOrderProcedureStatus(procedure);
                PatientDao.getMoney(PatientDao.getProcedurePrice(procedure));
            } catch(Exception e) {
                e.printStackTrace();
                connection.rollback();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        } 
    }

    private static int changeOrderProcedureStatus(String procedure) throws  Exception {
        String sql = "UPDATE AppointmentProcedure SET status_id = 2 FROM [Procedure] " +
                "JOIN AppointmentProcedure ON [Procedure].id = AppointmentProcedure.procedure_id " +
                "JOIN Appointment ON AppointmentProcedure.appointment_id = appointment.id " +
                "WHERE Appointment.id = ? and [Procedure].name = ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, App.currentAppointment);
            statement.setString(2, procedure);
            result = statement.executeUpdate();
        }
        return result;
    }

    private static double getProcedurePrice(String procedure) {
        String sql = "SELECT [Procedure].[price] as price FROM [Procedure] " +
                "WHERE [Procedure].name = ?";
        double price = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, procedure);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("price");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return price;
    }

    public static ObservableList<Recommendations> getRecommendations() {
        String sql = "SELECT Appointment.[date] as [date], Appointment.recomedation as recommendation FROM  Appointment " +
                "JOIN Patient ON Patient.id = Appointment.patient_id " +
                "WHERE Patient.username = ?";
        List<Recommendations> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new Recommendations(
                            resultSet.getString("date").substring(0, 16),
                            resultSet.getString("recommendation")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return FXCollections.observableList(result);
    }
}
