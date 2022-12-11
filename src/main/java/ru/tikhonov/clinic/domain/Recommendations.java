package ru.tikhonov.clinic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Recommendations {
    String date;
    String recommendation;
}
