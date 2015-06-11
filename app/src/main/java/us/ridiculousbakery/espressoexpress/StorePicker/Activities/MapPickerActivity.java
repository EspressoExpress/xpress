package us.ridiculousbakery.espressoexpress.StorePicker.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.MenuActivity;
import us.ridiculousbakery.espressoexpress.Model.FakeDataSource;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.Adapters.StorePagerAdapter;
import us.ridiculousbakery.espressoexpress.StorePicker.Fragments.MapFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.Fragments.PagerFragment;


public class MapPickerActivity extends ActionBarActivity implements
         PagerFragment.PagerListener,
        StorePagerAdapter.PagerItemListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private MapFragment fgMapStoreFragment;

    private PagerFragment fgPagerStoreFragment;

    private int position;

    public GoogleMap getMap() {
        return map;
    }

    private GoogleMap map;
    private ArrayList<Store> stores;


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stores = (ArrayList<Store>)getIntent().getSerializableExtra("stores");
        position =getIntent().getIntExtra("position", 0);

        setContentView(R.layout.activity_store_picker);
        final FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_store_pick_list) {
          finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private MapFragment getMapStoreFragment() {
        if (fgMapStoreFragment != null) return fgMapStoreFragment;
        fgMapStoreFragment = new MapFragment();
//        fgMapStoreFragment.getMapAsync(this);
        return fgMapStoreFragment;
    }

//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        if (map != null) {
//            Log.i("ZZZZZZ", "Map Fragment was loaded properly!");
//            map.setMyLocationEnabled(true);
//
//            // Attach long click listener to the map here
////            map.setOnMapLongClickListener(this);
////            map.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
//            reconnect();
//
//        } else {
//            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
//        }
//    }

    private PagerFragment getPagerFragment() {
        if (fgPagerStoreFragment != null) return fgPagerStoreFragment;
        return fgPagerStoreFragment = new PagerFragment();
    }

    private void activate_map_and_pager_fragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fgMapStoreFragment = new MapFragment();
        fgMapStoreFragment.setMenuVisibility(true);
        ft.replace(R.id.flContainer, fgMapStoreFragment);
        ft.add(R.id.flContainer, getPagerFragment());
        ft.commit();
    }

    public void reconnect() {
        if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ZZZZZZZ", "onStart");
        reconnect();

    }
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        reconnect();

                        break;
                }

        }
    }


    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }

            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("ZZZZZZ", "Connected");
        populate_list();
    }

    private LatLng currentlatLng() {
        if (!mGoogleApiClient.isConnected()) {
            Log.i("ZZZZZZZ", "No lat lon,  not yet connected");
            return null;
        }
        Log.i("ZZZZZZZ", "we are connected, looking for latlng");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("ZZZZZZ", latLng.toString());
            return latLng;
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void populate_list() {
        if (stores != null) return;
        LatLng ll = currentlatLng();
        if (ll == null) return;

        stores = FakeDataSource.nearby_stores(currentlatLng());
        Log.i("ZZZZZZZ", "store list: " + stores.size());

        Bundle b = new Bundle();
        b.putSerializable("stores", stores);
        b.putInt("position", position);

//        pb.setVisibility(View.GONE);
        getPagerFragment().setArguments(b);
        activate_map_and_pager_fragments();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

		/*
         * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void animateToNewMapTarget(int index, final boolean animate) {
        Log.i("ZZZZZZZ", "Entered animateToNewMapTarget " + index);
        fgPagerStoreFragment.setMapTarget(index);
        final LatLng latLng = new LatLng(stores.get(index).getLat(), stores.get(index).getLon());
        if(map==null){

            Log.i("ZZZZZZZ", "getting map async");
            getMapStoreFragment().getMapAsync(
                    new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if(map==null) {map=googleMap;}
                            Log.i("ZZZZZZZ", "camera update: " + latLng.toString());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                            if(animate==true)    googleMap.animateCamera(cameraUpdate);
                            else googleMap.moveCamera(cameraUpdate);
                        }
                    }
            );
        }else{
            MapsInitializer.initialize(this);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            if(animate==true)    map.animateCamera(cameraUpdate);
            else map.moveCamera(cameraUpdate);
            Log.i("ZZZZZZZ", "map exists");


        }

    }


    @Override
    public void gotoMenu(Store store){
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("menu", new StoreMenu(true));
        i.putExtra("store", store);
        startActivity(i);
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }



}
