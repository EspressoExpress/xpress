package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.parse.ParseUser;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.TutorialActivity;
import us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective.ListPickerActivity;

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
            launchActivity = TutorialActivity.class;
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

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            String activity = TutorialActivity.class.getName();
            return activity;
        } else {
            String activity = ListPickerActivity.class.getName();
            return activity;
        }
    }

}
