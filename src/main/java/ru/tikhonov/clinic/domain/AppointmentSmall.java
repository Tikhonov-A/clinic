package ru.tikhonov.clinic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentSmall {
    public String date;
    public String status;
}
