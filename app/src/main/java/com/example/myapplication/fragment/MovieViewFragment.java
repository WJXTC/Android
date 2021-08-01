package com.example.myapplication.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.networkconnection.API;
import com.example.myapplication.R;
import com.example.myapplication.Snippet;
import com.example.myapplication.WatchList.Watchlist;
import com.example.myapplication.WatchList.WatchlistViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MovieViewFragment extends Fragment {
    private View v;
    private TextView movie,cast,release_date,country,directors,genre,summary;
    private ImageView img;
    private RatingBar score;
    private Button addMemoir,addWatchlist;
    private SharedPreferences sp;
    WatchlistViewModel watchlistViewModel;
    private String urlImage,value_overview,value_score;
    public MovieViewFragment() {
        // Required empty public constructor
    }
    /**
     * Create a new instance of this fragment
     * @return A new instance of fragment FirstFragment.
     */
    public static MovieViewFragment newInstance() {
        return new MovieViewFragment();
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init ViewModel
        watchlistViewModel = ViewModelProviders.of(requireActivity()).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getActivity().getApplication());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_movieview, container, false);
        //the genre, the cast, the release date, the country, the name of the director(s),
        // a synopsis/plot summary/storyline (from a movie API).
        //declare views.
        //String mid;
        addMemoir = v.findViewById(R.id.mv_addMemoir);
        //if watchlist fragment passed here, then no button for add to watchlist.
        //if(getArguments().getString("watchlist") != null){
        addWatchlist = v.findViewById(R.id.mv_addWatchList);
        //}
        //1. receive bundles
        movie = v.findViewById(R.id.mv_title);
        cast = v.findViewById(R.id.mv_cast);//get credits
        release_date = v.findViewById(R.id.mv_release);
        country = v.findViewById(R.id.mv_country);//get details
        directors = v.findViewById(R.id.mv_directors);//get credits.
        genre = v.findViewById(R.id.mv_genre);//get details
        summary = v.findViewById(R.id.mv_summary);
        img = v.findViewById(R.id.mv_poster);
        score = v.findViewById(R.id.mv_score);
        //Bundle
        final String value_mid = getArguments().getString("mid");
        final String value_release = getArguments().getString("release date");
        final String value_movie = getArguments().getString("movie");
        final Boolean flag = getArguments().getBoolean("watchlist return");

        //directly source from tmvdb.

        /*final String value_score = getArguments().getString("score");
        Float value_scoreb = Float.parseFloat(value_score)/2;
        String value_overview = getArguments().getString("overview");*/

        //final String value_img = getArguments().getString("img");
        //user infor.
        sp=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String uid = sp.getString("uid",null);
        //set text to view.
        movie.setText(value_movie);
        release_date.setText(value_release);
        /*summary.setText(value_overview);
        score.setRating(value_scoreb);*/
        //display poster.
        //Async Task to call API to get details
        DetailAsync d = new DetailAsync();
        d.execute(value_mid);


        addMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check duplication
                //add information to the bundle
                //replace fragment to memoir
                Bundle b = new Bundle();
                b.putString("movie",value_movie);
                b.putFloat("rate",Float.parseFloat(value_score)/2);
                b.putString("release",value_release);
                b.putString("poster",urlImage);
                Fragment f = new AddMemoirFragment();
                f.setArguments(b);
                replaceFragment(f);
            }
        });
        /*if(flag == true){
            addWatchlist.setVisibility(View.INVISIBLE);
        }*/
        addWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAsync i = new insertAsync();
                //replace fragment to watchlist
                Calendar c = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String current = formatter.format(c.getTime());

                Watchlist watch = new Watchlist(value_mid,uid,value_movie,value_release,current);//correct.
                i.execute(watch);//get resources.
                //watchlistViewModel = new ViewModelProviders.of(getActivity()).get(WatchlistViewModel.class);
                //watchlistViewModel = new ViewModelProvider(getActivity()).get(WatchlistViewModel.class);
                //watchlistViewModel.initalizeVars(getActivity().getApplication());
                replaceFragment(new WatchListFragment());
            }
        });

        return v;
    }

    private class DetailAsync extends AsyncTask<String, Void, String> {

        @SuppressLint("WrongThread")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            String textResult = API.getDetail(strings[0]);//return a json
            HashMap<String, List<String>> sp = Snippet.getDetailSnippet(textResult);
            List<String> value_country_array = sp.get("country");
            List<String> value_genres_array = sp.get("genres");
            if(sp.get("overview").size() < 0 ){
                value_overview = "";
            }
            else{
                value_overview = sp.get("overview").get(0);//get
            }
            if(sp.get("score").size() < 0 ){
                value_score = "0";
            }
            else{value_score = sp.get("score").get(0);}
            if(sp.get("poster").size()<0){
                urlImage ="https://via.placeholder.com/150?text=No+Image+Available\n";//set default poster.
            }else{
            urlImage = "http://image.tmdb.org/t/p/w500/" + sp.get("poster").get(0);}
            //map.put("poster",urlImage);
            //ArrayList<String> value_score_array = sp.get("vote_average");
            //ArrayList<String> value_img_array = sp.get("poster_path");
            //convert array list to String
            String value_country = String.join(", ", value_country_array);
            String value_genres = String.join(", ", value_genres_array);
            //set text
            country.setText("country :" + value_country);
            genre.setText("genres: " + value_genres);
            //API call to get credits.
            String textResult2 = API.getCredit(strings[0]);
            HashMap<String, ArrayList<String>> sp2 = Snippet.getCreditSnippet(textResult2);//get~
            ArrayList<String> value_cast_array = sp2.get("cast");
            ArrayList<String> value_crew_array = sp2.get("crew");
            String value_cast = "";
            String value_crew = "";
            //convert array list to String
            if (value_cast_array.size() >= 3) {
                List l = value_cast_array.subList(0, 2);

                value_cast = String.join(", ", l);
            } else {
                value_cast = String.join(", ", value_cast_array);
            }
            if (value_crew_array.size() >= 3) {
                List l = value_crew_array.subList(0, 2);

                value_crew = String.join(", ", l);
            } else {
                value_crew = String.join(", ", value_crew_array);
            }
            //set text
            cast.setText("cast: " + value_cast);
            directors.setText("directors: " + value_crew);


            //API call to get img, score, overview.
            return null;
        }
        protected void onPostExecute(String textResult){
            Picasso.get().load(urlImage)
                    .placeholder(R.mipmap.ic_launcher) .resize(800, 1000)
                    .centerInside()
                    .into(img);
            score.setRating(Float.parseFloat(value_score)/2);
            summary.setText(value_overview);

        }
    }

    private class insertAsync extends AsyncTask<Watchlist, Void, String>{

        @Override
        protected String doInBackground(Watchlist... w) {
            String user  = w[0].getUid();
            Log.i("user",user);
            //check existence by mid in list<watchlist>
            String m = w[0].getMid();
            List<Watchlist> wl = watchlistViewModel.getListUserWatchlist(user);//null pointer.
            List<String> midlist = new ArrayList<String>();
            for(Watchlist wa:wl){
                midlist.add(wa.getMid());
            }
            if(!midlist.contains(m)){
                //execute insertion.
                watchlistViewModel.insert(w[0]);
            }
            return null;
        }
        protected void onPostExecute(String textResult){

        }
    }
    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }
}