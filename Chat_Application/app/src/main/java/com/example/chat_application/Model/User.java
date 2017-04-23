package com.example.chat_application.Model;

import java.io.Serializable;

/**
 * Created by BANDINI on 22-04-2017.
 */

public class User implements Serializable {
    //private  String UserName;
    private int ID;
    private String FirstName;
    private String LastName;
    private  String Country;
    private String PhoneNumber;
    private String EmailID;
    private String Password;
    private String VerificationCode;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    /*
        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }
        */
    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }
}
