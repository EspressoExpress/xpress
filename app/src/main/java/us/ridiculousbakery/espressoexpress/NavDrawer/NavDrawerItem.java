package us.ridiculousbakery.espressoexpress.NavDrawer;

/**
 * Created by bkuo on 5/28/15.
 */

public class NavDrawerItem {
    private String title;
    private int icon;

    public NavDrawerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return this.title;
    }

    public int getIcon() {
        return this.icon;
    }
}
