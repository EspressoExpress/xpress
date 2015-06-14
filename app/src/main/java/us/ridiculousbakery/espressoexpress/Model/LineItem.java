package us.ridiculousbakery.espressoexpress.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bkuo on 6/3/15.
 */
public class LineItem implements Serializable{


    private static final long serialVersionUID = -7700720797863366950L;
    private Item  item;
    private ArrayList<String> chosenOptions;
    //private ItemOption itemOption;


    //public ItemOption getItemOption() {
//        return itemOption;
//    }

    //================================================================================
    // Constructors
    //================================================================================

    public LineItem(Item item, ArrayList<String> chosenOptions) {
        this.item = item;
        this.chosenOptions = chosenOptions;
    }

//    public  LineItem(Item item, ItemOption itemOption) {
//        this.item = item;
//        this.itemOption = itemOption;
//    }

    //================================================================================
    // Getters
    //================================================================================

    public Item getItem() {
        return item;
    }

    public ArrayList<String> getChosenOptions() {
        return chosenOptions;
    }
}
