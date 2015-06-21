package us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Checkable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.MenuActivity;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.NavDrawer.NavDrawerBaseActivity;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.MapsPerspective.MapPickerActivity;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;


public class ListPickerActivity extends NavDrawerBaseActivity implements
        StoreElementListener,
        ListFragment.ListListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private ListFragment fgListStoreFragment;

    private List<Store> stores;
    private boolean list_fragment_activated = false;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store_picker);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_store_pick_map) {
            activate_map_and_pager_fragments(null);
        }

        return true;
    }

    private ListFragment getListFragment() {
        if (fgListStoreFragment != null) return fgListStoreFragment;
        return fgListStoreFragment = new ListFragment();
    }

    private void activate_map_and_pager_fragments(String storeId) {
        Log.i("ZZZZZZZ", "Starting Map Activity "+storeId);
        Intent i = new Intent(this, MapPickerActivity.class);
        if( storeId!=null) i.putExtra("storeId", storeId);
        i.putExtra("currentLatLng", currentlatLng());
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void activate_list_fragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, getListFragment());
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
        LatLng ll = currentlatLng();
        if (ll == null) return;
        if (list_fragment_activated) return;
        Bundle b = new Bundle();
        b.putParcelable("currentLatLng", ll);
        getListFragment().setArguments(b);
        activate_list_fragment();
        list_fragment_activated = true;

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
    public void onNewMapTarget(String storeId) {
        activate_map_and_pager_fragments(storeId);
    }

    public void onStoreElementClicked(Store store) {
        Checkable s = (Checkable) findViewById(R.id.xToggle);
        if (s.isChecked()) {
            activate_map_and_pager_fragments(store.getObjectId());
        } else {
            Intent i = new Intent(this, MenuActivity.class);
            i.putExtra("storeId", store.getObjectId());
            startActivity(i);

        }
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
