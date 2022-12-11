package ru.tikhonov.clinic.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Appointment {

    int id;
    String date;
    String recommendation;
    String review;
    String diagnose;
}
