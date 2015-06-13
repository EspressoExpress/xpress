package us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.MenuActivity;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.PagerFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.StorePager.StorePagerAdapter;


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
    private ArrayList<MarkedStore> marked_stores;


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stores = (ArrayList<Store>)getIntent().getSerializableExtra("stores");
        marked_stores = MarkedStore.decorateList(stores);
        position =getIntent().getIntExtra("position", 0);

        setContentView(R.layout.activity_store_picker);
        final FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            getPagerFragment().setArguments(getIntent().getExtras());
            activate_map_and_pager_fragments();
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
    public void onNewMapTargetRequest(final int index, final boolean animate) {
        Log.i("ZZZZZZZ", "Entered onNewMapTargetRequest " + index);
        if(map==null){
            Log.i("ZZZZZZZ", "getting map async");
            getMapStoreFragment().getMapAsync(
                    new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if(map==null) {
                                map = googleMap;
                                populateMap();
                            }
                            animateToStore(animate, index);
                        }
                    }
            );
        }else {
            Log.i("ZZZZZZZ", "map exists");
            MapsInitializer.initialize(this);
            animateToStore(animate, index);

        }

    }

    private void populateMap(){
        for (MarkedStore store : marked_stores) {
            map.addMarker(
                    new MarkerOptions()
                            .position(store.getLatLng())
                            .title(store.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );
            for (MarkedOrder order : store.getMarkedOrders()) {
                map.addMarker(
                        new MarkerOptions()
                                .position(order.getLatLng())
                                .title(order.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                );
            }
        }



    }

    private void animateToStore(boolean animate, int index) {
        LatLng latLng = stores.get(index).getLatLng();
//        unHighlightStoreMarkers();
//        highlightStoreMarker();
//        dropOrderMarkers();
//        addOrderMarkers();

        moveCamera(animate, latLng);
    }

    private void moveCamera(boolean animate, LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        if(animate==true)    map.animateCamera(cameraUpdate);
        else map.moveCamera(cameraUpdate);
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
