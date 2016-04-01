package com.github.coreycaplan3.bookmarket.database;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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

    private static final String BASE_URL = "https://book-mart.mybluemix.net/api.php/";
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
        Log.e(TAG, "connect: " + assembleURL(command, args, token));
        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Log.e(TAG, "connect: " + jObject.get("token").getAsString());

        return jObject.get("token").getAsString();
    }

    /**
     * Gets the user's info, based on the {@code userId} supplied.
     *
     * @param userID The user's unique user ID that is used to identify him/her.
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
        Log.e(TAG, "getUserInfo: " + assembleURL(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();
        return new GeneralUser(jObject.get("u_id").getAsString(), jObject.get("full_name").getAsString(),
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
        GeneralUser user = getUserInfo(userId);
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

        return jObject.get("u_id").toString();
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
     * @param title     the title of the book
     * @param author    the author of the book
     * @param image     a photo of the book
     * @return the sell_id, in case you need it for something.
     * @throws Exception
     */
    public String postSellListing(String isbn, String price, String condition, String title, String author, Bitmap image, String userToken) throws Exception {
        args = new String[6];
        args[0] = isbn;
        args[1] = price;
        args[2] = condition;
        args[3] = title;
        args[4] = author;
        args[5] = image.toString();
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
     * @param title     the title of the book
     * @param author    the author of the book
     * @param image     a photo of the book
     * @return t_id the id of the trade in case you need
     * @throws Exception
     */
    public String postTradeListing(String isbn, String condition, String title, String author, Bitmap image, String userToken) throws Exception {
        args = new String[5];
        args[0] = isbn;
        args[1] = condition;
        args[2] = title;
        args[3] = author;
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
    public String register(String full_name, String email, String password, String school) throws Exception {
        args = new String[4];
        args[0] = full_name;
        args[1] = password;
        args[2] = email;
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
     * Searches in Selling for the book you want to lookup
     *
     * @param isbn   the isbn of the book you want to lookup
     * @param school (optional) if no school is selected, give an empty String (""), and we will return every book of that isbn listed
     * @return ArrayList of TextBooks (restricted to the optional location)
     * @throws Exception
     */
    public ArrayList<TextBook> searchSell(String isbn, String school) throws Exception {
        args = new String[2];
        args[0] = isbn;
        args[1] = school;
        command = "searchSell";
        token = "none";

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Searches in Trading for the book you want to lookup
     *
     * @param isbn   the isbn of the book you want to lookup
     * @param school (optional) if no school is selected, give an empty String (""), and we will return every book of that isbn listed
     * @return ArrayList of Textbooks (restricted to the optional location)
     * @throws Exception
     */
    public ArrayList<TextBook> searchTrade(String isbn, String school) throws Exception {
        args = new String[2];
        args[0] = isbn;
        args[1] = school;
        command = "searchTrade";
        token = "none";

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Searches for all TextBooks for sale at a desired school
     *
     * @param school the school at which we wish to search for textbooks
     * @return ArrayList of TextBooks for sale
     * @throws Exception
     */
    public ArrayList<TextBook> getSellingForSchool(String school) throws Exception {
        args = new String[1];
        args[0] = school;
        command = "searchSchoolSales";
        token = "none";

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Searches for all TextBooks for trade at the desired school
     *
     * @param school the school at which we wish to search for textbooks
     * @return ArrayList of TextBooks for trade
     * @throws Exception
     */
    public ArrayList<TextBook> getTradingForSchool(String school) throws Exception {
        args = new String[1];
        args[0] = school;
        command = "searchSchoolTrades";
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
     * Makes a Sale. Triggered when the current user has ACCEPTED an "sale interest"
     *
     * @param sellingID the selling_id of the listing
     * @param userToken the token of the user
     * @return the id of the buyer
     */
    public String makeSale(String sellingID, String userToken) throws Exception {
        args = new String[1];
        args[0] = sellingID;
        command = "makeSale";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("buyer_id").getAsString();
    }

    /**
     * Makes a Trade.  Triggered when the current user has ACCEPTED a "trade interest"
     *
     * @param tradingID the trading_id of the listing
     * @param userToken the token of the user
     * @return the id of the other trader
     * @throws Exception
     */
    public String makeTrade(String tradingID, String userToken) throws Exception {
        args = new String[1];
        args[0] = tradingID;
        command = "makeTrade";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("trader_id").getAsString();
    }

    /**
     * Sets the isbn of the book given as the book you wish to trade for
     *
     * @param t_id      the trade id
     * @param isbn      the isbn of the book you want to trade for
     * @param userToken the token of the user
     * @return no clue
     */
    public String tradeFor(String t_id, String isbn, String userToken) throws Exception {
        args = new String[2];
        args[0] = t_id;
        args[1] = isbn;
        command = "tradeFor";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("t_id").getAsString();
    }

    /**
     * Gets all the textbooks im willing to trade for a certain trading_id
     *
     * @param t_id      the trading id I want to lookup
     * @param userToken the token of the user
     * @return
     * @throws Exception
     */
    public ArrayList<TextBook> getTradingForList(String t_id, String userToken) throws Exception {
        args = new String[1];
        args[0] = t_id;
        command = "getTradingForList";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }


//    public ArrayList<GeneralUser> getAllPotentialTraders(String userToken) throws Exception{
//        args = new String[1];
//        args[0] = "";
//        command = "getAllPotentialTraders";
//        token = userToken;
//        ArrayList<GeneralUser> allTraders = new ArrayList<GeneralUser>();
//        ArrayList<TextBook> tradeBooks = getMyTradeListings(userToken);
//        for (TextBook book : tradeBooks) {
//            allTraders.add(getSpecificPotentialTrader(book.get));
//        }
//        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
//        JsonObject jObject = jElement.getAsJsonObject();
//        Type listType = new TypeToken<List<TextBook>>() {
//        }.getType();
//
//        return new Gson().fromJson(jObject.get("books"), listType);
//    }

    /**
     * Returns all interested traders for a specific trade_id
     *
     * @param t_id      the trade_id
     * @param userToken the token of the user
     * @return ArrayList of GeneralUser of all the users interested in trading
     * @throws Exception
     */
    public ArrayList<GeneralUser> getSpecificPotentialTrader(String t_id, String userToken) throws Exception {
        args = new String[1];
        args[0] = t_id;
        command = "getSpecificTrader";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<GeneralUser>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("traders"), listType);

    }

    /**
     * Returns all interested buyers for a specific sell_id
     *
     * @param s_id      the sell_id
     * @param userToken the token of the user
     * @return ArrayList of GeneralUser for all the users interested in buying
     * @throws Exception
     */
    public ArrayList<GeneralUser> getSpecificPotentialBuyer(String s_id, String userToken) throws Exception {
        args = new String[1];
        args[0] = s_id;
        command = "getSpecificBuyer";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<GeneralUser>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("traders"), listType);
    }

    /**
     * Gets a list of TextBooks that I have expressed interest in buying
     *
     * @param userToken the token of the user
     * @return ArrayList of TextBooks I expressed interest in
     * @throws Exception
     */
    public ArrayList<TextBook> getMyIntrestedSales(String userToken) throws Exception {
        args = new String[1];
        args[0] = "";
        command = "getMyInterestedSales";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Gets a list of TextBooks that I have expressed interest in trading
     *
     * @param userToken the token of the user
     * @return ArrayList of TextBooks I expressed interest in
     * @throws Exception
     */
    public ArrayList<TextBook> getMyIntrestedTrades(String userToken) throws Exception {
        args = new String[1];
        args[0] = "";
        command = "getMyInterestedTrades";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendGet(assembleURL(command, args, token)));
        JsonObject jObject = jElement.getAsJsonObject();
        Type listType = new TypeToken<List<TextBook>>() {
        }.getType();

        return new Gson().fromJson(jObject.get("books"), listType);
    }

    /**
     * Expresses interest that the user has for buying a listed sale
     *
     * @param s_id      the sell_id of the book the user is interested in
     * @param userToken the token of the user
     * @return the sell_id of the listed sale
     * @throws Exception
     */
    public String expressBuyerInterest(String s_id, String userToken) throws Exception {
        args = new String[1];
        args[0] = s_id;
        command = "expressBuyerInterest";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("s_id").getAsString();
    }

    /**
     * Expresses interest that the user has for trading a listed trade
     *
     * @param t_id      the trade_id of the book the user is interested in
     * @param userToken the token of the user
     * @return the trade_id of the listed trade
     * @throws Exception
     */
    public String expressTradingInterest(String t_id, String userToken) throws Exception {
        args = new String[1];
        args[0] = t_id;
        command = "expressTraderInterest";
        token = userToken;

        JsonElement jElement = new JsonParser().parse(sendPost(command, args, token));
        JsonObject jObject = jElement.getAsJsonObject();

        return jObject.get("t_id").getAsString();
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
        URL url = new URL(BASE_URL);
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
        return jobject.get("translatedText").toString();
    }

    private String assembleURL(String command, String[] args, String token) {
        return (BASE_URL + "?command=" + command + "&args=" + formatArgs(args) + "&token=" + token);
    }

    private String formatArgs(String[] args) {
        String formattedArgs = "";
        for (String arg : args) {
            formattedArgs = formattedArgs + arg + ";";
        }
        return formattedArgs.substring(0, formattedArgs.length() - 1);
    }

}
