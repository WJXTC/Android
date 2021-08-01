package com.example.myapplication.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Snippet;
import com.example.myapplication.networkconnection.RestClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.github.mikephil.charting.components.YAxis.YAxisLabelPosition.OUTSIDE_CHART;

public class ReportFragment extends Fragment {
    private View v;
    private String year,uid;
    private Spinner sp;
    SharedPreferences sharep;
    private PieChart pie;
    private BarChart bar;
    private Button start,end,show;
    private DatePickerDialog picker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_report, container, false);
        pie = (PieChart) v.findViewById(R.id.report_pie);
        bar = (BarChart) v.findViewById(R.id.report_bar);
        sp = v.findViewById(R.id.report_sp_year);
        start = v.findViewById(R.id.report_btn_stdate);
        end = v.findViewById(R.id.report_btn_eddate);
        show = v.findViewById(R.id.report_btn_show);
        //set default value for spinner
        sp.setSelection(5);
        sharep=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        uid = sharep.getString("uid",null);
        year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));//???
        //get year from spinner.
        renderBar();
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
                Log.i("year2",year);
                getBarData barData = new getBarData();
                barData.execute(uid,year);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }

        });
        //set default value
        getPieData p = new getPieData();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        start.setText(String.valueOf(year) + "-01-01");
        end.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
        p.execute(uid,start.getText().toString(),end.getText().toString());

        //register button listener.
        start.setOnClickListener(new View.OnClickListener() {
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
                                start.setText(  year + "-" +(monthOfYear + 1)+ "-"+ dayOfMonth );
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
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
                                end.setText( year + "-" +(monthOfYear + 1)+ "-"+ dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pie chart render. num of memoir per suburb. during selected period.
               // suburbMemoir(/uidstart/end)
                getPieData p = new getPieData();
                p.execute(uid,start.getText().toString(),end.getText().toString());
            }
        });

        return v;

    }

    private class getPieData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            //get [{"suburb":"Docklands","noOfMemoir":1},{"suburb":"Melbourne","noOfMemoir":1}]
            return RestClient.suburbMemoir(strings[0],strings[1],strings[2]);//uid start and end.
        }
        protected void onPostExecute(String textResult){
            List<PieEntry> snippet = Snippet.getSuburbMemoir(textResult);//{[month,#],[month,#]....}
            PieDataSet pdataset = new PieDataSet(snippet,"");
            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(getResources().getColor(R.color.pie1));
            colors.add(getResources().getColor(R.color.pie2));
            colors.add(getResources().getColor(R.color.pie3));
            colors.add(getResources().getColor(R.color.pie4));
            colors.add(getResources().getColor(R.color.pie5));
            pdataset.setColors(colors);
            PieData pdata = new PieData();
            pdata.setDataSet(pdataset);
            pie.getDescription().setEnabled(false);
            pie.setExtraOffsets(5,10,5,5);

            pie.setData(pdata);
            pie.invalidate();
            pie.setNoDataText("No Data is Available");


        }
    }

    private void renderBar() {
        XAxis xAxis = bar.getXAxis();//get x axis
        YAxis leftAxis = bar.getAxisLeft();//get y axis
        leftAxis.setAxisMinimum(0f);
        leftAxis.setSpaceTop(10f);
        leftAxis.setLabelCount(10, false);//not nessassary to be 10.
        leftAxis.setPosition(OUTSIDE_CHART);
        xAxis.setLabelCount(12);
        /*xAxis.setAxisMaximum(15f);
        xAxis.setAxisMinimum(1f);
        xAxis.setDrawLimitLinesBehindData(true);
        leftAxis.removeAllLimitLines();


        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        bar.getAxisRight().setEnabled(false);*/
    }
    //get data from netbean.
    // BAr: total number of movies watched per month for the selected year.
    private class getBarData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.i("year",strings[1]);
            return RestClient.monthMemoir(strings[0],strings[1]);//uid and year
        }
        protected void onPostExecute(String textResult){
            List<BarEntry> entries = Snippet.getMonthMemoir(textResult);//{[month,#],[month,#]....}

            String[] xAxisLables = new String[]{"Jan","Feb","March","April","May","Jun","July","Aug","Sep","Oct","Nov","Dec"};
            bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLables));
            bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            bar.setScaleXEnabled(false);
            //bar.getXAxis().setXAxisMaximum(20);
            bar.getXAxis().setGranularity(1f);
            bar.getXAxis().setAxisMinimum(0f);
            bar.getXAxis().setAxisMaximum(12f);
            bar.getAxisLeft().setGranularity(1f);
            bar.getAxisRight().setGranularity(1f);
            BarDataSet barDataset = new BarDataSet(entries,"No. Of Memoir");
            barDataset.setColor(getResources().getColor(R.color.pie1));
            BarData bardata = new BarData(barDataset);
            bar.setData(bardata);
            bar.invalidate();//refresh
        }
    }
       /* month.add("Jan");month.add("Feb");month.add("Mar");month.add("Apr");
        month.add("May");month.add("Jun");month.add("Jul");month.add("Aug");
        month.add("Sep");month.add("Oct");month.add("Nov");month.add("Dec");*/

}
