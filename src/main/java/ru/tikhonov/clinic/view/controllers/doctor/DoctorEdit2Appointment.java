package ru.tikhonov.clinic.view.controllers.doctor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.DoctorDao;

import java.util.ArrayList;
import java.util.List;

public class DoctorEdit2Appointment {

    @FXML
    private RadioButton rb1;

    @FXML
    private RadioButton rb2;

    @FXML
    private RadioButton rb3;

    @FXML
    private RadioButton rb4;
    @FXML
    private Button backButton;

    @FXML
    private ListView<Item> diagnosPicker = new ListView<>();

    @FXML
    private Button editAppointment;

    @FXML
    private ListView<Item> procedurePicker = new ListView<>();

    @FXML
    private TextArea recommendationField;

    @FXML
    void initialize() {

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Доктор", getClass().getClassLoader().getResource("doctorEdit1Appointment.fxml"));
        });

        ObservableList<String> diagnosis;
        try {
            diagnosis = DoctorDao.findDiagnosis();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        List<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);
        radioButtons.add(rb4);

        String status = DoctorDao.getCurrentStatus();

        for (RadioButton rb : radioButtons) {
            rb.setSelected(false);
        }

        for (RadioButton rb : radioButtons) {
            if (rb.getText().equals(status)) {
                rb.setSelected(true);
            }
        }

        ObservableList<String> diagnosisPicked;
        try {
            diagnosisPicked = DoctorDao.findDiagnosisForCurrentAppointment();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        boolean flag = false;
        for (String s : diagnosis) {
            for (String sPicked : diagnosisPicked) {
                if (sPicked.equals(s)) {
                    diagnosPicker.getItems().add(new Item(s, true));
                    flag = true;
                }
            }
            if (!flag) {
                diagnosPicker.getItems().add(new Item(s, false));
            }
            flag = false;
        }


        diagnosPicker.setCellFactory(CheckBoxListCell.forListView(new Callback<Item, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Item item) {
                return item.onProperty();
            }
        }));

        ObservableList<String> procedures;
        try {
            procedures = DoctorDao.findProcedures();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> proceduresPicked;
        try {
            proceduresPicked = DoctorDao.findProceduresForCurrentAppointment();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        flag = false;
        for (String s : procedures) {
            for (String sPicked : proceduresPicked) {
                if (sPicked.equals(s)) {
                    procedurePicker.getItems().add(new Item(s, true));
                    flag = true;
                }
            }
            if (!flag) {
                procedurePicker.getItems().add(new Item(s, false));
            }
            flag = false;
        }

        recommendationField.setText(DoctorDao.findRecommendation());


        procedurePicker.setCellFactory(CheckBoxListCell.forListView(new Callback<Item, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Item item) {
                return item.onProperty();
            }
        }));



        editAppointment.setOnAction(actionEvent -> {
            DoctorDao.deleteProceduresForCurrentUser();
            DoctorDao.deleteDiagnosisForCurrentUser();
            for (Item item : diagnosPicker.getItems()) {
                if(item.isOn()) {
                    try {
                        DoctorDao.insertDiagnosis(item.name.get());
                    } catch (DaoException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }

            for (Item item : procedurePicker.getItems()) {
                if(item.isOn()) {
                    try {
                        DoctorDao.insertProcedure(item.name.get());
                    } catch (DaoException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }

            for (RadioButton rb : radioButtons) {
                if (rb.isSelected()) {
                    DoctorDao.updateStatus(rb.getText());
                }
            }

            DoctorDao.updateRecommendation(recommendationField.getText());
            App.setAlert("Редактирование према", Alert.AlertType.CONFIRMATION, "Редактирование према прошло успешно");
        });
    }

    public static class Item {
        private final StringProperty name = new SimpleStringProperty();
        private final BooleanProperty on = new SimpleBooleanProperty();

        public Item(String name, boolean on) {
            setName(name);
            setOn(on);
        }

        public final StringProperty nameProperty() {
            return this.name;
        }

        public final String getName() {
            return this.nameProperty().get();
        }

        public final void setName(final String name) {
            this.nameProperty().set(name);
        }

        public final BooleanProperty onProperty() {
            return this.on;
        }

        public final boolean isOn() {
            return this.onProperty().get();
        }

        public final void setOn(final boolean on) {
            this.onProperty().set(on);
        }

        @Override
        public String toString() {
            return getName();
        }

    }

}
