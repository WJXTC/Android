package com.example.myapplication;

import java.util.Date;

public class Appuser {
    //private Integer uid;
    private String name;
    private String surname;
    private String gender;
    private Date dob;
    private String address;
    private String state;
    private String postcode;
    private Integer uid;
    //private Credentials email;
    public Appuser() {
    }
    public Appuser(String name, String surname, String gender,Date dob,String address,String state,String postcode ) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address =  address;
        this.state = state; 
        this.postcode = postcode;

        /*Credentials cred = new Credentials();
        cred.setUsername(email);
        this.email = cred;*/
    }
    /*public Appuser(Integer uid) {
        this.uid = uid;
    }*/
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    //public Credentials getEmail() { return email; }

    //public void setEmail(Credentials email) {this.email = email;}

}
