package com.xiaoxintech.xiaoxinplayer.Data;

public class UserInfo {
    private String email, password, description;
    public UserInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
