package com.example.myapplication.WatchList;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class WatchlistViewModel extends ViewModel {
    //private final Object Watchlist;
    private WatchListRepository wRepository;
    private MutableLiveData<List<Watchlist>> allWatchlist;

    public WatchlistViewModel () { allWatchlist=new MutableLiveData<>();}

    public void setWatchlist( List<Watchlist> w) { allWatchlist.setValue(w); }
    public void initalizeVars(Application application){ wRepository = new WatchListRepository(application); }

    public LiveData<List<Watchlist>> getAllWatchlist() { return wRepository.getAllWatchlist(); }
    //get user watchlist
    public LiveData<List<Watchlist>> getUserWatchlist(String uid) { return wRepository.getUserWatchlist(uid); }//wRepository :null.
    public List<Watchlist> getListUserWatchlist(String uid) { return wRepository.getListUserWatchlist(uid); }

    public void insert(Watchlist w) { wRepository.insert(w); }//w ： get wrepository = null;
    public void insertAll(Watchlist... w) { wRepository.insertAll(w); }
    public void deleteAll() { wRepository.deleteAll(); }
    //delete certain object from watchlist
    //public void delete(Watchlist w) { wRepository.delete(w); }

    public void update(Watchlist... w) {
        wRepository.updateWatchlist(w); }
    public LiveData<List<Watchlist>> findByUid(String uid){ return wRepository.getUserWatchlist(uid); }
    public void deleteByWid(int wid){wRepository.deleteByWid(wid);}
}
