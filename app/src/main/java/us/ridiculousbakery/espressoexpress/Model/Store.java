package us.ridiculousbakery.espressoexpress.Model;

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

    public Float getLon() {
        return lon;
    }

    public Float getLat() {
        return lat;
    }

    private String logo;
    private Float lon;
    private Float lat;

//    public ArrayList<Item>  getMenu(){
//
//        return {fdshjilfjlmf.,sd,fjdskl,fsd,a}
//    }

}
