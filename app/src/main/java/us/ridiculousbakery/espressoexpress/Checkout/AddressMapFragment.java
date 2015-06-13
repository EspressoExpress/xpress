package us.ridiculousbakery.espressoexpress.Checkout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/9/2015.
 */
public class AddressMapFragment extends DialogFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnCameraChangeListener
{

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    //private LocationRequest mLocationRequest;
    private Marker locationMarker;
    private Button btSelectAddress;
    private ImageView ivMarker;

    public AddressMapFragment() {

    }

    public static AddressMapFragment newInstance() {
        AddressMapFragment addressMapFragment = new AddressMapFragment();
        addressMapFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return addressMapFragment;
    }

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        try {
            v = inflater.inflate(R.layout.fragment_address_map, container, false);
        } catch (InflateException e) {

        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.addressMapView, mapFragment).commit();
        btSelectAddress = (Button) v.findViewById(R.id.btSelectAddress);
        btSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ivMarker = (ImageView) v.findViewById(R.id.ivMarker);
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.sbux_twit_logo);
        Bitmap marker = Bitmap.createScaledBitmap(temp, 16, 16, true);
        ivMarker.setImageBitmap(marker);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                loadMap(map);
                //map.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
            }
        });
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);
            map.setOnCameraChangeListener(this);
            // Now that map has loaded, let's get our location!
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            Toast.makeText(getActivity(), "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            map.animateCamera(cameraUpdate);
            updateMarker(latLng);
            //startLocationUpdates();
        } else {
            Toast.makeText(getActivity(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateMarker(LatLng latLng){
        if (locationMarker == null) {
            BitmapDescriptor defaultMarkerIcon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            locationMarker = map.addMarker((new MarkerOptions()).position(latLng).icon(defaultMarkerIcon));
        }
        locationMarker.setPosition(latLng);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(getActivity(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //have moveable marker effect but jerky
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
            Toast.makeText(getActivity(), "on cam change", Toast.LENGTH_SHORT).show();
        if(locationMarker != null) {
            locationMarker.remove();
        }
        locationMarker = null;
        updateMarker(cameraPosition.target);
    }


    /*public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMapObject.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }*/


}
