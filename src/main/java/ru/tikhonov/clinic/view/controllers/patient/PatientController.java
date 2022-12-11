package ru.tikhonov.clinic.view.controllers.patient;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.PatientDao;


public class PatientController {


    @FXML
    public Button checkRecommendations;

    @FXML
    private Button appointment;

    @FXML
    private Button backButton;

    @FXML
    private Button bills;

    @FXML
    private Button diseases;

    @FXML
    private Button medicine;

    @FXML
    private Button personalInfo;

    @FXML
    private Button procedure;

    @FXML
    private Button review;

    @FXML
    private Button topUp;

    @FXML
    void initialize() {

        backButton.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Аутентификация", getClass().getClassLoader().getResource("auth.fxml"));
        });


        personalInfo.setOnAction(actionEvent -> {
            try {
                String result = PatientDao.findByUsername(App.currentUser).toString();
                App.setAlert("Информация", Alert.AlertType.INFORMATION, result);
            } catch (DaoException e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR,"Нет такого пользователя");
            }
        });

        topUp.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("topUp.fxml"));
        });

        bills.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientOperations.fxml"));
        });

        diseases.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientDiseases.fxml"));
        });

        appointment.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientFindAppointments.fxml"));
        });

        review.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientMakeReview.fxml"));
        });

        medicine.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientBuyMedicine.fxml"));
        });

        procedure.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientBuyProcedure.fxml"));
        });

        checkRecommendations.setOnAction(actionEvent -> {
            topUp.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientCheckRecommendations.fxml"));
        });

    }
}