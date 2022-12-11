package ru.tikhonov.clinic.domain;

import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@AllArgsConstructor
@Getter
@Setter
public class Patient {
    String name;
    String surname;
    String patronymic;
    Date birthday;
    long polis_number;
    double cash;


    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Surname: " + surname + "\n" +
                "Patronymic: " + patronymic +  "\n" +
                "Birthday: " + birthday + "\n" +
                "Polis number: " + polis_number + "\n";

    }
}
