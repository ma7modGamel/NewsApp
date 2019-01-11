package com.example.xxpc.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class FetchNewsData extends AsyncTaskLoader<ArrayList<newsModel>> {

    ArrayList<newsModel> arrayListnews;
    String url;
    StringBuilder sb = null;
    RecyclerView recyclerNews;
    Context applicationContext;


    public FetchNewsData( RecyclerView recyclerNews,Context applicationContext) {
        super(applicationContext);
        this.recyclerNews = recyclerNews;
        this.applicationContext = applicationContext;
    }


    ArrayList<newsModel> parsingJson(String jsonString) throws JSONException {
        arrayListnews = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray resultsArray = response.getJSONArray("results");
        for (int i = 0; i < SettingsActivity.NewsAppsPreferenceFragment.numItem; i++) {
            JSONObject jsonObjectinArray = resultsArray.getJSONObject(i);
            String type = jsonObjectinArray.getString("type");
            String webTitle = jsonObjectinArray.getString("webTitle");
            String sectionName = jsonObjectinArray.getString("sectionName");
            String webUrl = jsonObjectinArray.getString("webUrl");
            String webPublicationDate = jsonObjectinArray.getString("webPublicationDate");
            JSONArray tagsArray = jsonObjectinArray.getJSONArray("tags");
            String nameArth = " ";
            for (int j = 0; j < tagsArray.length(); j++) {

                JSONObject jsonObjectFromTaagArray = tagsArray.getJSONObject(j);
                nameArth = jsonObjectFromTaagArray.getString("webTitle");
            }
            
            arrayListnews.add(new newsModel(type, webTitle, sectionName, webPublicationDate, webUrl,nameArth));
        }

        return arrayListnews;
    }

    @Override
    public ArrayList<newsModel> loadInBackground() {
        url = NetworkUtils.URI_CONNECT + NetworkUtils.API_KEY;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        URL urL;


        ConnectivityManager cm =
                (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {

            try {
                urL = new URL(url);
                urlConnection = (HttpURLConnection) urL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                sb = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s = reader.readLine()) != null) {
                    sb.append(s + "\n");
                }
                if (sb.length() == 0) {
                    return null;
                }
                return parsingJson(sb.toString());
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                urlConnection.disconnect();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
        }
        return null;
    }

}



