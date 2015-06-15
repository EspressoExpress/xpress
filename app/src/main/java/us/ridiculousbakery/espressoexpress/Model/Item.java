package us.ridiculousbakery.espressoexpress.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by bkuo on 6/3/15.
 */
public class Item implements Serializable {


    private static final long serialVersionUID = 4455604316807152635L;
    private String name;
    private String imageURL;
    private TreeMap <String, ArrayList<String>> options;

    //================================================================================
    // Constructors
    //================================================================================

    public Item(String name) {
        this.name = name;
    }
    public Item(String name, TreeMap options) {
        this.name = name;
        this.options = options;
    }

    //================================================================================
    // Getters
    //================================================================================

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public TreeMap<String, ArrayList<String>> getOptions() {
        return options;
    }
    //================================================================================
    // Parcelable
    //================================================================================

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
//    }
//
//    public static final Parcelable.Creator<Item> CREATOR
//            = new Parcelable.Creator<Item>() {
//        @Override
//        public Item createFromParcel(Parcel in) {
//            return new Item(in);
//        }
//
//        @Override
//        public Item[] newArray(int size) {
//            return new Item[size];
//        }
//    };
//
//    private Item(Parcel in) {
//        name = in.readString();
//    }

}
