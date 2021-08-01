package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Snippet;
import com.example.myapplication.networkconnection.API;
import com.example.myapplication.networkconnection.RestClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback{
    private View v;
    private MapView mMap;
    private GoogleMap googleMap;
    private Bundle args = null;
    private ProgressDialog mDialog;
    private String LoginID = null;
    private SharedPreferences sp;
    private String uid;
    //private ArrayList<SiteMap> mSiteMaps;
    private AlertDialog.Builder builder;
    private static final String LOG_TAG = MapFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_map, container, false);
        //GET mapfragment from fragment manager. and its child fragment manager.
        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return v;
    }
    // map is ready to be used and provides a non-null instance of GoogleMap.
    @Override
    public void onMapReady(GoogleMap gMap) {
        //get user id.
        sp=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        uid = sp.getString("uid",null);
        Log.i("uid",uid);
        //get user information.
        googleMap = gMap;
        Async async = new Async();
        async.execute(uid);

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getContext(),
                    marker.getTitle() + " has been clicked " + clickCount + " times.", Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    //find all cinema information and user information
    private class Async extends AsyncTask<String, Void, HashMap<String, List<String[]>>> {

        @Override
        protected HashMap<String,List<String[]>> doInBackground(String... params) {
            Log.i("uid",params[0]);
            HashMap<String,List<String[]>> map = new HashMap<String,List<String[]>>();
            String[] homelocation = new String[2];

            List<String[]> cinemas = new ArrayList<String[]>();
            List<String[]> home = new ArrayList<String[]>();
            //get home address
            String textResult1 = RestClient.findUserByUid(params[0]);//uid
            //get home location using geocoding
            //{"address":"1 Porter St","dob":"1993-01-05T00:00:00+11:00","gender":"Female",
            // "name":"Angela","postcode":"3123","state":"VIC","surname":"Li","uid":27}
            String address1=null;
            try {
                JSONObject jo = new JSONObject(textResult1);
                address1 = jo.getString("address")+ jo.getString("postcode");
                jo.getString("postcode");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //get address from geocode
            Log.i("address",address1);
            String result1 = API.searchAddress(address1);
            Log.i("result1",result1);
            //snippet the result
            List<String[]> location1 =Snippet.geocode(result1);//outofbound exeception, arraylist.get()
            Log.i("location1",location1.get(0)[0]);
            homelocation[0] = location1.get(0)[0];
            homelocation[1] = location1.get(0)[1];
            home.add(homelocation);
            //get cinema address and name.[{"cid":1,"cinemaname":"Hoyts","location":"Melbourne"}]
            String textResult2 = RestClient.getCinema();
            List<String[]> snippet = Snippet.getCinema2(textResult2);//{[cinema+location]}
            //get address from geocode.
            //get cinema location using geocoding
            List<String[]> location2 =null;
            for(int e=0;e<snippet.size();e++){
                String result2 = API.searchAddress(snippet.get(e)[0]);
                //snippet the result
                String[] cinemalocation = new String[2];
                location2 =Snippet.geocode(result2);
                cinemalocation[0] = location2.get(0)[0];
                cinemalocation[1] = location2.get(0)[1];
                cinemas.add(cinemalocation);
            }
            //add to hashmap
            map.put("user",home);
            map.put("cinema",cinemas);
            return map;
        }
        protected void onPostExecute(HashMap<String, List<String[]>> m) {
            HashMap<String, List<String[]>> hashmap = m ;
            LatLng home = new LatLng(Double.parseDouble(hashmap.get("user").get(0)[0]),Double.parseDouble(hashmap.get("user").get(0)[1]));
            //home value get.
            if(googleMap != null){
                googleMap.getUiSettings().setZoomControlsEnabled(true);//false by default
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(home));  /// error is shown in this line
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));//10 is city and 15 is street.
                googleMap.addMarker(new MarkerOptions().position(home).title("home"));
                List<String[]> clist = m.get("cinema");//11 same location.
                //LatLng cinema = new LatLng();
                for(String[] str : clist){
                    LatLng cinema = new LatLng(Double.parseDouble(str[0]), Double.parseDouble(str[1]));
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(cinema).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    marker.showInfoWindow();
                }
            }
            //set cinema

        }
    }
}
