package ru.tikhonov.clinic.view.controllers.patient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.PatientDao;
import ru.tikhonov.clinic.domain.AppointmentSmall;
import ru.tikhonov.clinic.domain.Diseases;

public class PatientDiseases {

    @FXML
    private TableColumn<Diseases, String> date;

    @FXML
    private TableColumn<Diseases, String> diagnosis;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Diseases> table;

    @FXML
    void initialize() {

        date.setCellValueFactory(new PropertyValueFactory<Diseases,String>("date"));
        diagnosis.setCellValueFactory(new PropertyValueFactory<Diseases,String>("diagnosis"));

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        table.setItems(PatientDao.getDiseases());

    }
}
