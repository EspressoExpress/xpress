package us.ridiculousbakery.espressoexpress.Model;

/**
 * Created by bkuo on 6/3/15.
 */
public class User {
    public User(String user){
        this.name = user;
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
