package us.ridiculousbakery.espressoexpress.Checkout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/7/2015.
 */
public class CartFragment extends Fragment {

    protected ArrayList<LineItem> lineItems;
    protected CartItemAdapter alineItems;
    protected ListView lvOrderItems;
    protected TextView tvAddress;
    protected TextView tvStoreName;
    protected Button btPayment;
    protected Button btCheckout;
    private Order order;
    private OnItemClickedListener listener;

    public interface OnItemClickedListener {
        public void launchAddressMap();
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

        btCheckout = (Button) v.findViewById(R.id.btCheckout);
        btPayment = (Button) v.findViewById(R.id.btPayment);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
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
        if (activity instanceof OnItemClickedListener) {
            listener = (OnItemClickedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    private void setupListeners() {
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "launch map to pick Address", Toast.LENGTH_SHORT).show();
                listener.launchAddressMap();
            }
        });
        lvOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "launch item option fragment", Toast.LENGTH_SHORT).show();
            }
        });
        btPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchCCForm();
            }
        });

    }
}
