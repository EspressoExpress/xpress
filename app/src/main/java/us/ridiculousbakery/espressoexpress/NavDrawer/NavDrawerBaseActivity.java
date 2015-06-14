package us.ridiculousbakery.espressoexpress.NavDrawer;

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

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Checkout.CCFormFragment;
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
        navDrawerItems = new ArrayList<>();
        mDrawerList = (ListView) findViewById(R.id.lvDrawer);
        navDrawerItems = new ArrayList<>();
        navDrawerItems.add(new NavDrawerItem("Edit Payment Information", R.drawable.philz_twit_logo));
        navDrawerItems.add(new NavDrawerItem("Edit User Profile", R.drawable.philz_twit_logo));
//        navDrawerItems.add(new NavDrawerItem("Edit Usual Order", R.drawable.philz_twit_logo));

        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        displayCCForm();
                        break;
                    default:
                        break;
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

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