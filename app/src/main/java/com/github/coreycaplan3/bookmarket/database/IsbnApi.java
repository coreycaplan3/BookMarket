package com.github.coreycaplan3.bookmarket.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.github.coreycaplan3.bookmarket.application.BookApplication;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Corey on 4/1/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class IsbnApi implements Response.ErrorListener {

    private static final String TAG = IsbnApi.class.getSimpleName();

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    private static final String USER_AGENT = "Mozilla/5.0";

    public IsbnApi() {
    }

    public TextBook getTextBookFromIsbn(String isbn) throws Exception {
//        JsonElement jElement = new JsonParser().parse(sendGet());
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL + isbn, null,
                requestFuture, requestFuture);
        BookApplication.getInstance().getRequestQueue().add(request);
        JSONObject jObject = requestFuture.get(10, TimeUnit.SECONDS);
        JSONArray jsonArray = jObject.getJSONArray("items");
        jObject = (JSONObject) jsonArray.get(0);
        JSONObject volumeInfo = jObject.getJSONObject("volumeInfo");
        String title = volumeInfo.get("title").toString();
        String authors = "";
        JSONArray authorsArray = volumeInfo.getJSONArray("authors");
        for (int i = 0; i < authorsArray.length(); i++) {
            if (i == authorsArray.length() - 1) {
                authors += authorsArray.get(i).toString();
            } else {
                authors += authorsArray.get(i).toString() + " ";
            }
        }
        String imageUrl = volumeInfo.getJSONObject("imageLinks")
                .getString("smallThumbnail");
        Log.e(TAG, "getTextBookFromIsbn: " + title + " " + authors + " " + imageUrl);
        return new TextBook(title, authors, isbn, imageUrl, false);
    }

    private String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode(); //gets response code

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result
        return response.toString();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
