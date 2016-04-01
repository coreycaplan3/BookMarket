package com.github.coreycaplan3.bookmarket.database;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import com.github.coreycaplan3.bookmarket.functionality.*;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: there is not a purpose for this class except Database bullshit
 */
public class DatabaseApi {

    private final String TAG = getClass().getSimpleName();
    private final String USER_AGENT = "Mozilla/5.0";

    private String baseURL = "http://book-mart.mybluemix.com/api.php/";
    private String command;
    private String[] args;
    private String token;

    /**
     * Log in the user using their email and password
     *
     * @param email    The user's registered email
     * @param password The user's password
     * @return token the token associated with the user's account
     * @throws Exception
     */
    private String connect(String email, String password) throws Exception {
        args = new String[2];
        command = "connect";
        args[0] = email;
        args[1] = password;
        token = "none";
        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Log.e(TAG, "connect: " + jObject.get("token").getAsString());
        return jObject.get("token").getAsString();
    }

    /**
     * Gets the user's info, based on the {@code userId} supplied.
     *
     * @param userID The user's unique user ID that is used to identify him/her in the Loccasion
     *               server.
     * @return a {@link GeneralUser} object with the id, name, and email of the user
     * @throws Exception
     */
    public GeneralUser getUserInfo(String userID) throws Exception {
        args = new String[1];
        command = "getUserInfo";
        args[0] = userID;
        token = "none";
        ArrayList<String> result = new ArrayList<>();

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        return new GeneralUser(jObject.get("u_id").getAsString(), jObject.get("name").getAsString(),
                jObject.get("email").getAsString(), jObject.get("school").getAsString());
    }

    /**
     * Logs the user into Loccasion using the necessary methods
     *
     * @param email    The email address for the given login attempt.
     * @param password The password for the given login attempt.
     * @return A User Profile object with name, email, login method, and user token
     * @throws Exception
     */
    public UserProfile logIn(String email, String password) throws Exception {
        String userToken = connect(email, password);
        String userId = getId(email);
        GeneralUser user = getUserInfo(email);
        return new UserProfile(user.getDisplayName(), email, userToken, userId,
                user.getUniversity());
    }

    /**
     * Returns the user ID associated with a given email address
     *
     * @param email The user's email address.
     * @return The user ID associated with the given email address.
     * @throws Exception
     */
    public String getId(String email) throws Exception {
        args = new String[1];
        command = "getID";
        args[0] = email;
        token = "none";
        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        return jObject.get("userid").toString();
    }

    /**
     * Checks if a given token is valid
     *
     * @param token The token that should be checked whether or not it is valid.
     * @return True if the token is valid or false if it is not.
     * @throws Exception
     */
    public boolean isValidToken(String token) throws Exception {
        args = new String[1];
        command = "validToken";
        args[0] = token;
        token = "none";
        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        return jObject.get("valid").getAsString().equals("true");
    }

    /**
     * Submits the entered textbook as a selling listing
     *
     * @param isbn      the isbn of the book
     * @param price     the price the user wishes to list the book at
     * @param condition the physical condition of the book
     * @param userToken token of the user who will be making the sell listing
     * @return the sell_id, in case you need it for something.
     * @throws Exception
     */
    public String postSellListing(String isbn, String price, String condition, String userToken) throws Exception {
        args = new String[3];
        args[0] = isbn;
        args[1] = price;
        args[2] = condition;
        command = "postSell";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("s_id").getAsString();
    }

    /**
     * Submits the entered textbook as a trade listing
     *
     * @param isbn      the isbn of the book you want to list for trade
     * @param condition the physical condition of the book you want to sell
     * @param userToken token of the user who will be making the sell listing
     * @return t_id the id of the trade in case you need
     * @throws Exception
     */
    public String postTradeListing(String isbn, String condition, String userToken) throws Exception {
        args = new String[2];
        args[0] = isbn;
        args[1] = condition;
        command = "postTrade";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("t_id").getAsString();
    }

    /**
     * Registers a new user to the app
     *
     * @param full_name the full name of the user
     * @param email     the email address for the user
     * @param password  the password for the user
     * @param school    the school the user wishes to be registered to
     * @return the u_id of the newly created user, in case you need it
     * @throws Exception
     */
    public String makeUser(String full_name, String email, String password, String school) throws Exception {
        args = new String[4];
        args[0] = full_name;
        args[1] = email;
        args[2] = password;
        args[3] = school;
        command = "makeUser";
        token = "none";

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("u_id").getAsString();
    }

    /**
     * Looks up the requested title, author, or isbn in the Books table
     *
     * @param lookupString
     * @return ArrayList of TextBooks
     * @throws Exception
     */
    public ArrayList<TextBook> textbookLookup(String lookupString) throws Exception {
        args = new String[1];
        args[0] = lookupString;
        command = "textbookLookup";
        token = "none";

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Searches in Selling and Trading for the book-isbn requested
     *
     * @param isbn   the isbn of the book you want to lookup
     * @param school (optional) if no school is selected, give an empty String (""), and we will return every book of that isbn listed
     * @return ArrayList of TextBooks (restricted to the optional location)
     * @throws Exception
     */
    public ArrayList<TextBook> searchSellTrade(String isbn, String school) throws Exception {
        args = new String[2];
        args[0] = isbn;
        args[1] = school;
        command = "searchSellTrade";
        token = "none";

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);

    }

    /**
     * Gets the trade listings that I have listed
     *
     * @param userToken the token of the current user
     * @return an ArrayList of Textbooks that I have listed for trade
     * @throws Exception
     */
    public ArrayList<TextBook> getMyTradeListings(String userToken) throws Exception {
        args = new String[1];
        args[0] = "";
        token = userToken;
        command = "getTradeListings";

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Gets the sales listings that I have listed
     *
     * @param userToken the token of the current user
     * @return an ArrayList of Textbooks that I have listed for sales
     * @throws Exception
     */
    public ArrayList<TextBook> getMySalesListings(String userToken) throws Exception {
        args = new String[1];
        args[0] = "";
        token = userToken;
        command = "getSalesListings";

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Makes a Sale. Triggered when the current user has ACCEPTED an "interest"
     *
     * @param userToken the id of the user
     * @param sellingID the selling_id of the listing
     *
    */
    public String makeSale(String userToken, String sellingID)
    {
        return "true";
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
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("data");
        JsonArray jarray = jobject.getAsJsonArray("translations");
        jobject = jarray.get(0).getAsJsonObject();
        String result = jobject.get("translatedText").toString();
        return result;
    }

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

}
