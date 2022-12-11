package ru.tikhonov.clinic.view.controllers.doctor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.DoctorDao;
import ru.tikhonov.clinic.dao.PatientDao;
import ru.tikhonov.clinic.domain.Appointment;
import ru.tikhonov.clinic.domain.AppointmentSmall;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DoctorFindAppointment {

    @FXML
    public TableColumn<AppointmentSmall,String> date;

    @FXML
    public TableColumn<AppointmentSmall,String> status;

    @FXML
    private Button backButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private Button findButton;

    @FXML
    private TableView<AppointmentSmall> table;

    /*ObservableList<AppointmentSmall> list = FXCollections.observableArrayList(
            new AppointmentSmall("sdfsdfds", "dsfsdfdsfs"),
            new AppointmentSmall("sdsdf", "gfs")
    );*/

    @FXML
    void initialize() throws DaoException {

        date.setCellValueFactory(new PropertyValueFactory<AppointmentSmall,String>("date"));
        status.setCellValueFactory(new PropertyValueFactory<AppointmentSmall,String>("status"));
        //table.setItems(list);

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorGui.fxml"));
        });

        findButton.setOnAction(actionEvent -> {
            try {
                LocalDate.parse(dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                App.currentAppointmentDate = dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println(App.currentAppointmentDate);
                try {
                    ObservableList<AppointmentSmall> list = DoctorDao.getAppointments();
                    table.setItems(list);
                } catch (DaoException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
                }
            } catch (Exception e) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Нет такой даты");
            }
        });

    }

}
