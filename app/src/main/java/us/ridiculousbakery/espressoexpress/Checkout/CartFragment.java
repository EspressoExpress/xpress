package us.ridiculousbakery.espressoexpress.Checkout;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.InProgress.Receiving.ReceivingActivity;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.SelectedOption;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.Model.TempOrder;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/7/2015.
 */
public class CartFragment extends Fragment {

    protected ArrayList<LineItem> lineItems;
    //protected CartItemAdapter alineItems;
    protected CartItemAdapterSwipe alineItems;
    protected RelativeLayout rlDeliveryAddress;
    protected RelativeLayout rlCCInfo;
    protected ListView lvOrderItems;
    protected TextView tvAddress;
    protected TextView tvAddressLine2;
    protected TextView tvChangeAddress;
    protected TextView tvStoreName;
    protected TextView tvCCInfo;
    protected TextView tvChangeCCInfo;
    protected TextView tvTotal$;
    protected Button btAddress;
    protected Button btPayment;
    protected Button btCheckout;
    private TempOrder order;
    private Store store;
    private String storeId;
    //private CreditCard cc;
    private OnWidgetClickedListener listener;
    static private boolean LOCK_VISIBILITY = false;

    public interface OnWidgetClickedListener {
        void launchAddressMap();
        void launchAddressMap(LatLng latLng);
        void launchCCForm();
    }

    /*public static CartFragment newInstance(String orderId) {
        CartFragment cartFragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable("orderId", orderId);
        cartFragment.setArguments(args);
        return cartFragment;
    }*/

    public static CartFragment newInstance(TempOrder order, String storeID) {
        CartFragment cartFragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        args.putString("StoreId", storeID);
        cartFragment.setArguments(args);
        return cartFragment;
    }

    //inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container,false);
        tvStoreName = (TextView) v.findViewById(R.id.tvStoreName);
        tvStoreName.setText(store.getName());
        rlDeliveryAddress = (RelativeLayout) v.findViewById(R.id.rlDeliveryAddress);
        rlCCInfo = (RelativeLayout) v.findViewById(R.id.rlCCInfo);
        btAddress = (Button) v.findViewById(R.id.btAddress);
        btCheckout = (Button) v.findViewById(R.id.btCheckout);
        btCheckout.setEnabled(false);
        btPayment = (Button) v.findViewById(R.id.btPayment);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvAddressLine2 = (TextView) v.findViewById(R.id.tvAddressLine2);
        tvChangeAddress = (TextView) v.findViewById(R.id.tvChangeAddress);
        tvCCInfo = (TextView) v.findViewById(R.id.tvCCInfo);
        tvChangeCCInfo = (TextView) v.findViewById(R.id.tvChangeCCInfo);
        tvTotal$ = (TextView) v.findViewById(R.id.tvTotal$);
        tvTotal$.setText("$" + StringHelper.priceToString(order.getTotalPrice()));
        lvOrderItems = (ListView) v.findViewById(R.id.lvOrderItems);
        lvOrderItems.setAdapter(alineItems);
        setupListeners();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (TempOrder) getArguments().getSerializable("order");
        lineItems = order.getLineItems();
        alineItems = new CartItemAdapterSwipe(getActivity(), lineItems);
        store = Store.getInForeground(getArguments().getString("StoreId"));
        /*Store.getInBackground(getArguments().getString("StoreId"), new GetCallback<Store>() {
            @Override
            public void done(Store store, ParseException e) {
                if(e!=null) e.printStackTrace();
                CartFragment.this.store = store;
            }
        });
        */
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
                final Order orderObj = new Order();
                ParseUser user = ParseUser.getCurrentUser();
                orderObj.put("name", user.get("displayName"));
                orderObj.put("store_name", store.getName());
                orderObj.put("storeId", store.getObjectId());
                orderObj.put("status", Order.SUBMITTED);
                orderObj.put("receiver_id", user.getObjectId());
                orderObj.put("delivery_lat", order.getLatLng().latitude);
                orderObj.put("delivery_lng", order.getLatLng().longitude);
                for (int i=0;i<order.getLineItems().size();i++) {
                    //need price and selection later in line items but just get the name for now
                    LineItem lineItem = order.getLineItems().get(i);
                    ParseObject lineItemObj = new ParseObject("LineItem");
                    lineItemObj.put("name", lineItem.getItem().getName());
                    lineItemObj.put("selectOptions", SelectedOption.ArrayToString(lineItem.getChosenOptions()));
                    try {
                        lineItemObj.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ParseRelation<ParseObject> relation = orderObj.getRelation("lineItems");
                    relation.add(lineItemObj);
                }
                try {
                    orderObj.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getActivity(), ReceivingActivity.class);
                i.putExtra("orderID", orderObj.getObjectId());
                startActivity(i);
            }
        });

    }

    public void saveAndShowAddress(LatLng latLng, Address address) {
        if (latLng != null) {
            order.setLat(latLng.latitude);
            order.setLon(latLng.longitude);
            tvAddress.setText(address.getAddressLine(0));
            tvAddressLine2.setText(address.getLocality() + ", " + address.getAdminArea());
            if (!LOCK_VISIBILITY) {
                rlDeliveryAddress.setVisibility(View.VISIBLE);
                //doesn't work
                /*rlDeliveryAddress.animate().translationY(tvAddress.getHeight())
                    .alpha(1.0f)
                    .setDuration(2000);*/
                btAddress.setVisibility(View.GONE);
                btPayment.setVisibility(View.VISIBLE);
            }

        }
    }

    public void saveAndShowCCInfo(CreditCard cc) {

        //
        String ccNumber = cc.getCardNumber();
        tvCCInfo.setText(cc.getCardType().toString() + " ending in " + ccNumber.substring(ccNumber.length() - 4, ccNumber.length()));
        if (!LOCK_VISIBILITY) {
            rlCCInfo.setVisibility(View.VISIBLE);
            btPayment.setVisibility(View.GONE);
        }
        btCheckout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_statelist));
        btCheckout.setVisibility(View.VISIBLE);
        btCheckout.setEnabled(true);
    }

    public void deleteItem(int position) {
        lineItems.remove(position);
        alineItems.notifyDataSetChanged();
        tvTotal$.setText("$" + StringHelper.priceToString(order.getTotalPrice()));
    }
}
