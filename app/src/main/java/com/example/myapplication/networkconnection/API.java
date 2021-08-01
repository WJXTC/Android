package com.example.myapplication.networkconnection;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class API {
    private static final String API_KEY = "b95c6abc78eaefbfcd0aba52b6026c19";
    //private static final String SEARCH_ID_cx = "YOUR SEARCH ID CX";
    public static String searchMovie(String keyword) {
        //if there is any space in keyword for search, substitute by %
        keyword = keyword.replace(" ", "%20");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        try {
            url = new URL("https://api.themoviedb.org/3/search/movie?api_key="+ API_KEY+ "&language=en-US&query="+keyword+"&page=1&include_adult=false");
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000); connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) { textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace(); }
        finally{
            connection.disconnect(); }
        Log.i("api result",textResult);//get result ~
        return textResult;
    }

    public static String image(String id) throws MalformedURLException {
        //if there is any space in keyword for search, substitute by %
        //id = id.replace(" ", "%20");
        URL url = new URL("https://api.themoviedb.org/3/movie/" + id + "/images?api_key=b95c6abc78eaefbfcd0aba52b6026c19&language=en-US");
        //private
        //https://api.themoviedb.org/3/movie/550/images?api_key=b95c6abc78eaefbfcd0aba52b6026c19&language=en-US
        //final String base_url= "http://image.tmdb.org/t/p/w500/";//base url with w500 as poster size.
        HttpURLConnection connection = null;
        String textResult = "";
        try {

            //url = new URL(base_url + filepath);
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000); connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) { textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace(); }
        finally{
            connection.disconnect(); }
        Log.i("url_image",textResult);//get result ~
        return textResult;
    }


    public static String getDetail(String mid) {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        try {
            url = new URL("https://api.themoviedb.org/3/movie/"+mid+"?api_key=b95c6abc78eaefbfcd0aba52b6026c19&language=en-US");
            //Log.i("getDetailurl",url.toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000); connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) { textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace(); }
        finally{
            connection.disconnect(); }
        Log.i("api result",textResult);//get result ~
        return textResult;
    }

    public static String getCredit(String mid) {
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        try {
            url = new URL("https://api.themoviedb.org/3/movie/"+mid+"/credits?api_key=b95c6abc78eaefbfcd0aba52b6026c19");
            //Log.i("getDetailurl",url.toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000); connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) { textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace(); }
        finally{
            connection.disconnect(); }
        //Log.i("api result",textResult);//get result ~
        return textResult;
    }

    public static String searchAddress(String address) {
        //https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,
        //+Mountain+View,+CA&key=YOUR_API_KEY
        String add = address.replaceAll(" ","%20");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        try {
            url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+add+"&key=AIzaSyC2OQQGIHMmOWQ-nJh5NHQY5aoRJe6Re2Q");
            //Log.i("getDetailurl",url.toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000); connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) { textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace(); }
        finally{
            connection.disconnect(); }
        Log.i("api result",textResult);//get result ~
        return textResult;
    }

}