package us.ridiculousbakery.espressoexpress.NavDrawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import us.ridiculousbakery.espressoexpress.Checkout.CCFormFragment;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.TutorialActivity;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.ProfileImageHelper;
import us.ridiculousbakery.espressoexpress.Model.XpressUser;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.LocationUtility;

/**
 * Created by bkuo on 6/14/15.
 */
public class NavDrawerLocationBaseActivity extends AppCompatActivity implements LocationUtility.OnConnectedDelegate {
    final public static String SHOW_FAB="show fab";
    final public static String SHOW_LIST_SWITCH="listModeSwitch";
    final public static String SHOW_PAGER="showPager";
    final public static String MAPMODE="mapMode";
    final public static String ANIM_MRK="animateMarkers";
    final public static String ALT_ACCENT="Alternative Accent Color";
    public static final String SCRIPT_MODE = "Script Font";
    private CharSequence mTitle;
    private ListView mDrawerList;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    protected LocationUtility locationUtility;
    private boolean shouldRestart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences p = getSharedPreferences("Xpress",MODE_PRIVATE);
        if(p.getBoolean(NavDrawerLocationBaseActivity.SCRIPT_MODE, false)){
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("Satisfy-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
            );
        }else{
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("open-sans/OpenSans-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
            );
        }
        if(p.getBoolean(ALT_ACCENT, false)) setTheme(R.style.XpressThemeB);
        else setTheme(R.style.XpressTheme);

        super.setContentView(R.layout.nav_drawer_wrapper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerList = (ListView) findViewById(R.id.lvDrawer);
        View vgHeader = getLayoutInflater().inflate(R.layout.nav_drawer_header, null);
        ImageView rv =(ImageView) vgHeader.findViewById(R.id.ivLogo);
        TextView tvName = (TextView) vgHeader.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) vgHeader.findViewById(R.id.tvEmail);

        Log.i("ZZZZZZZZZ", "current gravatar: _" + ParseUser.getCurrentUser().getString("gravatar_url") + "_");
        Picasso.with(this).load(XpressUser.getGravatarUrl(ParseUser.getCurrentUser())).fit().transform(ProfileImageHelper.circleTransformation(100)).into(rv);
        tvName.setText(ParseUser.getCurrentUser().getString("displayName"));
        tvEmail.setText(ParseUser.getCurrentUser().getString("email") );

        mDrawerList.addHeaderView(vgHeader);

        navDrawerItems = new ArrayList<>();
        navDrawerItems.add(new NavDrawerItem("Bank / CC", R.drawable.bank24));
        navDrawerItems.add(new NavDrawerItem("User Profile", R.drawable.ic_user_profile));
        navDrawerItems.add(new NavDrawerItem("Invite Friends", R.drawable.ic_action_share));
        navDrawerItems.add(new NavDrawerItem("Settings", R.drawable.ic_settings));

        navDrawerItems.add(new NavDrawerItem("Sign Out", R.drawable.ic_user_profile));

//        navDrawerItems.add(new NavDrawerItem("Edit Usual Order", R.drawable.philz_twit_logo));

        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        displayCCForm();
                        break;
                    case 2:
                        displayUserProfileForm();
                        break;
                    case 3:
                        displayShare();
                        break;
                    case 4:
                        displaySettings();
                        break;
                    case 5:
                        do_logout();
                        break;
                    default:
                        break;
                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        locationUtility = new LocationUtility(this, this);

    }

    private void displaySettings() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        LinearLayoutCompat b = new LinearLayoutCompat(this);
        b.setOrientation(LinearLayoutCompat.VERTICAL);
        b.setPadding(10, 10, 10, 10);
//        b.addView(pref(SHOW_FAB));
//        b.addView(pref(SHOW_LIST_SWITCH));
//        b.addView(pref(SHOW_PAGER));
//        b.addView(pref(MAPMODE));
        b.addView(pref(ALT_ACCENT));
        b.addView(pref(SCRIPT_MODE));
        alertDialogBuilder.setView(b);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i("ZZZZZZZ", "DISMISSED");
                recreate();
            }
        });

        alertDialog.show();


    }
    private SwitchCompat pref(final String label){return this.pref(label, false); }

    private SwitchCompat pref(final String label, boolean def){
        SwitchCompat d = new SwitchCompat(this);
        final SharedPreferences p = getSharedPreferences("Xpress", MODE_PRIVATE);

        d.setText(label);
        d.setPadding(5, 5, 5, 15);
        d.setChecked(p.getBoolean(label, def));
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.edit().putBoolean(label, ((SwitchCompat) v).isChecked()).apply();
                shouldRestart = true;
            }
        });
        return d;
    }

    private void do_logout() {
        ParseUser.logOut();
        Intent i= new Intent(this, TutorialActivity.class);
        startActivity(i);
        finish();
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
    @Override
    protected void onStart() {
        super.onStart();
        locationUtility.reconnect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        locationUtility.disconnect_if_present();
        super.onStop();
    }
    /*
      * Handle results returned to the FragmentActivity by Google Play services
      */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case LocationUtility.CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        locationUtility.reconnect();
                        break;
                }

        }
    }
    protected LatLng currentlatLng() {
        if (!locationUtility.isConnected()) {
            Log.i("ZZZZZZZ", "No lat lon,  not yet connected");
            return null;
        }
        Log.i("ZZZZZZZ", "we are connected, looking for latlng");

        Location location = LocationServices.FusedLocationApi.getLastLocation(locationUtility.client());
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("ZZZZZZ", latLng.toString());
            return latLng;
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    @Override
    public void onConnected() {

    }
}