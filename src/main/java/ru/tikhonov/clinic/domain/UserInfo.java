package ru.tikhonov.clinic.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public
class UserInfo {
    public String username;
    public String hashed_password;
    public String salt;
    public String role;
}
