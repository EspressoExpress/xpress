package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.LoginActivity;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.StorePickerActivity;

public class EntryActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // launch a different activity
        Intent launchIntent = new Intent();
        Class<?> launchActivity;
        try {
            String className = getScreenClassName();
            launchActivity = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            launchActivity = LoginActivity.class;
        }
        launchIntent.setClass(getApplicationContext(), launchActivity);
        startActivity(launchIntent);

        finish();
    }

    /** return Class name of Activity to show **/
    private String getScreenClassName()
    {
        // NOTE - Place logic here to determine which screen to show next
        // Default is used in this demo code
        // Check for current user
        ParseUser.logOut();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            String activity = LoginActivity.class.getName();
            return activity;
        } else {
            String activity = StorePickerActivity.class.getName();
            return activity;
        }
    }

}
