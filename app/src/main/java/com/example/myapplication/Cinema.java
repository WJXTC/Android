package com.example.myapplication;

import java.util.Collection;

public class Cinema {
    private Integer cid;

    private String cinemaname;

    private String location;

    private Collection<Memoir> memoirCollection;

    public Cinema() {
    }
    public Cinema(String c, String l) {
        this.cinemaname = c;
        this.location = l;
    }

    public Cinema(Integer cid) {
        this.cid = cid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

