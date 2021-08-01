package com.example.myapplication.WatchList;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.WatchList.Watchlist;
import com.example.myapplication.WatchList.WatchlistDAO;
import com.example.myapplication.WatchList.WatchlistDB;
import com.example.myapplication.fragment.WatchListFragment;

import java.util.List;

public class WatchListRepository {
    private WatchlistDAO dao;
    private LiveData<List<Watchlist>> allwatchlist;
    private Watchlist watchlist;
    private List<Watchlist> wl;
    public WatchListRepository(Application application){
        WatchlistDB db = WatchlistDB.getInstance(application);
        dao=db.watchlistDao();
    }

    public LiveData<List<Watchlist>> getAllWatchlist() {
        allwatchlist=dao.getAll();
        return allwatchlist;
    }
    //get watch list under certain user account identified by uid.
    public LiveData<List<Watchlist>> getUserWatchlist(String uid) {
        allwatchlist=dao.findByUid(uid);//should return a live data.
        return allwatchlist;
    }
    public List<Watchlist> getListUserWatchlist(String uid) {
        wl = dao.ListFindByUid(uid);
        return wl;
    }

    public void insert(final Watchlist w){
        WatchlistDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() { dao.insert(w);
            }
        });
    }

    public void deleteAll(){ WatchlistDB.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() {dao.deleteAll(); }
    });
    }
    //delete certain watchlist object
    public void deleteByWid(final int wid){
        //return executor service...
        WatchlistDB.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() {
            dao.deleteByWid(wid); }
    });
    }

    public void insertAll(final Watchlist... w){
        WatchlistDB.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() { dao.insertAll(w); }
        });
    }


    public void updateWatchlist(final Watchlist... w){ WatchlistDB.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() { dao.updateWatchlist(w); }
    });
    }

    public Watchlist DeleteByWid(final int wid){
        WatchlistDB.databaseWriteExecutor.execute(new Runnable() { @Override
        public void run() {
            dao.deleteByWid(wid);
        } });
        return watchlist; }
    public void setWatchlist(Watchlist w){ this.watchlist=w;
    }
}