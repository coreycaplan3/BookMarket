package com.github.coreycaplan3.bookmarket.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.coreycaplan3.bookmarket.application.BookApplication;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Corey on 4/1/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class IsbnApi {

    private static final String TAG = IsbnApi.class.getSimpleName();

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    private static final String USER_AGENT = "Mozilla/5.0";

    public IsbnApi() {
    }

    public TextBook getTextBookFromIsbn(String isbn) throws Exception {
        JsonElement jElement = new JsonParser().parse(sendGet(BASE_URL + isbn));
        JsonObject jObject = jElement.getAsJsonObject();
        JsonArray jsonArray = jObject.getAsJsonArray("items");
        jObject = (JsonObject) jsonArray.get(0);
        JsonObject volumeInfo = jObject.getAsJsonObject("volumeInfo");
        String title = volumeInfo.get("title").getAsString();
        String authors = "";
        JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
        for (int i = 0; i < authorsArray.size(); i++) {
            if (i == authorsArray.size() - 1) {
                authors += authorsArray.get(i).getAsString();
            } else {
                authors += authorsArray.get(i).getAsString() + " ";
            }
        }
        String imageUrl = volumeInfo.getAsJsonObject("imageLinks")
                .getAsJsonPrimitive("smallThumbnail").getAsString();
//        Log.e(TAG, "getTextBookFromIsbn: " + title + " " + authors + " " + imageUrl);
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

}
