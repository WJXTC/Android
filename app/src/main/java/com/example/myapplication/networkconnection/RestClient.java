package com.example.myapplication.networkconnection;

import android.util.Log;
import android.widget.EditText;

import com.example.myapplication.Appuser;
import com.example.myapplication.Cinema;
import com.example.myapplication.Credentials;
import com.example.myapplication.Memoir;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient {
    private EditText et;
    private static final String BASE_URL = "http://192.168.20.3:46879/WebApplication1/webresources/";
    public static String findByUsernameAndPsw(String usr, String psw) {
        final String methodPath = "assignment1.credentials/findByUsernameAndPsw/";
        //http://192.168.20.3:46879/Assignment1/webresources/assignment1.credentials/findByUsernameAndPsw/heidi@gmail.com/1234567
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + usr + "/" + psw); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        System.out.println(textResult);

        return textResult;

    }// first method closed.

    // add new user.
    public static Appuser register(Appuser user){ //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="assignment1.appuser/";
        try {
            //Gson gson =new Gson();00:00:00+11:00
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            //create json string for the added user.
            String stringUserJson=gson.toJson(user);
            Log.i("register user activity",stringUserJson);
            url = new URL(BASE_URL + methodPath);
            Log.i("register activity2",url.toString());

//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return user;//pass the user back to the register activity
    }

    //add credentials
    // add new user.
    public static Credentials credentials(Credentials credent){ //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="assignment1.credentials/";
        Log.i("register activity",methodPath);
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            //create json string for the added user.
            String stringUserJson=gson.toJson(credent);
            Log.i("register activity",stringUserJson);
            url = new URL(BASE_URL + methodPath);
            Log.i("url",url.toString());
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return credent;//pass the user back to the register activity
    }

    public static String findByTopFive(String uid) {
        final String methodPath = "assignment1.memoir/topFive/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + uid ); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                //System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);

        return textResult;

    }// first method closed.

    public static String monthMemoir(String uid,String year) {
        final String methodPath = "assignment1.memoir/monthMemoir/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + uid+"/"+year ); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);

        return textResult;

    }

    public static String suburbMemoir(String uid,String start,String end) {
        final String methodPath = "assignment1.memoir/suburbMemoir/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + uid+"/"+start + "/"+end); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);
        return textResult;

    }

    public static String getCinema() {
        final String methodPath = "assignment1.cinema";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);

        return textResult;

    }
//get all user memoir by uid.
    public static String getUserMemoir(String uid) {
        //http://localhost:8080/Assignment1/webresources/assignment1.memoir/findByUid/27
        final String methodPath = "assignment1.memoir/findByUid/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + uid ); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);

        return textResult;

    }
//post memoir to the db.
    public static Memoir postMemoir(Memoir m){ //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="assignment1.memoir/";
        Log.i("register activity",methodPath);
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            //create json string for the added user.
            String stringUserJson=gson.toJson(m);
            Log.i("register activity",stringUserJson);
            url = new URL(BASE_URL + methodPath);
            Log.i("url",url.toString());
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return m;//pass the user back to the register activity
    }

    public static String findUserByUid(String uid) {
        //http://localhost:8080/Assignment1/webresources/assignment1.appuser/findByUid/27
        final String methodPath = "assignment1.appuser/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath +uid); //open the connection
            Log.i("userurl",url.toString());
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //Log.i("userinfo",textResult);

        return textResult;

    }

    public static String findAllCinema() {
        //http://localhost:8080/Assignment1/webresources/assignment1.appuser/findByUid/27
        final String methodPath = "assignment1.cinema/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);

        return textResult;

    }

    public static String findByUsername(String u) {
        //http://localhost:8080/Assignment1/webresources/assignment1.credentials/findByUsername/corinee@gmail.com
        final String methodPath = "assignment1.credentials/findByUsername/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + u ); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                //System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);

        return textResult;

    }// first method closed.

    public static Cinema postCinema(Cinema m){ //initialise
        URL url = null;
        HttpURLConnection conn = null;
        final String methodPath="assignment1.cinema/";
        Log.i("register activity",methodPath);
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
            //create json string for the added user.
            String stringUserJson=gson.toJson(m);
            Log.i("register activity",stringUserJson);
            url = new URL(BASE_URL + methodPath);
            Log.i("url",url.toString());
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
//set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUserJson.getBytes().length);
            //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
//Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUserJson);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return m;//pass the user back to the register activity
    }


    public static String findByCinemaname(String name) {
        final String methodPath = "assignment1.cinema/findByCinemaname/";
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        //String uid = c.getUid();
//Making HTTP request
        try {
            url = new URL(BASE_URL + methodPath + name); //open the connection
            System.out.println(url);
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //Read the response
            Scanner inStream = new Scanner(conn.getInputStream()); //read the input stream and store it as string
            while (inStream.hasNextLine()) { textResult += inStream.nextLine();
                System.out.println(textResult);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally { conn.disconnect();
        }
        //System.out.println(textResult);
        return textResult;

    }
}

