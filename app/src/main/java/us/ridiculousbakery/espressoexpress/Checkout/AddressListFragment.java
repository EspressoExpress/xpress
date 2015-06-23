package us.ridiculousbakery.espressoexpress.Checkout;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/13/2015.
 */
public class AddressListFragment extends DialogFragment {

    protected ArrayList<Address> listAddresses;
    //protected ArrayAdapter<Address> aListAddresses;
    protected AddressListAdapter aListAddresses;
    protected ListView lvAddresses;
    private EditText etAddress;
    private Button btCancelAddress;
    private LatLng anchorLatLng;
    OnWidgetClickedListener listener;

    public interface OnWidgetClickedListener {
        void onCancelSearch(LatLng latLng);
        void onSelectSearchResult(LatLng latLng);
    }

    public  AddressListFragment() {

    }

    public static AddressListFragment newInstance(Double lat, Double lng, Address address) {
        AddressListFragment addressListFragment = new AddressListFragment();
        addressListFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lng", lng);
        args.putParcelable("address", address);
        addressListFragment.setArguments(args);
        return addressListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_address_list, container, false);
        listener = (OnWidgetClickedListener) getActivity();
        etAddress = (EditText) v.findViewById(R.id.etAddress);
        etAddress.setText(StringHelper.addressToString((Address) getArguments().getParcelable("address")));
        etAddress.setSelection(etAddress.getText().length());
        etAddress.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        btCancelAddress = (Button) v.findViewById(R.id.btCancelAddress);
        lvAddresses = (ListView) v.findViewById(R.id.lvAddresses);
        lvAddresses.setAdapter(aListAddresses);
        setupListeners();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anchorLatLng = new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        listAddresses = new ArrayList<>();
        //aListAddresses = new ArrayAdapter<Address>(getActivity(), android.R.layout.simple_list_item_1, listAddresses);
        aListAddresses = new AddressListAdapter(getActivity(), listAddresses);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //still doesn't show soft ketboard
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        return dialog;
    }

    private void setupListeners() {
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getActivity(), s.toString(), Toast.LENGTH_LONG).show();
                if (s.toString().isEmpty()) {
                    listAddresses.clear();
                    aListAddresses.notifyDataSetChanged();
                } else {
                    (new LocationSearchTask()).execute(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btCancelAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelSearch(anchorLatLng);
                dismiss();
            }
        });

        lvAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address selectedAddress = listAddresses.get(position);
                listener.onSelectSearchResult(new LatLng(selectedAddress.getLatitude(), selectedAddress.getLongitude()));
                dismiss();
            }
        });
    }

    class LocationSearchTask extends AsyncTask<String, Void, List<Address>> {
        final static int MAX_RESULTS = 5;
        final static double SEARCH_BOUNDARY = .2;
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
                            anchorLatLng.latitude - SEARCH_BOUNDARY ,
                            anchorLatLng.longitude - SEARCH_BOUNDARY ,
                            anchorLatLng.latitude + SEARCH_BOUNDARY ,
                            anchorLatLng.longitude + SEARCH_BOUNDARY);
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
            if (addresses == null) {
                //Toast.makeText(getActivity(), "Location could not be found", Toast.LENGTH_LONG).show();
            }
            else {
                aListAddresses.clear();
                aListAddresses.addAll(addresses);
            }
            //progressItem.setVisible(false);
        }
    }

}
