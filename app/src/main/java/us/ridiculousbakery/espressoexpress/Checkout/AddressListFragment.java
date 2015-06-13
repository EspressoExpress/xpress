package us.ridiculousbakery.espressoexpress.Checkout;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/13/2015.
 */
public class AddressListFragment extends DialogFragment {

    private EditText etAddress;
    private Button btCancelAddress;
    private LatLng anchorLatLng;

    public  AddressListFragment() {

    }

    public static AddressListFragment newInstance(Double lat, Double lng) {
        AddressListFragment addressListFragment = new AddressListFragment();
        addressListFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        addressListFragment.setArguments(args);
        return addressListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_address_list, container, false);
        etAddress = (EditText) v.findViewById(R.id.etAddress);
        etAddress.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        etAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Toast.makeText(getActivity(), etAddress.getText().toString(), Toast.LENGTH_SHORT).show();
                new UpdateEventLocationTask().execute(etAddress.getText().toString());
                return false;
            }
        });
        btCancelAddress = (Button) v.findViewById(R.id.btCancelAddress);
        btCancelAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anchorLatLng = new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
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

    class UpdateEventLocationTask extends AsyncTask<String, Void, List<Address>> {
        final static double SEARCH_BOUNDS_PRECISION = .3;
        final static int MAX_RESULTS = 7;

        @Override
        protected void onPreExecute() {
            //progressItem.setVisible(true);
        }

        @Override
        protected List<Address> doInBackground(String... params) {
            String locationName = params[0];
            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                if (anchorLatLng != null) {
                    addresses = geocoder.getFromLocationName(locationName, MAX_RESULTS,
                            anchorLatLng.latitude - SEARCH_BOUNDS_PRECISION,
                            anchorLatLng.longitude - SEARCH_BOUNDS_PRECISION,
                            anchorLatLng.latitude + SEARCH_BOUNDS_PRECISION,
                            anchorLatLng.longitude + SEARCH_BOUNDS_PRECISION);
                } else {
                    addresses = geocoder.getFromLocationName(locationName, 1);
                }

                if (addresses.isEmpty()) {
                    return null;
                }
                return addresses;
            } catch (IOException e) {
                Log.e("asynctask: ", "Do in Background failed", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            if (addresses.isEmpty()) {
                Toast.makeText(getActivity(), "Location could not be found", Toast.LENGTH_LONG).show();
            }
            else {

            }
            //progressItem.setVisible(false);
        }
    };

}
