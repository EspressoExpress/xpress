package us.ridiculousbakery.espressoexpress.Model;

/**
 * Created by teddywyly on 6/14/15.
 */
public class SelectedOption {
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
