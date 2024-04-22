package org.example.virtualtapcash.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LoginCredentials {

    @Id
    private String UserId;

    @Column(name = "m_pin")
    private String mPin;


    public LoginCredentials() {
    }

    public LoginCredentials(String userId, String mPin) {
        UserId = userId;
        this.mPin = mPin;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getmPin() {
        return mPin;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
    }

    @Override
    public String toString() {
        return "LoginCredentials{" +
                "UserId='" + UserId + '\'' +
                ", mPin='" + mPin + '\'' +
                '}';
    }
}
