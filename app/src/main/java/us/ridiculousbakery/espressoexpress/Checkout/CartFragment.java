package us.ridiculousbakery.espressoexpress.Checkout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/7/2015.
 */
public class CartFragment extends Fragment {

    protected ListView lv_orderItems;

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
        lv_orderItems = (ListView) v.findViewById(R.id.lv_orderItems);
        //lv_orderItems.setAdapter(aTweets);
        //setupListeners();
        return v;
    }
}
