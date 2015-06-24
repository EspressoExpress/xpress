package us.ridiculousbakery.espressoexpress.Model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teddywyly on 6/23/15.
 */
public class TempStore implements Serializable {

    private String name;
    private Integer logo;
    private Double lon;
    private Double lat;
    private Integer background;
    private String storeID;

    public TempStore(Store store) {
        this.name = store.getName();
        this.background = store.getBackground();
        this.lon = store.getLon();
        this.lat = store.getLat();
        this.logo = store.getLogo();
        this.storeID = store.getObjectId();
    }

    public String getName() {
        return name;
    }

    public Integer getLogo() {
        return logo;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Integer getBackground() {
        return background;
    }
}
