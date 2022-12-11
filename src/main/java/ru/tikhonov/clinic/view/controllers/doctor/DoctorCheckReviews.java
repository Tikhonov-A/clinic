package ru.tikhonov.clinic.view.controllers.doctor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DoctorDao;
import ru.tikhonov.clinic.dao.PatientDao;
import ru.tikhonov.clinic.domain.Reviews;

public class DoctorCheckReviews {

    @FXML
    private Button backButton;

    @FXML
    private Label balance;

    @FXML
    private TableColumn<Reviews, String> date;

    @FXML
    private TableColumn<Reviews, String> review;

    @FXML
    private TableView<Reviews> table;

    @FXML
    void initialize() {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        review.setCellValueFactory(new PropertyValueFactory<>("review"));

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("doctorGui.fxml"));
        });

        table.setItems(DoctorDao.getReviews());
    }

}
