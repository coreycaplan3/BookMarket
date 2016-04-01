package com.github.coreycaplan3.bookmarket.database;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class DatabaseApi {

    private final String TAG = getClass().getSimpleName();
    private final String USER_AGENT = "Mozilla/5.0";

    private String baseURL = "https://meet-up-1097.appspot.com";
    private String command;
    private String[] args;
    private String token;

    private String assembleURL(String command, String[] args, String token) {
        return (this.baseURL + "/?command=" + command + "&args=" + formatArgs(args) + "&token=" + token);
    }

    private String formatArgs(String[] args) {
        String formattedArgs = "";
        for (String arg : args) {
            formattedArgs = formattedArgs + arg + ";";
        }
        return formattedArgs.substring(0, formattedArgs.length() - 1);
    }

    //Sends get request, returns response
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

    private String sendPost(String command, String[] args, String token) throws Exception {
        URL url = new URL(baseURL);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "command=" + command + "&args=" + formatArgs(args) + "&token=" + token; //parameters

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

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

    public String parse(String jsonLine) {
        JsonElement jElement = new JsonParser().parse(jsonLine);
        JsonObject jObject = jElement.getAsJsonObject();
        jObject = jObject.getAsJsonObject("data");
        JsonArray jArray = jObject.getAsJsonArray("translations");
        jObject = jArray.get(0).getAsJsonObject();
        return jObject.get("translatedText").toString();
    }


}
