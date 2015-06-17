package us.ridiculousbakery.espressoexpress.Model;

import java.io.Serializable;

/**
 * Created by teddywyly on 6/14/15.
 */
public class SelectedOption implements Serializable {
    private String name;
    private String category;

    public SelectedOption(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}
