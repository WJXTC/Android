package com.example.myapplication;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Snippet {
    public static List<String[]> getSnippet(String textResult){

        List<String[]> snippet = new ArrayList<String[]>();
        try{
            /*JSON exploror:(root):4 Items page:1 total_results:121 total_pages:7
            results20 Items*/
            JSONObject jsonObject = new JSONObject(textResult);
            //first level of json array:
            JSONArray j1 = jsonObject.getJSONArray("results");
            for(int i = 0;i< j1.length();i++){
                JSONObject row =  j1.getJSONObject(i);
                String[] s = new String[9];
                //the genre, the cast, the release date, the country, the name of the director(s),
                // a synopsis/plot summary/storyline (from a movie API).
                s[0]= row.getString("title");//String type of value.
                s[1]= row.getString("release_date");
                s[2]= row.getString("poster_path");//get filepath
                s[3]= row.getString("id");
                s[4]= row.getString("vote_average");
                //row.getJSONArray("genre_id");
                s[5] = row.getString("genre_ids");

                s[6]= row.getString("overview");//story*/
                //s[7] = "";
                //s[8] = "";
                snippet.add(s);
            }
        }
        catch (Exception e){ e.printStackTrace();
            String[] a = {"NO MOVIE FOUND","","",""};
            snippet.add(a) ;
            //Toast.makeText(getActivity(),"NO MOVIE FOUND",Toast.LENGTH_LONG).show();
        }
        return snippet;
    }

    public static HashMap<String, List<String>> getDetailSnippet(String textResult){
        //HashMap<String, ArrayList<String>>
        HashMap<String, List<String>> snippet = new HashMap<String, List<String>>();
        try{
            /*JSON exploror:(root):4 Items page:1 total_results:121 total_pages:7
            results20 Items*/

            JSONObject jsonObject = new JSONObject(textResult);//root
            //first level of json array:
            JSONArray j1 = jsonObject.getJSONArray("genres");
            List<String> g = new ArrayList<String>();
            List<String> c = new ArrayList<String>();
            List<String> s = new ArrayList<String>();
            List<String> o = new ArrayList<String>();
            List<String> p = new ArrayList<String>();
            for(int i = 0; i<j1.length();i++){
                HashMap<String,Object> map = new HashMap<String,Object>();
                JSONObject object = j1.getJSONObject(i);
                String genres = object.getString("name");
                g.add(genres);
            }
            //get genres
            //get country
            JSONArray j2 = jsonObject.getJSONArray("production_countries");
            for(int i = 0;i< j2.length();i++){
                JSONObject object = j2.getJSONObject(i);
                String country = object.getString("name");
                c.add(country);
            }
            //get overview.
            String score = jsonObject.getString("vote_average");
            String overview = jsonObject.getString("overview");
            String poster = jsonObject.getString("poster_path");
            s.add(score);
            o.add(overview);
            p.add(poster);
            snippet.put("genres",g);
            snippet.put("country",c) ;
            snippet.put("score",s);
            snippet.put("overview",o);
            snippet.put("poster",p);
        }
        catch (Exception e){ e.printStackTrace();
            ArrayList<String> a = new ArrayList<String>(){};
            a.add("no movie found");
            snippet.put("NO MOVIE FOUND",a);            //Toast.makeText(getActivity(),"NO MOVIE FOUND",Toast.LENGTH_LONG).show();
        }
        return snippet;
    }

    public static HashMap<String, ArrayList<String>> getCreditSnippet(String textResult){
        HashMap<String, ArrayList<String>> snippet = new HashMap<String, ArrayList<String>>();

        //List<List<String>> snippet = new ArrayList<String[]>();
        try{
            /*JSON exploror:(root):4 Items page:1 total_results:121 total_pages:7
            results20 Items*/

            JSONObject jsonObject = new JSONObject(textResult);//root
            //first level of json array:
            JSONArray j1 = jsonObject.getJSONArray("cast");
            ArrayList<String> g = new ArrayList<String>();
            ArrayList<String> c = new ArrayList<String>();
            for(int i = 0; i<j1.length();i++){
                HashMap<String,Object> map = new HashMap<String,Object>();
                JSONObject object = j1.getJSONObject(i);
                String cast = object.getString("name");
                g.add(cast);
            }
            //get genres
            //get country
            JSONArray j2 = jsonObject.getJSONArray("crew");
            for(int i = 0;i< j2.length();i++){
                JSONObject object = j2.getJSONObject(i);
                String crew = object.getString("name");
                c.add(crew);
            }
            snippet.put("cast",g);
            snippet.put("crew",c) ;
        }
        catch (Exception e){ e.printStackTrace();
            ArrayList<String> a = new ArrayList<String>(){};
            a.add("no movie found");
            snippet.put("NO MOVIE FOUND",a);            //Toast.makeText(getActivity(),"NO MOVIE FOUND",Toast.LENGTH_LONG).show();
        }
        return snippet;
    }

    public static List<BarEntry> getMonthMemoir(String textResult){
        List<BarEntry> snippet = new ArrayList<>();
        //HashMap<String,List<String>> snippet = new HashMap<String,List<String>>;
        try{
            //[{"month":2,"noOfMemoir":1},{"month":3,"noOfMemoir":1}]
            JSONArray j1 = new JSONArray(textResult);
            //int[] check = new int[11];
            //first level of json array
            for(int i=0;i<j1.length();i++){
                //String[] s = new String[2];
                int m = j1.getJSONObject(i).getInt("month");
                int num = j1.getJSONObject(i).getInt("noOfMemoir");
                //List<Integer> month = new List<>{1,2,3,4,5,6,7,8,9,10,11,12};
               //BarEntry(xvalue, yvalue)
                snippet.add(new BarEntry(m,num));//{[month,#],[month,#]....}
            }
            /*int[] month = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
            for(int e=0;e<month.length;e++){
                if(m not in month[e]){
                    snippet.add(new BarEntry(month[3],0));
                }
            }*/
        }
        catch (Exception e){ e.printStackTrace();
            e.printStackTrace();
            //Toast.makeText(getActivity(),"NO MOVIE FOUND",Toast.LENGTH_LONG).show();
        }
        return snippet;
    }

    public static List<PieEntry> getSuburbMemoir(String textResult){
        List<PieEntry> snippet = new ArrayList<>();
        //HashMap<String,List<String>> snippet = new HashMap<String,List<String>>;
        try{
            //[{"month":2,"noOfMemoir":1},{"month":3,"noOfMemoir":1}]
            JSONArray j1 = new JSONArray(textResult);
            //first level of json array
            for(int i=0;i<j1.length();i++){
                String[] s = new String[2];
                String m = j1.getJSONObject(i).getString("suburb");
                String num = j1.getJSONObject(i).getString("noOfMemoir");
                PieEntry pie = new PieEntry(Float.parseFloat(num),m);//PieEntry(value, label)
                snippet.add(pie);//{[month,#],[month,#]....}
            }
        }
        catch (Exception e){ e.printStackTrace();
            e.printStackTrace();
        }
        return snippet;
    }

    public static List<String[]> getCinema(String textResult){
        List<String[]> snippet = new ArrayList<String[]>();
        try{
            //[{"cid":1,"cinemaname":"Hoyts","location":"Melbourne"}]
            String cid = null;
            String c =null;
            String l = null;
            String cinema = null;
            JSONArray ja = new JSONArray(textResult);
            for (int i=0;i<ja.length();i++) {
                cid = ja.getJSONObject(i).getString("cid");
                c = ja.getJSONObject(i).getString("cinemaname");
                l = ja.getJSONObject(i).getString("location");
                cinema = c + " " + l;
                String[]  str = new String[]{cid,cinema};
                snippet.add(str);
            }
        }
        catch (Exception e){ e.printStackTrace();
            String[] a = {"NO Cinema FOUND for current year","","",""};
            snippet.add(a) ;
            //Toast.makeText(getActivity(),"NO MOVIE FOUND",Toast.LENGTH_LONG).show();
        }
        return snippet;
    }

    public static List<String[]> getCinema2(String textResult){
        List<String[]> snippet = new ArrayList<String[]>();
        try{
            //[{"cid":1,"cinemaname":"Hoyts","location":"Melbourne"}]
            String cid = null;
            String c =null;
            String l = null;
            String cinema = null;
            JSONArray ja = new JSONArray(textResult);
            for (int i=0;i<ja.length();i++) {
                cid = ja.getJSONObject(i).getString("cid");
                c = ja.getJSONObject(i).getString("cinemaname");
                l = ja.getJSONObject(i).getString("location");
                //cinema = c + " " + l;
                String[]  str = new String[]{c+" "+l};
                snippet.add(str);
            }
        }
        catch (Exception e){ e.printStackTrace();
            String[] a = {"NO Cinema FOUND for current year","","",""};
            snippet.add(a) ;
            //Toast.makeText(getActivity(),"NO MOVIE FOUND",Toast.LENGTH_LONG).show();
        }
        return snippet;
    }

    public static List<String[]> geocode(String s){
        //only returns the first result.
        List<String[]> list = new ArrayList<>();
        String[] location = new String[2];
        try {
            JSONObject jo1 = new JSONObject(s);//get ~
            JSONArray j1 = jo1.getJSONArray("results");//number location found.
            //JSONArray j2= j1.getJSONArray("geometry");
            /*JSONArray j2 = j1.getJSONObject(0).getJSONArray("geometry");
            String lat = j2.getJSONObject(0).getString("lat");
            String lng = j2.getJSONObject(0).getString("lng");
            location[0] = lat;
            location[1] = lng;*/
            for(int i=0;i<j1.length();i++){
                JSONObject jo2 = j1.getJSONObject(i).getJSONObject("geometry");//get geometry level
                String lat = jo2.getJSONObject("location").getString("lat");
                String lng = jo2.getJSONObject("location").getString("lng");
                //String name = jo2.getJSONObject()
                location[0] = lat;
                location[1] = lng;
                list.add(location);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;//return all location meet the requirements.
    }


    public static List<String[]> getMemoir(String s){
        //[{"cid":{"cid":1,"cinemaname":"Hoyts","location":"Melbourne"},"comment":"GOOD MOVIE","mid":43,"moviename":"The Lion King","rating":80,"releasedate":"2019-06-22T00:00:00+10:00","uid":{"address":"1 Porter St","dob":"1993-01-05T00:00:00+11:00","gender":"Female","name":"Angela","postcode":"3123","state":"VIC","surname":"Li","uid":27},"watchtime":"2019-07-13T00:00:00+10:00"}]
        //snippet textResult. array list <>of memoirs that including all information[].
        //only returns the first result.
        List<String[]> list = new ArrayList<>();
        //i.e[location,comment,mid,moviename,rate,releasedate,watchtime]
        try {
            JSONArray j1 = new JSONArray(s);
            //for each object
            for(int i=0;i<j1.length();i++){
                String[] memoir = new String[10];

                //get cinema info.
                String l1 = j1.getJSONObject(i).getJSONObject("cid").getString("cinemaname");
                String l2 = j1.getJSONObject(i).getJSONObject("cid").getString("location");
                //get comment
                String c = j1.getJSONObject(i).getString("comment");
                //get mid
                String mid = j1.getJSONObject(i).getString("mid");
                //get moviename
                String n =j1.getJSONObject(i).getString("moviename");
                //get rate
                String r =j1.getJSONObject(i).getString("rating");
                //get release date
                String rd = j1.getJSONObject(i).getString("releasedate");
                //get watchdate
                String wd = j1.getJSONObject(i).getString("watchtime");
                memoir[0] = l1+" "+ l2;
                memoir[1] = c;
                memoir[2] = mid;
                memoir[3] =n;
                memoir[4] = r;
                memoir[5] = rd;
                memoir[6] = wd;
                list.add(memoir);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;//return all location meet the requirements.
    }

    public static String getCid(String s){
     //[{"cid":1,"cinemaname":"Hoyts","location":"Melbourne"},{}]
        String cid = null;
        try {
            JSONArray j1 = new JSONArray(s);
            //only get the first result.
            cid = j1.getJSONObject(0).getString("cid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cid;//return all location meet the requirements.
    }

}
