package ru.tikhonov.clinic.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.domain.AppointmentSmall;
import ru.tikhonov.clinic.domain.Operations;
import ru.tikhonov.clinic.domain.Recommendations;
import ru.tikhonov.clinic.domain.Reviews;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {
    

    public static void insertAppointment(String username, String date, String time) throws DaoException {
        String sql = "INSERT INTO Appointment(personal_id, date, status_id) " +
                "VALUES((SELECT Personal.id FROM Personal WHERE Personal.username = ?), ?," +
                "(SELECT Appointment_Status.id FROM Appointment_Status WHERE Appointment_status.status = ?))";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            System.out.println(date + " " + time);
            statement.setString(1, username);
            statement.setString(2, date + " " + time);
            System.out.println(date + " " + time);
            statement.setString(3, "Свободен");
            try {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("На данное время есть приемы");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static ObservableList<String> findAppointments(String date) throws DaoException {
        String sql = "SELECT Appointment.date FROM Personal " +
                "JOIN Appointment on Personal.id = Appointment.personal_id " +
                "WHERE username = ? and CONVERT(date, Appointment.[date]) LIKE ?";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            System.out.println(date);
            statement.setString(2, date);
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

    public static ObservableList<String> findDiagnosis() throws DaoException {
        String sql = "SELECT Diagnosis.name as name FROM Personal " +
                "JOIN Speciality ON Speciality.id = Personal.speciality_id " +
                "JOIN Diagnosis ON Diagnosis.speciality_id = Speciality.id " +
                "WHERE Personal.username = ?";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
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

    public static ObservableList<String> findDiagnosisForCurrentAppointment() throws DaoException {
        String sql = "SELECT [Diagnosis].name as name FROM Personal" +
                " JOIN Appointment on Personal.id = Appointment.personal_id" +
                " JOIN AppointmentDiagnosis ON Appointment.id = AppointmentDiagnosis.appointment_id" +
                " JOIN [Diagnosis] ON AppointmentDiagnosis.diagnosis_id = [Diagnosis].id " +
                " WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
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

    public static ObservableList<String> findProcedures() throws  DaoException {
        String sql = "SELECT [Procedure].name as name FROM Personal " +
                "JOIN Speciality ON Speciality.id = Personal.speciality_id " +
                "JOIN [Procedure] ON [Procedure].speciality_id = Speciality.id " +
                "WHERE Personal.username = ?";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
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

    public static ObservableList<String> findProceduresForCurrentAppointment() throws  DaoException {
        String sql = "SELECT [Procedure].name as name FROM Personal " +
                "JOIN Appointment on Personal.id = Appointment.personal_id " +
                "JOIN AppointmentProcedure ON Appointment.id = AppointmentProcedure.appointment_id " +
                "JOIN [Procedure] ON AppointmentProcedure.procedure_id = [Procedure].id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?";
        List<String> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
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

    public static String findRecommendation() {
        String sql = "SELECT Appointment.recomedation as recomendation FROM Personal " +
                "JOIN Appointment on Personal.id = Appointment.personal_id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?";
        String result = "";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result = resultSet.getString("recomendation");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return result;
    }

    public static int updateRecommendation(String recommendation) {

        String sql = "UPDATE Appointment SET recomedation = ? FROM Personal " +
                "JOIN Appointment on Personal.id = Appointment.personal_id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, recommendation);
            statement.setString(2, App.currentUser);
            statement.setString(3, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(4, (App.currentAppointmentDate + "%"));
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement or result set");
        }
        return result;
    }

    public static int deleteProceduresForCurrentUser() {
        String sql = "DELETE FROM AppointmentProcedure " +
                "WHERE AppointmentProcedure.id in( " +
                "SELECT AppointmentProcedure.id FROM AppointmentProcedure " +
                "JOIN Appointment on AppointmentProcedure.appointment_id = Appointment.id " +
                "JOIN Personal ON Appointment.personal_id = Personal.id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?)";
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

    public static int deleteDiagnosisForCurrentUser() {
        String sql = "DELETE FROM AppointmentDiagnosis " +
                "WHERE AppointmentDiagnosis.id in( " +
                "SELECT AppointmentDiagnosis.id FROM AppointmentDiagnosis " +
                "JOIN Appointment on AppointmentDiagnosis.appointment_id = Appointment.id " +
                "JOIN Personal ON Appointment.personal_id = Personal.id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?)";
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

    public static void insertDiagnosis(String diagnosis) throws DaoException {
        String sql = "INSERT INTO AppointmentDiagnosis(appointment_id, diagnosis_id) " +
                "VALUES( " +
                "(SELECT Appointment.id FROM Appointment " +
                "JOIN Personal ON Appointment.personal_id = Personal.id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?), " +
                "(SELECT Diagnosis.id FROM Diagnosis WHERE Diagnosis.name = ?))";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
            statement.setString(4, diagnosis);
            try {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("Ошибка");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static void insertProcedure(String procedure) throws  DaoException {
        String sql = "INSERT INTO AppointmentProcedure(appointment_id, procedure_id, status_id) " +
                "VALUES( " +
                "(SELECT Appointment.id FROM Appointment " +
                "JOIN Personal ON Appointment.personal_id = Personal.id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?), " +
                "(SELECT [Procedure].id FROM [Procedure] WHERE [Procedure].name = ?), ?)";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
            statement.setString(4, procedure);
            statement.setInt(5, 1);
            try {
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("Ошибка");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close statement or connection");
        }
    }

    public static String getCurrentStatus() {
        String sql = "SELECT Appointment_status.status as recomendation FROM Appointment_status " +
                "JOIN Appointment on Appointment_status.id = Appointment.status_id " +
                "JOIN Personal ON Appointment.personal_id = Personal.id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?";
        String result = "";
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(3, (App.currentAppointmentDate + "%"));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString("recomendation");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return result;

    }

    public static int updateStatus(String status)  {
        String sql = "UPDATE Appointment SET status_id = (SELECT Appointment_status.id FROM Appointment_status " +
                "WHERE Appointment_status.status = ?) FROM Appointment_status " +
                "JOIN Appointment on Appointment_status.id = Appointment.status_id " +
                "JOIN Personal ON Appointment.personal_id = Personal.id " +
                "WHERE username = ? and CONVERT(time, Appointment.[date]) LIKE ? and CONVERT(date, Appointment.[date]) LIKE ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, status);
            statement.setString(2, App.currentUser);
            statement.setString(3, ("%" + App.currentAppointmentTime + "%"));
            statement.setString(4, (App.currentAppointmentDate + "%"));
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement or result set");
        }
        return result;
    }

    public static ObservableList<AppointmentSmall> getAppointments() throws  DaoException {
        String sql = "SELECT Appointment.date, Appointment_status.status FROM Appointment_status " +
                "JOIN Appointment on Appointment_status.id = Appointment.status_id " +
                "JOIN Personal on Appointment.personal_id = Personal.id " +
                "WHERE Personal.username = ? and CONVERT(date, Appointment.[date]) LIKE ?";

        List<AppointmentSmall> appointments = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, App.currentAppointmentDate);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(new AppointmentSmall(
                            resultSet.getString("date").substring(0, 16),
                            resultSet.getString("status")
                    ));
                }
            } catch (SQLException e) {
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            System.err.println("Cannot close connection or statement");
        }
        return FXCollections.observableList(appointments);
    }

    public static int getCountAppointments() {
        String sql = "SELECT COUNT(*) as countRes FROM Personal " +
                "JOIN Appointment ON Personal.id = Appointment.personal_id " +
                "JOIN Appointment_status ON Appointment.status_id = Appointment_status.id " +
                "WHERE Personal.username = ? and Appointment_status.status = ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, "Закончен");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt("countRes");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return result;
    }

    public static int getCountAppointmentsForMonth() {
        String sql = "SELECT COUNT(*) as countRes FROM Personal " +
                "JOIN Appointment ON Personal.id = Appointment.personal_id " +
                "JOIN Appointment_status ON Appointment.status_id = Appointment_status.id " +
                "WHERE Personal.username = ? and CONVERT(date, Appointment.[date]) > ? and Appointment_status.status = ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            statement.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-01-MM")));
            statement.setString(3, "Закончен");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt("countRes");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return result;
    }

    public static int Salary(int countForMonth) {
        String sql = "SELECT Speciality.appointment_income as income FROM Speciality " +
                "JOIN Personal ON Personal.speciality_id = Speciality.id " +
                "WHERE Personal.username = ?";
        int result = 0;
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, App.currentUser);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt("income") * countForMonth;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Cannot close result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot close sql connection or statement");
        }
        return result;
    }

    public static ObservableList<Reviews> getReviews() {
        String sql = "SELECT Appointment.[date] as [date], Appointment.review as review FROM  Appointment " +
                "JOIN Personal ON Personal.id = Appointment.personal_id " +
                "WHERE Personal.username = ?";
        List<Reviews> result = new ArrayList<>();
        try (Connection connection = DaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, App.currentUser);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new Reviews(
                            resultSet.getString("date").substring(0, 16),
                            resultSet.getString("review")
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
