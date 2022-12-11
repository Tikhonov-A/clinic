package ru.tikhonov.clinic;

import ru.tikhonov.clinic.dao.AdminDao;
import ru.tikhonov.clinic.dao.DaoException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestMain {
    public static void main(String[] args) throws DaoException {
        AdminDao.insertPersonal(
                "Admin",
                "Adminov",
                "Adminovich",
                "admin",
                "admin",
                null,
                1, 21312312312L
        );
        //System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-01-MM")));
    }
}
