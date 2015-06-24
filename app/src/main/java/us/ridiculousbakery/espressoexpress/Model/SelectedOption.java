package us.ridiculousbakery.espressoexpress.Model;

import java.io.Serializable;
import java.util.ArrayList;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public static String toString(SelectedOption o) {
        return o.getCategory() + ": " + o.getName();
    }

    public static String ArrayToString(ArrayList<SelectedOption> l) {
        String optionStr = "";
        for (int i=0; i < l.size(); i++) {
            optionStr += toString(l.get(i));
            if (i < l.size()-1) {
                optionStr += ", ";
            }
        }
        return  optionStr;
    }
}
