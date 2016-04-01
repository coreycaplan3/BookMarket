package com.github.coreycaplan3.bookmarket.functionality;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.R;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class TextBook implements Parcelable {

    private static final String TAG = TextBook.class.getSimpleName();
    private String title;
    private String author;
    private String isbn;
    private double price;
    @Nullable
    private String sellingId;
    @Nullable
    private String tradingId;
    @Condition
    int condition;
    private Bitmap picture;

    public static final int CONDITION_NEW = 0x000000;
    public static final int CONDITION_LIKE_NEW = 0x000001;
    public static final int CONDITION_GOOD = 0x000002;
    public static final int CONDITION_BAD = 0x000003;

    @IntDef({CONDITION_NEW, CONDITION_LIKE_NEW, CONDITION_GOOD, CONDITION_BAD})
    public @interface Condition {
    }

    @SuppressWarnings("unused")
    public static TextBook getDummyTextbook() {
        return new TextBook("Database System Concepts 6th Edition",
                "Abraham Silberschatz, Henry F. Korth, S. Sudarshan",
                "978–0–07–352332–3", 129.95, CONDITION_NEW, null);
    }

    public static String getCondition(@Condition int condition, Context context) {
        String returnValue = context.getString(R.string.string_new);
        switch (condition) {
            case CONDITION_NEW:
                break;
            case CONDITION_LIKE_NEW:
                returnValue = context.getString(R.string.like_new);
                break;
            case CONDITION_GOOD:
                returnValue = context.getString(R.string.good);
                break;
            case CONDITION_BAD:
                returnValue = context.getString(R.string.bad);
                break;
            default:
                Log.e(TAG, "getCondition: ", new IllegalArgumentException("Found: " + condition));
                break;
        }
        return returnValue;
    }

    /**
     * Constructor for selling a textbook
     */
    public TextBook(String title, String author, String isbn, double price, @Condition int condition,
                    Bitmap picture) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.condition = condition;
        this.picture = picture;
    }

    /**
     * Constructor for posting a textbook to your trades
     */
    public TextBook(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Constructor making book for notification.
     *
     * @param title          The title of the book.
     * @param author         The author of the book.
     * @param isbn           The ISBN of the book.
     * @param notificationId The id of the notification, either selling or trading depending on the
     *                       {@code isSelling} flag.
     * @param picture        The picture of the book.
     * @param isSelling      True if the notification is for a book being sold. False if it is for a
     *                       book being traded.
     */
    @SuppressWarnings("NullableProblems")
    public TextBook(String title, String author, String isbn, String notificationId, Bitmap picture,
                    boolean isSelling) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.picture = picture;
        if (isSelling) {
            sellingId = notificationId;
        } else {
            tradingId = notificationId;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public double getPrice() {
        return price;
    }

    @Nullable
    public String getSellingId() {
        return sellingId;
    }

    @Nullable
    public String getTradingId() {
        return tradingId;
    }

    @Condition
    public int getCondition() {
        return condition;
    }

    public Bitmap getPicture() {
        return picture;
    }

    @SuppressWarnings("WrongConstant")
    protected TextBook(Parcel in) {
        title = in.readString();
        author = in.readString();
        isbn = in.readString();
        price = in.readDouble();
        condition = in.readInt();
        picture = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        sellingId = in.readString();
        tradingId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(isbn);
        dest.writeDouble(price);
        dest.writeInt(condition);
        dest.writeValue(picture);
        dest.writeString(sellingId);
        dest.writeString(tradingId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TextBook> CREATOR = new Parcelable.Creator<TextBook>() {
        @Override
        public TextBook createFromParcel(Parcel in) {
            return new TextBook(in);
        }

        @Override
        public TextBook[] newArray(int size) {
            return new TextBook[size];
        }
    };

}
