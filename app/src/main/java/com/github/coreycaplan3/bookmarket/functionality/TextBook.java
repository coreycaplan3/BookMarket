package com.github.coreycaplan3.bookmarket.functionality;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class TextBook implements Parcelable {

    private String title;
    private String author;
    private String isbn;
    private double price;
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

    public static TextBook getDummyTextbook() {
        return new TextBook("Database System Concepts 6th Edition",
                "Abraham Silberschatz, Henry F. Korth, S. Sudarshan",
                "978–0–07–352332–3", 129.95, CONDITION_NEW, null);
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

    public TextBook(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
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
