package ru.tikhonov.clinic.view.controllers.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.tikhonov.clinic.App;


public class AdminController {

    @FXML
    public Button addMedicine;

    @FXML
    public Button addDiagnosis;

    @FXML
    public Button addSpeciality;

    @FXML
    public Button addMedicineCategory;

    @FXML
    public Button addBonus;

    @FXML
    public Button addProcedure;

    @FXML
    public Button fillMedicine;

    @FXML
    private Button backButton;

    @FXML
    private Button addPersonal;

    @FXML
    void initialize() {
        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Аутентификация", getClass().getClassLoader().getResource("auth.fxml"));
        });

        addPersonal.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminAddPersonal.fxml"));
        });

        addMedicine.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminAddMedicine.fxml"));
        });

        addMedicineCategory.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminAddMedicineCategory.fxml"));
        });

        addDiagnosis.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminAddDiagnosis.fxml"));
        });

        addSpeciality.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminAddSpeciality.fxml"));
        });

        addBonus.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminAddBonus.fxml"));
        });

        addProcedure.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminAddProcedure.fxml"));
        });

        fillMedicine.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Админ", getClass().getClassLoader().getResource("adminFillMedicine.fxml"));
        });

    }
}
