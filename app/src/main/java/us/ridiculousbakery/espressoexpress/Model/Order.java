package us.ridiculousbakery.espressoexpress.Model;

import java.util.ArrayList;

/**
 * Created by bkuo on 6/3/15.
 */
public class Order {

    private ArrayList<LineItem> lineItems;


    private Double lon;
    private Double lat;
    private long valid_until;
    private long created_at;
    private Store store;
    private User user;

}