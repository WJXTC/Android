package com.example.myapplication.WatchList;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchlistDAO {
    @Query("SELECT * FROM watchlist")
    LiveData<List<Watchlist>> getAll();

    @Query("SELECT * FROM watchlist WHERE wid = :wid LIMIT 1")
    Watchlist findByID(int wid);
    //return only logged in users' watchlist.
    @Query("SELECT * FROM watchlist WHERE uid = :uid")
    LiveData<List<Watchlist>> findByUid(String uid);
    //used to check duplication
    @Query("SELECT * FROM watchlist WHERE uid = :uid")
    List<Watchlist> ListFindByUid(String uid);

    @Query("DELETE FROM watchlist WHERE wid = :wid")
    void deleteByWid(int wid);

    //CRUD method for the queried result.
    @Insert
    void insertAll(Watchlist... watchlist);

    @Insert
    long insert(Watchlist watchlist);

    @Delete
    void delete(Watchlist watchlist);

    @Update(onConflict = REPLACE)
    void updateWatchlist(Watchlist... watchlist);

    @Query("DELETE FROM watchlist")
    void deleteAll();
}
