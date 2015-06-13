package us.ridiculousbakery.espressoexpress.Model;

import java.io.Serializable;

/**
 * Created by bkuo on 6/3/15.
 */
public class User implements Serializable {
    public User(String user){
        this.name = user;
    }

    public String getName() {
        return name;
    }

    private String name;

    private Double lon;
    private Double lat;


//Payment info fields
    private String pan;
    private String cvv;
    private String zip;
    private String exp;
}
