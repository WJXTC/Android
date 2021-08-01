package com.example.myapplication;

import java.util.Date;

public class Memoir {

    private static final long serialVersionUID = 1L;

    private Integer mid;

    private String moviename;

    private Date releasedate;

    private Date watchtime;

    private String comment;

    private Integer rating;

    private Appuser uid;

    private Cinema cid;

    public Memoir() {
    }
    public Memoir(String m,Date rd,Date wd,String c, Integer rate,int u) {
        //this.mid = mid;
        this.moviename =m;
        this.releasedate = rd;
        this.watchtime = wd;
        this.comment =c;
        this.rating = rate;

        Appuser a = new Appuser();
        a.setUid(u);
        this.uid = a;

    }

    public Memoir(Integer mid) {
        this.mid = mid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public Date getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(Date watchtime) {
        this.watchtime = watchtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Appuser getUid() {
        return uid;
    }

    public void setUid(Appuser uid) {
        this.uid = uid;
    }

    public Cinema getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        //this.cid = cid;
        Cinema cin = new Cinema();
        cin.setCid(cid);
        this.cid = cin;
    }
}
