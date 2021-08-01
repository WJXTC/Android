package com.example.myapplication.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.RegisterActivity;
import com.example.myapplication.networkconnection.RestClient;

public class LoginFragment extends Fragment {
    private EditText username;
    private EditText password;
    private View login;
    private SharedPreferences sp;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        login= inflater.inflate(R.layout.activity_login, container, false);

        Button btn = login.findViewById(R.id.activity_login_btn);
        username = login.findViewById(R.id.activity_username);
        password = login.findViewById(R.id.activity_password);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check null
                String user = username.getText().toString();
                String psw = password.getText().toString();
                //set an array to be passed to async task
                String[] str = new String[2];
                str[0] = user;
                str[1] = psw;
                //check validation
                LoginAsync validateUser = new LoginAsync();
                if(!str[0].isEmpty() || !str[1].isEmpty()){
                    validateUser.execute(str);}
                else Toast.makeText(getActivity(),"empty",Toast.LENGTH_LONG).show();

            }
        });
        //sign up button
        Button btn2 = login.findViewById(R.id.activity_register_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        return login;
    }

    //when connect to db, async execution.and protect from time out.
    private class LoginAsync extends AsyncTask<String, Void, String> {

        //... means that zero or more String objects (or an array of them) may be passed as the argument(s) for that method.

       // android.os.AsyncTask<Params: type sent to task upon execution , Progress: type during background computation ,
        // Result: type of result of background computation. >
        // Void : if you do not need one of these.

        @Override
        protected String doInBackground(String... strings) {
            //set user and psw value
            String user = strings[0];
            String psw = strings[1];
            return RestClient.findByUsernameAndPsw(user,psw);
        }

        protected void onPostExecute(String textResult) {
            //regex to replace the [] to empty.
            String str = textResult.replaceAll("[\\[\\]\"]", "");
            System.out.println(str);
            Log.v("boolean", Boolean.toString(textResult.isEmpty()));
            if(str.equals("")){
                Toast.makeText(getActivity(),"invalid username or password",Toast.LENGTH_LONG).show();
            }
            else{
                String str2 = str.replaceAll("[\\{\\}]","");
                String[] details = str2.split(",");
                sp=getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("uid", details[9].split(":")[1]);
                Log.i("details",details[9].split(":")[1]);//get uid from appuser info.
                Log.i("username",details[10].split(":")[1]);//get username from credentials
                //editor.putString("password", details[1].split(":")[1]);
                editor.putString("username", details[10].split(":")[1]);
                editor.putString("firstname", details[5].split(":")[1]);
                editor.apply();
                //logged in and switch to home session.

                replaceFragment(new HomeFragment());

                //editor.putBoolean("remember",remember.isChecked());
            }
        }
    }
    //https://stackoverflow.com/questions/27678325/cannot-find-symbol-method-getsupportfragmentmanager
    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }
}
