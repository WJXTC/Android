package com.example.myapplication.WatchList;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Watchlist {
    @PrimaryKey(autoGenerate = true) public int wid;//watchlistid
    @ColumnInfo(name = "mid") public String mid;
    @ColumnInfo(name = "uid") public String uid;
    @ColumnInfo(name = "movie_name") public String movie_name;
    @ColumnInfo(name = "release_date") public String release_date;
    @ColumnInfo(name = "date_time_add") public String date_time_add;
    public Watchlist(String mid,String uid,String movie_name,String release_date,String date_time_add) {
        this.uid = uid;
        this.mid = mid;
        this.movie_name = movie_name;
        this.release_date = release_date;
        this.date_time_add = date_time_add;
    }
    public int getId() { return wid;
    }
    public String getMovie_name() { return movie_name;
    }
    public void setMovie_name(String m) {
        this.movie_name =m;
    }

    public String getMid() { return mid;
    }
    public void setMid(String mid) { this.mid = mid;
    }

    public String getUid() { return uid;
    }
    public void setUid(String u) { this.uid = u;
    }

    public String getRelease_date() { return release_date;
    }
    public void setRelease_date(String date) { this.release_date = date;
    }


    public String getDate_time_add() { return date_time_add;
    }
    public void setDate_time_add(String dt) { date_time_add = dt;
    }


}