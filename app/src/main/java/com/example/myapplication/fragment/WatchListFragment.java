package com.example.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.WatchList.Watchlist;
import com.example.myapplication.WatchList.WatchlistDB;
import com.example.myapplication.WatchList.WatchlistViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WatchListFragment extends Fragment {
    private View v;
    private String mParam2;
    private TextView movie,release,dateAdd,selection;
    //private EditText et_movie,et_release,et_mid;
    private Button delete,viewbtn;
    private WatchlistDB watchlistDB;
    private SharedPreferences sp;
    private String uid,mid,movie_value,release_date,current;
    private int wid;
    public WatchlistViewModel watchlistViewModel;
    //private String selected;
    //private WatchlistViewModel watchListvm;
    //list view
    ArrayList<HashMap<String, Object>> mvListArray;
    SimpleAdapter mvListAdapter;
    ListView mvList;

    String[] colHEAD = new String[] {"movie name","release date","date Add"};
    //defined in the listview layout!!!not in the fragment layout
    int[] dataCell = new int[] {R.id.wl_tv_moviename,R.id.wl_tv_release,R.id.wl_tv_date_add};
    public static WatchListFragment newInstance() { return new WatchListFragment();
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init ViewModel
        watchlistViewModel = ViewModelProviders.of(requireActivity()).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getActivity().getApplication());

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_watch_list, container, false);

        watchlistDB = WatchlistDB.getInstance(getContext());
        // define views.
        movie = v.findViewById(R.id.wl_tv_moviename);
        release = v.findViewById(R.id.wl_tv_release);
        dateAdd = v.findViewById(R.id.wl_tv_date_add);
        mvList = v.findViewById(R.id.wl_watchlv);
        viewbtn = v.findViewById(R.id.wl_btn_read);
        selection = v.findViewById(R.id.wl_tv_selection);
        delete = v.findViewById(R.id.wl_btn_delete);
        //watchlist view model
        //WatchlistViewModel watchListvm= ViewModelProviders.of(getActivity()).get(WatchlistViewModel.class);
        //watchListvm.initalizeVars(getActivity().getApplication());
        //get uid
        sp=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        uid= sp.getString("uid",null);
        /*//get WatchList object values
        movie_value = et_movie.getText().toString();
        release_date = et_release.getText().toString();
        //get mid
        mid = et_mid.getText().toString();*/
        //get current time.
        Calendar calendar = Calendar.getInstance(); // Returns instance with current date and time set
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        current = formatter.format(calendar.getTime());
        //read ALL watchlist of user.


        watchlistViewModel.getUserWatchlist(uid).observe(getViewLifecycleOwner(), new Observer<List<Watchlist>>() {
            @Override
            public void onChanged(@Nullable final List<Watchlist> w){
                //String allWatchlist = "";
                mvListArray = new ArrayList<HashMap<String, Object>>();
                for (Watchlist temp : w) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("movie name",temp.getMovie_name());
                    map.put("release date",temp.getRelease_date());
                    map.put("date Add",temp.getDate_time_add());
                    map.put("id",temp.getMid());
                    map.put("wid",temp.getId());
                    mvListArray.add(map);
                    //String wstr = (temp.getId() + " " + temp.getMovie_name() + " " + temp.getMid() + " " + temp.getUid()+ " " + temp.getRelease_date()+ " " + temp.getDate_time_add());
                    //allWatchlist = allWatchlist + System.getProperty("line.separator") + wstr;//line separator: \n
                }
                mvListAdapter=new SimpleAdapter(getContext(),mvListArray,R.layout.watchlistlv,colHEAD,dataCell);
                mvList.setAdapter(mvListAdapter);
            }
        });
        mvList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                Log.i("movie list position", String.valueOf(position));
                //get movie mid.
                int posititonNum = position;
                HashMap<String, Object> map = (HashMap<String, Object>) mvList.getItemAtPosition(position);//get item by its position.
                String mvid = map.get("id").toString();//get item info.
                String released = map.get("release date").toString();
                String namem = map.get("movie name").toString();
                String date = map.get("date Add").toString();
                selection.setText(namem + " "+ released+ " addded at "+date + " is selected");
                mid = mvid;
                movie_value = namem;
                release_date = date;
                wid = (Integer) map.get("wid");
            }
        });

        //register btn.
        viewbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(mid != null){
                    //replaceFragment(new MovieViewFragment());
                    Fragment f = new MovieViewFragment();
                    Bundle b = new Bundle();
                    b.putString("mid",mid);
                    b.putString("movie",movie_value);
                    b.putString("release date", release_date);
                    b.putBoolean("watchlist return", true);
                    f.setArguments(b);
                    replaceFragment(f);
                }
                Log.i("selection","nothing selected");
                Toast.makeText(getContext(),"please select an item from list",Toast.LENGTH_LONG);//not use
            }

        });
        delete.setOnClickListener(new View.OnClickListener() { //including onClick() method
            public void onClick(View v) {
                //get wid.

                //delete w.
                watchlistViewModel.deleteByWid(wid);
            }

        });

        return v;
    }
    //implement in movie view ....
    public class InsertDatabase extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            watchlistDB.watchlistDao().insert((Watchlist) objects[0]);
            return null;
        }

        @Override

        protected void onPostExecute(String details) {


        }
    }
    /*private class DeleteAllDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            watchlistDB.watchlistDao().deleteAll();
            return null;
        }
        @Override

        protected void onPostExecute(String details) {
            Toast.makeText(getContext(),"empty database",Toast.LENGTH_LONG);
        }
    }
    private class DeleteDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            watchlistDB.watchlistDao().delete(watchlist);
            return null;
        }
        @Override

        protected void onPostExecute(String details) {


        }
    }*/

   /* private class DeleteDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            watchlistDB.watchlistDao().delete();
            return null;
        }
        @Override

        protected void onPostExecute(String details) {


        }
    }*/

    /*private class UpdateDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            watchlistDB.watchlistDao().updateWatchlist(watchlist);
            return null;
        }
        @Override

        protected void onPostExecute(String details) {


        }
    }*/

    private class ReadDatabase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            LiveData<List<Watchlist>> users = watchlistDB.watchlistDao().findByUid(params[0]);
            return null;
        }
        @Override

        protected void onPostExecute(String details) {


        }
    }
    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

}




