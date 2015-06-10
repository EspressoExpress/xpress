package us.ridiculousbakery.espressoexpress.StorePicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.FakeDataSource;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.Adapters.StoreListAdapter;
import us.ridiculousbakery.espressoexpress.StorePicker.Fragments.ListFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.Fragments.MapFragment;
import us.ridiculousbakery.espressoexpress.StorePicker.Fragments.PagerFragment;


public class StorePickerActivity extends ActionBarActivity implements
        StoreListAdapter.MapTargetListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private MapFragment fgMapStoreFragment;
    private ListFragment fgListStoreFragment;
    private PagerFragment fgPagerStoreFragment;

    public GoogleMap getMap() {
        return map;
    }

    private GoogleMap map;
    private ArrayList<Store> stores;

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_picker);

        if (savedInstanceState == null) {
            activate_list_fragment();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        populate_list();
        Log.i("ZZZZZZZ", "onCreate");

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return false;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_store_pick_list) {
            activate_list_fragment();

        } else if (id == R.id.action_store_pick_map) {
            activate_map_and_pager_fragments();
        }

        return super.onOptionsItemSelected(item);
    }

    private MapFragment getMapStoreFragment() {
        if (fgMapStoreFragment != null) return fgMapStoreFragment;
        fgMapStoreFragment = new MapFragment();
        fgMapStoreFragment.getMapAsync(this);
        return fgMapStoreFragment;
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);

            // Now that map has loaded, let's get our location!
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this).build();

            // Attach long click listener to the map here
//            map.setOnMapLongClickListener(this);
//            map.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
        reconnect();

        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }
    private PagerFragment getPagerStoreFragment() {
        if (fgPagerStoreFragment != null) return fgPagerStoreFragment;
        return fgPagerStoreFragment = new PagerFragment();
    }

    private ListFragment getListStoreFragment() {
        if (fgListStoreFragment != null) return fgListStoreFragment;
        return fgListStoreFragment = new ListFragment();
    }

    private void activate_map_and_pager_fragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        getListStoreFragment().setMenuVisibility(false);
        getMapStoreFragment().setMenuVisibility(true);
        ft.replace(R.id.flContainer, getMapStoreFragment());
        ft.add(R.id.flContainer, getPagerStoreFragment());
        ft.commit();
    }

    private void activate_list_fragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        getListStoreFragment().setMenuVisibility(true);
        getMapStoreFragment().setMenuVisibility(false);
        ft.remove(getPagerStoreFragment());
        ft.replace(R.id.flContainer, getListStoreFragment());
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
    protected void onResume() {
        super.onResume();
        Log.i("ZZZZZZZ", "onResume");
        populate_list();
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
//        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
populate_list();
//        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (location != null) {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            Log.i("ZZZZZZ", latLng.toString());
//    populate_list();
//        } else {
//            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
//        }

    }

    private LatLng currentlatLng(){
        if(!mGoogleApiClient.isConnected()){
            Log.i("ZZZZZZZ", "No lat lon,  not yet connected");
            return null;
        }
        Log.i("ZZZZZZZ", "we are connected, looking for latlng");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location!=null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("ZZZZZZ", latLng.toString());
            return latLng;
        }else{
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void populate_list() {
        LatLng ll = currentlatLng();
        if (ll != null) {
            stores = FakeDataSource.nearby_stores(currentlatLng());
            Log.i("ZZZZZZZ", "store list: " + stores.size());
            getListStoreFragment().notifyNewData(stores);
            Bundle b = new Bundle();
            b.putSerializable("stores", stores);
            getPagerStoreFragment().setArguments(b);
        }
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
    public void onNewMapTarget(int index) {
//        fgPagerStoreFragment.setMapTarget(index);
        Log.i("ZZZZZZZ", "store list: "+ stores.size());
        final LatLng latLng = new LatLng(stores.get(index).getLat(),stores.get(index).getLon());
//        getMapStoreFragment().setInitialMapTarget(latLng);
        getMapStoreFragment().getMapAsync(
               new OnMapReadyCallback(){
                   @Override
                   public void onMapReady(GoogleMap googleMap) {
                       Log.i("ZZZZZZZ", "camera update: "+ latLng.toString());

                       CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                       map.animateCamera(cameraUpdate);
                   }
               }
        );
    }
    @Override
    public void onMapsRequired() {
        activate_map_and_pager_fragments();
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
