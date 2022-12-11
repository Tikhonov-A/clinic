package ru.tikhonov.clinic.view.controllers.patient;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.PatientDao;
import ru.tikhonov.clinic.domain.AppointmentSmall;
import ru.tikhonov.clinic.domain.Operations;

public class PatientOperationsController {

    @FXML
    public TableColumn<Operations,String> date;

    @FXML
    public TableColumn<Operations,String> product;

    @FXML
    public TableColumn<Operations,Integer> price;

    @FXML
    private Button backButton;

    @FXML
    private Label balance;

    @FXML
    private TableView<Operations> table;


    @FXML
    void initialize() {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        balance.setText("Баланс: " + PatientDao.getCash() + "рублей");
        table.setItems(PatientDao.getOperations());
    }

}
