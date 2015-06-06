package us.ridiculousbakery.espressoexpress.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by bkuo on 6/3/15.
 */
public class Store implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    private String logo;
    private Double lon;
    private Double lat;


    Store(String name) {
        this.name = name;
    }

    Store(String name, LatLng latlng, String logo) {
        this.name = name;
        this.lon = latlng.longitude;
        this.lat = latlng.latitude;
        this.logo=logo;
    }
//    public ArrayList<Item>  getMenu(){
//
//        return {fdshjilfjlmf.,sd,fjdskl,fsd,a}
//    }

}
