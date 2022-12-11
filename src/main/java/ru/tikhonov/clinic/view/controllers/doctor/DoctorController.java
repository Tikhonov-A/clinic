package ru.tikhonov.clinic.view.controllers.doctor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DoctorDao;

public class DoctorController {
    @FXML
    public Button makeAppointment;

    @FXML
    public Button checkAppointments;

    @FXML
    public Button statistic;

    @FXML
    public Button checkReviews;

    @FXML
    private Button editAppointment;

    @FXML
    private Button backButton;

    @FXML
    private Button bills;


    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Аутентификация", getClass().getClassLoader().getResource("auth.fxml"));
        });

        makeAppointment.setOnAction(actionEvent -> {
            makeAppointment.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorAppointment.fxml"));
        });

        editAppointment.setOnAction(actionEvent -> {
            makeAppointment.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorEdit1Appointment.fxml"));
        });

        checkAppointments.setOnAction(actionEvent -> {
            makeAppointment.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorFindAppointments.fxml"));
        });

        checkReviews.setOnAction(actionEvent -> {
            makeAppointment.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorCheckReviews.fxml"));
        });

        statistic.setOnAction(actionEvent -> {

            int genCount = DoctorDao.getCountAppointments();
            int countForMonth = DoctorDao.getCountAppointmentsForMonth();
            int moneyThisMonth = DoctorDao.Salary(countForMonth);
            App.setAlert("Статистика", Alert.AlertType.INFORMATION,
                    "Заказов всего: " + genCount + "\n" +
                            "Заказов за месяц: " + countForMonth + "\n"+
                            "Заработано в этом месяце: " + moneyThisMonth);
        });
    }
}
