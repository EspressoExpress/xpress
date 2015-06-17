package us.ridiculousbakery.espressoexpress.NavDrawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Checkout.CCFormFragment;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.TutorialActivity;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/14/15.
 */
public class NavDrawerBaseActivity extends AppCompatActivity {
    private CharSequence mTitle;
    private ListView mDrawerList;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.nav_drawer_wrapper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerList = (ListView) findViewById(R.id.lvDrawer);
        mDrawerList.addHeaderView(getLayoutInflater().inflate(R.layout.nav_drawer_header, null));

        navDrawerItems = new ArrayList<>();
        navDrawerItems.add(new NavDrawerItem("Bank / CC", R.drawable.bank24));
        navDrawerItems.add(new NavDrawerItem("User Profile", R.drawable.ic_user_profile));
        navDrawerItems.add(new NavDrawerItem("Invite Friends", R.drawable.ic_action_share));
        navDrawerItems.add(new NavDrawerItem("Sign Out", R.drawable.ic_user_profile));

//        navDrawerItems.add(new NavDrawerItem("Edit Usual Order", R.drawable.philz_twit_logo));

        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1:
                        displayCCForm();
                        break;
                    case 2:
                        displayUserProfileForm();
                        break;
                    case 3:
                        displayShare();
                    case 4:
                        do_logout();
                    default:
                        break;
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void do_logout() {
        ParseUser.logOut();
        Intent i= new Intent(this, TutorialActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void displayShare() {

    }

    private void displayUserProfileForm() {
    }

    private void displayCCForm() {
        FragmentManager fm = getSupportFragmentManager();
        CCFormFragment ccFormFragment = CCFormFragment.newInstance();
        ccFormFragment.show(fm, "cc_form");
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewStub stub = (ViewStub) findViewById(R.id.vsNavContent);
        stub.setLayoutResource(layoutResID);
        stub.inflate();

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}