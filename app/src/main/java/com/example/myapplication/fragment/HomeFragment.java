package com.example.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Memoir;
import com.example.myapplication.R;
import com.example.myapplication.networkconnection.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HomeFragment extends Fragment {
    private SharedPreferences sp;
    private View home;
    private Gson gson;
    private TextView welcome;
    //declare hash map simple adapter and list view.
    List<HashMap<String, String>> mvListArray;
    SimpleAdapter myListAdapter;
    ListView mvList;
    //HashMap<String,String> map = new HashMap<String,String>();
    String[] colHEAD = new String[] {"movie","release date","rating"};
    //data cells id.
    int[] dataCell = new int[] {R.id.tv_name,R.id.tv_release,R.id.tv_score};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        home = inflater.inflate(R.layout.fragment_home, container, false);
        //used bind data list to the simple adapter
        mvList = home.findViewById(R.id.home_top5list);
        //http get top5
        //1. obtain uid
        sp=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String usr= sp.getString("uid",null);// defValue: the return return if this preference does not exist.
        Log.i("user",usr);
        //display welcome page.
        welcome = home.findViewById(R.id.tv_welcome);
        Date current = new Date();
        SimpleDateFormat df  = new SimpleDateFormat("dd/mm/yyyy");

        welcome.setText(df.format(current) + " WELCOME " + (String)sp.getString("firstname",null));
        Async async = new Async();//http to obtain top5 by entering uid.
        async.execute(usr);//jump to async task.
        //2. obtain memoir by TopFive return top5 list.
        //[{"movie":"Heidi","release_date":"2020-01-16","rating":98},{"movie":"Mulan","release_date":"2020-03-31","rating":73},{"movie":"Invisible Life","release_date":"2020-01-28","rating":66}]


        Async top5 = new Async();

        top5.execute(usr);//
        //binding data, layout....to the simple adapter
        return home;
    }
    private class Async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            mvListArray = new ArrayList<HashMap<String, String>>();
            Log.i("movieListuid",params[0]);
            //pass uid as parameters and return a list of top 5 movie.
            String top5 = RestClient.findByTopFive(params[0]);
            Log.i("top5",top5);
            //"MOVIE","Release Date","Score"
            //memo.movie, memo.release_date, memo.rating
            String top5v2 = top5.replaceAll("[\\[\\]\"]", "");
            String[] row = top5v2.split("\\},\\{");
            Log.i("row",row[0]);
            //int i;
            for(int i = 0;i < row.length;i++){
                HashMap<String,String> map = new HashMap<String,String>();
                row[i].replaceAll("[\\{\\}\"]", "");
                String[] memo = row[i].split(",");
                map.put("movie",memo[0].split(":")[1]);
                map.put("release date",memo[1].split(":")[1]);
                map.put("rating",memo[2].split(":")[1]);
                mvListArray.add(map);
            }
            mvListArray.size();
            return RestClient.findByTopFive(params[0]);
        }
        protected void onPostExecute(String textResult) {
            myListAdapter = new SimpleAdapter(getActivity(),mvListArray,R.layout.top5lv,colHEAD,dataCell);
            mvList.setAdapter(myListAdapter);
        }
    }

}
