package com.example.myapplication;

import java.util.Date;

public class Credentials {
    private String username;
    private String password;
    private Date datesignup;
    private Appuser uid;
    //private String uid;

    public Credentials() {
    }
    public Credentials(String user,String psw,Date date,Integer uid) {
        this.username = user;
        this.password = psw;
        this.datesignup = date;

    }
    public Credentials(String user,String psw,Date date) {
        this.username = user;
        this.password = psw;
        this.datesignup = date;
    }
    public Appuser getUid() {
        return uid;
    }

    public void setUid(Appuser u) {
        this.uid = u;
    }

    public Credentials(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDatesignup() {
        return datesignup;
    }

    public void setDatesignup(Date datesignup) {
        this.datesignup = datesignup;
    }

    /*public String getUid() { return uid; }

    public void setUid(String uid) {
        this.uid = uid;
    }*/
}
