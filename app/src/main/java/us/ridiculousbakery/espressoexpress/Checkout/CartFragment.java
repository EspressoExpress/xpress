package us.ridiculousbakery.espressoexpress.Checkout;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devmarvel.creditcardentry.library.CreditCard;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ReceivingActivity;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.SelectedOption;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/7/2015.
 */
public class CartFragment extends Fragment {

    protected ArrayList<LineItem> lineItems;
    protected CartItemAdapter alineItems;
    protected RelativeLayout rlDeliveryAddress;
    protected RelativeLayout rlCCInfo;
    protected ListView lvOrderItems;
    protected TextView tvAddress;
    protected TextView tvAddressLine2;
    protected TextView tvChangeAddress;
    protected TextView tvStoreName;
    protected TextView tvCCInfo;
    protected TextView tvChangeCCInfo;
    protected Button btAddress;
    protected Button btPayment;
    protected Button btCheckout;
    private Order order;
    private CreditCard cc;
    private OnWidgetClickedListener listener;
    static private boolean LOCK_VISIBILITY = false;

    public interface OnWidgetClickedListener {
        public void launchAddressMap();
        public void launchAddressMap(LatLng latLng);
        public void launchCCForm();
    }

    public static CartFragment newInstance(Order order) {
        CartFragment cartFragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        cartFragment.setArguments(args);
        return cartFragment;
    }

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container,false);
        tvStoreName = (TextView) v.findViewById(R.id.tvStoreName);
        tvStoreName.setText(order.getStore().getName());
        rlDeliveryAddress = (RelativeLayout) v.findViewById(R.id.rlDeliveryAddress);
        rlCCInfo = (RelativeLayout) v.findViewById(R.id.rlCCInfo);
        btAddress = (Button) v.findViewById(R.id.btAddress);
        btCheckout = (Button) v.findViewById(R.id.btCheckout);
        btPayment = (Button) v.findViewById(R.id.btPayment);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvAddressLine2 = (TextView) v.findViewById(R.id.tvAddressLine2);
        tvChangeAddress = (TextView) v.findViewById(R.id.tvChangeAddress);
        tvCCInfo = (TextView) v.findViewById(R.id.tvCCInfo);
        tvChangeCCInfo = (TextView) v.findViewById(R.id.tvChangeCCInfo);
        lvOrderItems = (ListView) v.findViewById(R.id.lvOrderItems);
        lvOrderItems.setAdapter(alineItems);
        setupListeners();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (Order) getArguments().getSerializable("order");
        lineItems = order.getLineItems();
        alineItems = new CartItemAdapter(getActivity(), lineItems);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnWidgetClickedListener) {
            listener = (OnWidgetClickedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    private void setupListeners() {
        tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchAddressMap(order.getLatLng());
                LOCK_VISIBILITY = true;
            }
        });
        tvChangeCCInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchCCForm();
                LOCK_VISIBILITY = true;
            }
        });
        lvOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "launch item option fragment", Toast.LENGTH_SHORT).show();
            }
        });

        btAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchAddressMap();
                LOCK_VISIBILITY = false;
            }
        });
        btPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchCCForm();
                LOCK_VISIBILITY = false;
            }
        });
        btCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseObject orderObj = new ParseObject("Order");
                ParseUser user = ParseUser.getCurrentUser();
                orderObj.put("name", user.get("displayName"));
                orderObj.put("receiver_id", user.getObjectId());
                orderObj.put("delivery_lat", order.getLatLng().latitude);
                orderObj.put("delivery_lng", order.getLatLng().longitude);
                orderObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseRelation<ParseObject> relation = orderObj.getRelation("lineItems");
                            //  Add price to line item later
                            for (int i=0;i<order.getLineItems().size();i++) {
                                LineItem lineItem = order.getLineItems().get(i);
                                ParseObject lineItemObj = new ParseObject("LineItem");
                                lineItemObj.put("name", lineItem.getItem().getName());
                                ArrayList<String> des = new ArrayList<>();
                                for (int j=0; j<lineItem.getChosenOptions().size(); j++) {
                                    SelectedOption op = lineItem.getChosenOptions().get(j);
                                    des.add(op.getCategory() + " - " + op.getName());
                                }
                                lineItemObj.put("descriptions", des);
                                relation.add(lineItemObj);
                            }
                        }
                        else {
                            Log.d("Parse 1: ", e.toString());
                        }
                    }
                });

                // Show progress Indicator
                orderObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null) {
                            ParseUser user = ParseUser.getCurrentUser();
                            user.put("currentOrderId", orderObj.getObjectId());
                            try{user.save();}catch(ParseException ee){}
                            Intent i = new Intent(getActivity(), ReceivingActivity.class);
                            startActivity(i);
                        }
                        else {
                            Log.d("Parse 2: ", e.toString());
                        }

                        // Stop progress indicator
                    }
                });

            }
        });

    }

    public void saveAndShowAddress(LatLng latLng, Address address) {
        if (latLng != null) {
            order.setLat(latLng.latitude);
            order.setLon(latLng.longitude);
            tvAddress.setText(address.getAddressLine(0));
            tvAddressLine2.setText(address.getLocality() + ", " + address.getAdminArea());
            //animation doesn't work
            /*tvAddress.animate().translationY(tvAddress.getHeight())
                    .alpha(1.0f)
                    .setDuration(2000);*/
            if (!LOCK_VISIBILITY) {
                rlDeliveryAddress.setVisibility(View.VISIBLE);
                btAddress.setVisibility(View.GONE);
                btPayment.setVisibility(View.VISIBLE);
            }

        }
    }

    public void saveAndShowCCInfo(CreditCard cc) {
        //save ccInfo to order object
        this.cc = cc;
        //
        String ccNumber = cc.getCardNumber();
        tvCCInfo.setText(cc.getCardType().toString() + " ending in " + ccNumber.substring(ccNumber.length() - 4, ccNumber.length()));
        if (!LOCK_VISIBILITY) {
            rlCCInfo.setVisibility(View.VISIBLE);
            btPayment.setVisibility(View.GONE);
        }
        btCheckout.setEnabled(true);
    }
}
