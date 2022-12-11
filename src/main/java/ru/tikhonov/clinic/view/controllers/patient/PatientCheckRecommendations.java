package ru.tikhonov.clinic.view.controllers.patient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.PatientDao;
import ru.tikhonov.clinic.domain.Recommendations;

public class PatientCheckRecommendations {

    @FXML
    private Button backButton;

    @FXML
    private Label balance;

    @FXML
    private TableColumn<Recommendations, String> date;

    @FXML
    private TableColumn<Recommendations, String> recommendation;

    @FXML
    private TableView<Recommendations> table;

    @FXML
    void initialize() {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        recommendation.setCellValueFactory(new PropertyValueFactory<>("recommendation"));

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        table.setItems(PatientDao.getRecommendations());
    }

}
