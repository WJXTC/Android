package com.example.myapplication.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.myapplication.Cinema;
import com.example.myapplication.Memoir;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.Snippet;
import com.example.myapplication.networkconnection.RestClient;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AddMemoirFragment extends Fragment {
    private RatingBar rateOthers,rate;
    private TextView moviename, releasedate,tv_datepicker,tv_timepicker;
    private Button datepicker, addcinema,submit,timepicker;
    private EditText comment,cinema,location;
    private Spinner spinner;
    private String value_moviename,value_releasedate,value_img,value_cinema,uid;
    private Float value_rateOthers;
    private ImageView poster;
    private DatePickerDialog picker;
    private TimePickerDialog tpicker;
    private SharedPreferences sharep;
    //SpinnerAdapter spinnerAdapter;
    ArrayAdapter<String> spinnerAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            value_moviename = getArguments().getString("movie");
            value_releasedate = getArguments().getString("release");
            value_rateOthers = getArguments().getFloat("rate");
            value_img = getArguments().getString("poster");

        }
        sharep=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        uid = sharep.getString("uid",null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_memoir, container, false);
        moviename = v.findViewById(R.id.addmm_tv_movie);
        releasedate = v.findViewById(R.id.addmm_tv_release);
        rateOthers = v.findViewById(R.id.addmm_rb_rateother);
        poster = v.findViewById(R.id.addmm_im_poster);
        comment =v.findViewById(R.id.addmm_et_comment);
        rate = v.findViewById(R.id.addmm_rb_rate);
        //set values.
        moviename.setText(value_moviename);
        releasedate.setText(value_releasedate);
        rateOthers.setRating(value_rateOthers);
        //display poster.
        Picasso.get().load(value_img)
                .placeholder(R.mipmap.ic_launcher) .resize(500, 800)
                .centerInside()
                .into(poster);
        //spinner.
        spinner = v.findViewById(R.id.addmm_sp_cinema);
        //get cinema from db
        Async a = new Async();
        a.execute();
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value_cinema = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        //add new cinema
        addcinema = v.findViewById(R.id.addmm_btn_addcinema);
        cinema= v.findViewById(R.id.addmm_tv_addcinema);
        location = v.findViewById(R.id.addmm_tv_suburb);
        addcinema.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {

            String cin=cinema.getText().toString();
            String sub= location.getText().toString();
            String combine = cin+" " + sub;
            spinnerAdapter.add(combine);
            spinnerAdapter.notifyDataSetChanged();
            spinner.setSelection(spinnerAdapter.getPosition(combine));
            //add to database.
            AddCinemaAsync add = new AddCinemaAsync();
            Cinema c = new Cinema(cin,sub);
            add.execute(c);

            }
        });
        //register buttons.
        datepicker = v.findViewById(R.id.addmm_btn_datepicker);
        timepicker = v.findViewById(R.id.addmm_btn_timepicker);
        tv_datepicker = v.findViewById(R.id.addmm_tv_datepicker);

        tv_timepicker = v.findViewById(R.id.addmm_tv_timepicker);
        submit = v.findViewById(R.id.addmm_btn_submit);
        datepicker.setOnClickListener(new View.OnClickListener() {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            @Override
            public void onClick(View v) {
                // date picker dialog pop up.
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //set textview for the date picked.
                                datepicker.setText( dayOfMonth+ "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        timepicker.setOnClickListener(new View.OnClickListener() {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            int hour = cldr.get(Calendar.HOUR);
            int minute = cldr.get(Calendar.MINUTE);
            @Override
            public void onClick(View v) {
                tpicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int m) {
                        timepicker.setText(hourOfDay+":"+minute);
                    }
                }, hour, minute,true);
                tpicker.show();
            }
        });


        //sumbit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get values.
                /*value_moviename;
                value_cinema;
                value_img;
                value_releasedate;
                value_rateOthers;*/
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy HH:MM");
                //SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                //SimpleDateFormat df2 = new SimpleDateFormat("HH:MM:SS");
                Date watchdate = null;
                Date releasedate = null;
                try {
                    watchdate = df.parse(datepicker.getText().toString() + " "+timepicker.getText().toString());
                    releasedate = df2.parse(value_releasedate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String cinema = value_cinema.split(" ")[0];
                String suburb = value_cinema.split(" ")[1];
                //initialize class
                //Integer mid,String m,Date rd,Date wd,String comment, Integer rate,Appuser uid,Cinema cid
                Cinema c = new Cinema(cinema,suburb);
                Memoir m = new Memoir(value_moviename,releasedate,watchdate,comment.getText().toString(),rate.getNumStars()*2,Integer.parseInt(uid));
                //async to post to memoir.
                AsyncMemoir am = new AsyncMemoir();
                am.execute(c,m);
            }
        });

        return v;
    }

    private class Async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //get LIST OF CINEMA
            String a = RestClient.getCinema();
            Log.i("cinema",a);


            return a;
        }
        protected void onPostExecute(String a) {
            //snippet get cinema items.{[cid,cinema]}
            List<String[]> sp = Snippet.getCinema(a);
            List<String> cl = new ArrayList<>();
            for (int i=0;i<sp.size();i++){
                //Log.i("c",sp.get(i)[1]);//get.
                cl.add(sp.get(i)[1]);
                //final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_spinner_item, cl);
                spinnerAdapter = new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_spinner_item, cl);
                spinner.setAdapter(spinnerAdapter);
            }
        }
    }

    private class AddCinemaAsync extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... o) {
            //get LIST OF CINEMA
            Cinema c = (Cinema)o[0];
            RestClient.postCinema(c);
            return c;
        }
        protected void onPostExecute(Object o) {
            //snippet get cinema items.{[cid,cinema]}

        }
    }

    private class AsyncMemoir extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... o) {
            /*[{"cid":{"cid":1,"cinemaname":"Hoyts","location":"Melbourne"},"comment":"GOOD MOVIE",
            "mid":43,"moviename":"The Lion King","rating":80,"releasedate":"2019-06-22T00:00:00+10:00",
            "uid":{"address":"1 Porter St","dob":"1993-01-05T00:00:00+11:00","gender":"Female","name":"Angela",
            "postcode":"3123","state":"VIC","surname":"Li","uid":27},"watchtime":"2019-07-13T00:00:00+10:00"}*/
            //post cinema
            Cinema c = (Cinema)o[0];
            Memoir m = (Memoir)o[1];

            //RestClient.postCinema(c);

            //get cinema id. by cinemaname
            String cinemainfo = RestClient.findByCinemaname(value_cinema.split(" ")[0]);
            Log.i("cidtest",cinemainfo);
            Log.i("cidtest2",Snippet.getCid(cinemainfo));
            //int cid = 0;
            try{
                 int cid = Integer.parseInt(Snippet.getCid(cinemainfo));
                 //c.setCid(cid);//cid not null.
                    m.setCid(cid);
            }catch(NumberFormatException ex){
                Log.i("numberformat exception",Snippet.getCid(cinemainfo));
            }
            RestClient.postMemoir(m);//cid null????????
            return o[1];
        }
        protected void onPostExecute(Object o) {
            replaceFragment(new MemoirFragment());
        }
    }
    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }
}
