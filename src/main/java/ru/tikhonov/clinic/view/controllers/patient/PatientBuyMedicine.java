package ru.tikhonov.clinic.view.controllers.patient;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.tikhonov.clinic.App;
import ru.tikhonov.clinic.dao.DaoException;
import ru.tikhonov.clinic.dao.PatientDao;

public class PatientBuyMedicine {

    @FXML
    private Button backButton;

    @FXML
    private Button buyButton;

    @FXML
    private ComboBox<String> categoryField;

    @FXML
    private TextField countField;

    @FXML
    private Button findButton;

    @FXML
    private ComboBox<String> productField;

    @FXML
    void initialize() {

        backButton.setOnAction(actionEvent -> {
            backButton.getScene().getWindow().hide();
            App.setScene("Пациент", getClass().getClassLoader().getResource("patientGui.fxml"));
        });

        countField.setDisable(true);
        productField.setDisable(true);
        categoryField.setItems(PatientDao.getCategories());

        findButton.setOnAction(actionEvent -> {
            if (categoryField.getValue() == null) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Выберите категорию");
            } else  {
                countField.setDisable(false);
                productField.setDisable(false);
                productField.setItems(PatientDao.getMedicine(categoryField.getValue()));
            }

        });

        buyButton.setOnAction(actionEvent -> {
            if (productField.getValue() == null || countField.getText().equals("")) {
                App.setAlert("Ошибка", Alert.AlertType.ERROR, "Выберите товар или количество");
            } else {
                try {
                    int count = Integer.parseInt(countField.getText());
                    PatientDao.buyMedicine(productField.getValue(), count);
                    App.setAlert("Покупка", Alert.AlertType.CONFIRMATION, "Куплено успешно");
                } catch (NumberFormatException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, "Количество товара должно быть целым числом");
                } catch (DaoException e) {
                    App.setAlert("Ошибка", Alert.AlertType.ERROR, e.getMessage());
                }
            }
        });

    }
}