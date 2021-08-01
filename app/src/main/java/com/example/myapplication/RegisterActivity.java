package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.networkconnection.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstname,surname,postcode,email,password,address,confirmpsw;
    private String gender,state;
    private TextView dobtv;
    private Button register, dob;
    private DatePickerDialog picker;

//first name, surname, gender, DoB, address,state, postcode, email and password)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //edit text
        firstname = findViewById(R.id.register_firstname);
        surname = findViewById(R.id.register_surname);
        postcode = findViewById(R.id.register_postcode);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirmpsw = findViewById(R.id.register_confirm_password);
        address = findViewById(R.id.register_address);
        //textview
        dobtv = findViewById(R.id.register_tv_dob);
        //radio group
        RadioGroup groupgender = findViewById(R.id.register_gender);
        //spinner
        Spinner sp = findViewById(R.id.register_state);
        //button
        dob = findViewById(R.id.register_btn_dob);
        register= findViewById(R.id.register_btn);
        //spinner value
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        //radio group
        groupgender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbtn = findViewById(checkedId);
                switch (rbtn.getId()){
                    case R.id.register_female: gender = "Female"; break;
                    case R.id.register_male: gender = "Male";break;
                };
            }
        });
        // dob selection button
        dob.setOnClickListener(new View.OnClickListener() {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            @Override
            public void onClick(View v) {
                // date picker dialog pop up.
                picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //set textview for the date picked.
                                dobtv.setText( dayOfMonth+ "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
                //date picker pop up.
                //select from date picker
                //validate selection
                    // date after current date.
                    // empty value.
            }
        });
        //sign up button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AsyncRegister addAsyncTask = new AsyncRegister();
                //add all information together
                String fname = firstname.getText().toString();
                String sname = surname.getText().toString();
                String add = address.getText().toString();
                String post = postcode.getText().toString();
                String birth = dobtv.getText().toString();
                String mail = email.getText().toString();
                String psw = password.getText().toString();
                //String[] str = {fname,sname,gender,birth,add,state,post,mail,psw};
                //set up date 2016-08-31
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                Date date = null;
                try {
                    date = df.parse(birth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //create new instance of app user
                Appuser user = new Appuser(fname,sname,gender,date,add,state,post);
                Date current = new Date();
                Log.i("current", current.toString());
                AsyncRegister addAsyncTask = new AsyncRegister();
                //addAsyncTask.execute(user);
                //String uid =
                Credentials cred = new Credentials(mail,psw,current);
                // String[] vars = new String[8]

                addAsyncTask.execute(user,cred);
                //if (!str[6].isEmpty() || !str[7].isEmpty()) {

                    //credent.execute(cred);

                //}
            }
        });
    }


    private class AsyncRegister extends AsyncTask<Object, Void,String > {

        @Override
        protected String doInBackground(Object... o) {
            //splitting parts of the text based on the space between them
            // public Appuser(String name, String surname, String gender,Date dob,String address,String State,String postcode ) {
            Appuser u = (Appuser) o[0];
            Credentials c = (Credentials) o[1];
            //check existence of user.
            String flag = "";
            if(RestClient.findByUsername(c.getUsername()).equals("[]")){
                c.setUid(RestClient.register(u));
                RestClient.credentials(c);
              //
                flag = "true";
            }
            else{
              //  Looper.prepare();//Call looper.prepare()
             //
                //onCreate();
                flag ="false";
            }
            //why cannot insert uid to the credentials.
            return flag;
        }

        @Override
        protected void onPostExecute(String s) {
            //TextView resultTextView = findViewById(R.id.tvResult);
            //resultTextView.setText(response);
            if (s.equals("true")){
                Toast.makeText(getApplicationContext(),"register successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"username existed, please enter a new one",Toast.LENGTH_LONG).show();
            }

        }
    }
}
