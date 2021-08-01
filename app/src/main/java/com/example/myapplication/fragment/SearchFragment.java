package com.example.myapplication.fragment;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.myapplication.networkconnection.API;
import com.example.myapplication.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.myapplication.Snippet;

public class SearchFragment extends Fragment {
    private View v;
    private String mParam2;
    private String searchContent;
    ArrayList<HashMap<String, Object>>mvListArray;
    SimpleAdapter mvListAdapter;
    ListView mvList;

    String[] colHEAD = new String[] {"poster","movie","release date"};
    //defined in the listview layout!!!not in the fragment layout
    int[] dataCell = new int[] {R.id.iv_search_poster,R.id.tv_search_name,R.id.tv_search_release};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_search, container, false);
        //v.findViewById(R.id.search_searchlv);
        //declare view objects.
        final EditText et = v.findViewById(R.id.et_search);
        Button btn = v.findViewById(R.id.bt_search);
        mvList = v.findViewById(R.id.search_searchlv);//view object of listview to be used for displaying movies.
        //mvListArray = new ArrayList<HashMap<String, String>>();//data to be displayed.
        //set onclick listener to button.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContent = et.getText().toString();
                searchContent.trim();//this api already case insensitive!
                SearchAsync s = new SearchAsync();
                s.execute(searchContent);
            }
        });

        return v;
    }

    private class SearchAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String textResult = API.searchMovie(strings[0]);
            mvListArray = new ArrayList<HashMap<String, Object>>();
            //List<String[]> snippet = getSnippet(textResult);
            //"image","movie","release date","score"};
            List<String[]> snippet = Snippet.getSnippet(textResult);
            for(int i=0;i<snippet.size();i++){
                HashMap<String,Object> map = new HashMap<String,Object>();
                map.put("movie", snippet.get(i)[0]);
                map.put("release date", snippet.get(i)[1]);
                String urlImage = "http://image.tmdb.org/t/p/w500/" + snippet.get(i)[2];
                //map.put("poster",urlImage);
                Bitmap url = getBitmap(urlImage);//can only execute in async task
                // do netwrok related operation on Background threads like AsyncTask!!!!!!
                map.put("poster",url);
                map.put("id",snippet.get(i)[3]);
                map.put("score",snippet.get(i)[4]);
                map.put("genre",snippet.get(i)[5]);
                map.put("overview",snippet.get(i)[6]);
                map.put("strurl",urlImage);
                mvListArray.add(map);//success
            }
            return API.searchMovie(strings[0]);
        }
        protected void onPostExecute(String textResult) {

            mvListAdapter = new SimpleAdapter(getContext(),mvListArray,R.layout.searchlv,colHEAD,dataCell);
            mvListAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
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
            //set onclick listener to listview widget.
            mvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Log.i("movie list position", String.valueOf(position));
                    int posititonNum = position;
                    HashMap<String, String> map = (HashMap<String, String>) mvList.getItemAtPosition(position);//get item by its position.
                    String mid = map.get("id");//get item info.
                    String release = map.get("release date");
                    String name = map.get("movie");
                    String score = map.get("score");
                    String overview = map.get("overview");
                    String genre = map.get("genre");
                    String img = map.get("strurl");
                    Fragment fragment = new MovieViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("mid",mid);
                    bundle.putString("release date", release);
                    bundle.putString("movie", name);
                    bundle.putString("score", score);
                    bundle.putString("overview",overview);
                    bundle.putString("genre",genre);
                    bundle.putString("img",img);
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
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

}
