package us.ridiculousbakery.espressoexpress.Checkout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    private TextView tvAddress;
    private LatLng addressLatLng;
    private Address address;
    OnWidgetClickedListener listener;

    public interface OnWidgetClickedListener {
        void onSelectAddress(LatLng latLng, Address address);
        void onSearchAddress(LatLng latLng, Address address);
    }

    public AddressMapFragment() {

    }

    public static AddressMapFragment newInstance() {
        AddressMapFragment addressMapFragment = new AddressMapFragment();
        addressMapFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return addressMapFragment;
    }

    public static AddressMapFragment newInstance(Double lat, Double lng) {
        AddressMapFragment addressMapFragment = new AddressMapFragment();
        addressMapFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        addressMapFragment.setArguments(args);
        return addressMapFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null) {
            return;
        }
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics  = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        //float density = getResources().getDisplayMetrics().density;
        float dialogWidth = displayMetrics.widthPixels - 56; // specify a value here
        float dialogHeight = displayMetrics.heightPixels - 56; // specify a value here
        //Toast.makeText(getActivity(), "width: " + Math.round(dialogWidth) + ", height: " + Math.round(dialogHeight), Toast.LENGTH_LONG).show();
        getDialog().getWindow().setLayout(Math.round(dialogWidth), Math.round(dialogHeight));
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
        listener = (OnWidgetClickedListener) getActivity();
        ivMarker = (ImageView) v.findViewById(R.id.ivMarker);
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delivery_address);
        Bitmap marker = Bitmap.createScaledBitmap(temp, 128, 128, true);
        ivMarker.setImageBitmap(marker);

        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        btSelectAddress = (Button) v.findViewById(R.id.btSelectAddress);
        setupListeners();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            addressLatLng = new LatLng(args.getDouble("lat"), args.getDouble("lng"));
        }
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
            //Toast.makeText(getActivity(), "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
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
        if (addressLatLng == null) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                //Toast.makeText(getActivity(), "GPS location was found!", Toast.LENGTH_SHORT).show();
                addressLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(getActivity(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
            }
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(addressLatLng, 17);
        map.animateCamera(cameraUpdate);
        updateAddress(addressLatLng);
        //updateMarker(latLng);
        //startLocationUpdates();

    }

    private void updateAddress(LatLng latLng) {
        if (latLng != null) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            addressLatLng = latLng;
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (!addresses.isEmpty()) {
                    address = addresses.get(0);
                    tvAddress.setText(address.getAddressLine(0));
                }
            } catch (IOException e) {
                Log.d("updateAddress: ", "can't get address from geocoder", e);
            }
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

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        updateAddress(cameraPosition.target);
        /*if(locationMarker != null) {
            locationMarker.remove();
        }
        locationMarker = null;
        updateMarker(cameraPosition.target);*/
    }

    private void setupListeners() {
        btSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectAddress(addressLatLng, address);
                dismiss();
            }
        });


        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setTextColor(Color.LTGRAY);
                listener.onSearchAddress(addressLatLng, address);
                dismiss();
            }
        });
    }


}
