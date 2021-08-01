package com.example.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Snippet;
import com.example.myapplication.networkconnection.API;
import com.example.myapplication.networkconnection.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MemoirFragment extends Fragment {
    private View v;
    private String uid,sort,filter;
    private TextView tvsort,tvfilter ;
    private Spinner spsort,spfilter;
    private SharedPreferences sp;
    private Button apply;
    ArrayList<HashMap<String, Object>> mvListArray;
    SimpleAdapter mvListAdapter;
    ListView mvList;



    String[] colHEAD = new String[] {"img","title","release","star","watch","suburb","comment"};
    int[] dataCell = new int[] {R.id.mmlv_image,R.id.mmlv_tv_moviename,R.id.mmlv_tv_release,R.id.mmlv_rb_score,R.id.mmlv_tv_date_ad,R.id.mmlv_tv_sub,R.id.mmlv_tv_com};

    //String[] colHEAD = new String[] {"title","release","star","watch","suburb","comment"};
    //int[] dataCell = new int[] {R.id.mmlv_tv_moviename,R.id.mmlv_tv_release,R.id.mmlv_rb_score,R.id.mmlv_tv_date_ad,R.id.mmlv_tv_sub,R.id.mmlv_tv_com};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_memoir, container, false);
        //declare views.
        tvsort = v.findViewById(R.id.mm_tv_sort1);
        tvfilter = v.findViewById(R.id.mm_tv_filter1);
        spsort = v.findViewById(R.id.mm_sp_sort2);
        spfilter = v.findViewById(R.id.mm_sp_filter2);
        //apply = v.findViewById(R.id.mm_btn_apply);
        //declare list
        mvList = v.findViewById(R.id.mm_lv);
        //get UID.
        uid = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("uid",null);
        //register spinner.
        spsort.setSelection(0);
        spsort.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        spfilter.setSelection(0);
        MemoirAsync m = new MemoirAsync();
        m.execute(uid);
        return v;
    }


    public void onViewCreated(View view,Bundle savedInstanceState){
        //ListView mvList = (ListView) v.findViewById(R.id.mm_lv);
        //MemoirAsync m = new MemoirAsync();
        //m.execute(uid);
    }
    private class MemoirAsync extends AsyncTask<String, Void,List<String[]>> {
        @Override
        protected List<String[]> doInBackground(String... strings) {
            String textResult = RestClient.getUserMemoir(strings[0]);
            //HashMap<String,List<String[]>> map = new HashMap<>();
            //[{"cid":{"cid":1,"cinemaname":"Hoyts","location":"Melbourne"},"comment":"GOOD MOVIE","mid":43,"moviename":"The Lion King","rating":80,"releasedate":"2019-06-22T00:00:00+10:00","uid":{"address":"1 Porter St","dob":"1993-01-05T00:00:00+11:00","gender":"Female","name":"Angela","postcode":"3123","state":"VIC","surname":"Li","uid":27},"watchtime":"2019-07-13T00:00:00+10:00"}]
            //snippet textResult. array list <>of memoirs that including all information[].
            List<String[]> snippet = Snippet.getMemoir(textResult);
            for(String[] str : snippet) {
                String textResult2 = API.searchMovie(str[3]);
                //HashMap<String, ArrayList<String>>
                //ArrayList<String> fullinfo= new ArrayList<String>(Arrays.asList(str));
                //String[] mv = new String[2];
                //fullinfo.add(Snippet.getDetailSnippet(textResult2).get("genres").get(0));
                //fullinfo.add(Snippet.getDetailSnippet(textResult2).get("poster").get(0));
                List<String[]> snippet2 = Snippet.getSnippet(textResult2);
                //return only one movie.
                if (snippet2.size() < 1) {
                    str[7] = "[]";
                    str[8] = "[]";
                    str[9] = "[]";
                }
                else {
                    //Log.i("size", String.valueOf(snippet2.size()));
                    if (snippet2.get(0)[2] == null) {
                        str[7] = "[]";
                    }
                    String mid = snippet2.get(0)[3];
                    String textResult3 = API.getDetail(mid);
                    HashMap<String, List<String>> snippet3 = Snippet.getDetailSnippet(textResult3);
                    /*if (snippet2.get(0)[5] == null) {
                        str[8] = "[]";
                    }
                    str[8] = snippet2.get(0)[5];//genres*/
                    str[7] = snippet2.get(0)[2];

                    str[8] = snippet3.get("genres").toString().replaceAll("\\[\\]", "");
                    //Log.i("genres",str[8]);//[Animation, Family, Adventure]
                    //Snippet.getDetailSnippet(textResult2).get("poster").get(1);
                    str[9] = mid;

                }
            }

            mvListArray = new ArrayList<HashMap<String, Object>>();
            for(String[] i: snippet){
                HashMap<String,Object> map = new HashMap<String,Object>();
                //get all info snippet
                //String[] colHEAD = new String[] {"img","title","release","star","watch","suburb","comment"};
                map.put("suburb",i[0]);
                map.put("comment",i[1]);
                map.put("mid",i[2]);//mid
                map.put("title",i[3]);
                map.put("star",i[4]);
                map.put("release",i[5]);
                map.put("watch",i[6]);
                //Bitmap img.
                String urlImage = "http://image.tmdb.org/t/p/w500/" + i[7];
                //map.put("poster",urlImage);
                Bitmap url = getBitmap(urlImage);//can only execute in async task
                map.put("img",url);
                map.put("genres",i[8]);
                map.put("mid",i[9]);
                //get img path from movie api by movie name.
                mvListArray.add(map);//get~
            }
            //[location,comment,mid,moviename,rate,releasedate,watchtime]
            return snippet;//get
        }
        protected void onPostExecute(List<String[]> a) {//same result for 15 times.


            //sort by watch
            //default release date watch date rate</item>
            setMyAdapter(mvListArray);
            //set onitem select listener
            mvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("movie list position", String.valueOf(position));
                    int posititonNum = position;
                    HashMap<String, String> map = (HashMap<String, String>) mvList.getItemAtPosition(position);//get item by its position.
                    String mid = map.get("mid");//get item info.
                    String release = map.get("release");
                    String name = map.get("title");
                    Fragment fragment = new MovieViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("mid",mid);
                    bundle.putString("release date", release);
                    bundle.putString("movie", name);
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }
            });

            spfilter.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String filter = parent.getItemAtPosition(position).toString();
                    List<HashMap<String, Object>> newlist = new ArrayList<>();
                    //mvListArray.clear();
                    if (filter.equals("Show All")){
                        setMyAdapter(mvListArray);
                    }
                    else{
                        for(HashMap<String,Object> h: mvListArray){
                            String[] genres_list = h.get("genres").toString().replaceAll("[\\[\\]\"]", "").split(",");
                            //Arrays. asList(genres_list);
                            if(Arrays. asList(genres_list).contains(filter)){
                                newlist.add(h);
                            }
                        }
                        setMyAdapter(newlist);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });
        }
    }
    public Bitmap getBitmap(String u){
        Bitmap mBitmap = null;
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }
    private void setMyAdapter(List<HashMap<String, Object>> ma){
        mvListAdapter = new SimpleAdapter(getContext(),ma,R.layout.memoirlv,colHEAD,dataCell);
        mvListAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof RatingBar){
                    String stringval = String.valueOf(data);//parse object to string
                    float ratingValue = Float.parseFloat(stringval);//parse string to float
                    RatingBar ratingBar = (RatingBar) view;//identify view.
                    ratingBar.setRating(ratingValue);//set value to rating bar
                    return true;
                }
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView img = (ImageView) view;
                    Bitmap bit = (Bitmap)data;
                    img.setImageBitmap(bit);
                    return true;
                }
                return false;
            }
        });
        mvList.setAdapter(mvListAdapter);
    }
}
