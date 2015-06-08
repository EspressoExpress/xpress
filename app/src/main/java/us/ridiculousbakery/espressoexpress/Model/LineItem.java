package us.ridiculousbakery.espressoexpress.Model;

import java.io.Serializable;

/**
 * Created by bkuo on 6/3/15.
 */
public class LineItem implements Serializable{
    private Item  item;
    private ItemOption itemOption;

    public Item getItem() {
        return item;
    }

    public ItemOption getItemOption() {
        return itemOption;
    }

    public  LineItem(Item item) {
        this.item = item;
    }
}
