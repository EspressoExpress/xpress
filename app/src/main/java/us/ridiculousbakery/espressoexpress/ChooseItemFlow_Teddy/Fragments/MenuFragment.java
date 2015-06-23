package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Checkout.CartActivity;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.MenuAdapter;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.Order;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.Model.TempOrder;
import us.ridiculousbakery.espressoexpress.Model.TempStore;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/6/15.
 */
public class MenuFragment extends Fragment implements CustomizeItemDialog.CustomizeItemDialogListener {

    private MenuAdapter aMenu;
    private StoreMenu storeMenu;
    private ExpandableListView elvMenu;
    private Button btnCart;
    private ArrayList<LineItem> lineItems;
    private CustomizeItemDialog customizeDialog;
    private Store store;

    //================================================================================
    // Constructors
    //================================================================================

    public static MenuFragment newInstance(String storeId) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString("storeId", storeId);

//        args.putSerializable("store", store);
        fragment.setArguments(args);
        return fragment;
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });
//        ParseObject post = ...;
//
//        ParseObject category = ParseObject.getIn;
//        ParseRelation relation = user.getRelation("posts");
//        relation.add(post);
//        user.saveInBackground();

//        String storeID = getArguments().getString("storeId");
//
//
//        store = (Store) getArguments().getSerializable("store");
//        storeMenu = store.getStoreMenu();
//        aMenu = new MenuAdapter(getActivity(), storeMenu);
//        // Create an initializer from aMenu
//        lineItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_menu_list, null, false);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String storeID = getArguments().getString("storeId");
        storeID = "1NoCwWrzM5";

        Store.getInBackground(storeID, new GetCallback<Store>() {
            @Override
            public void done(final Store store, ParseException e) {

                if (e == null) {
                    Log.d("Success in findg store", "Success in finding stre");
                    String menuString = (String) store.get("menu");
                    JSONObject menuJSON = null;
                    try {
                        menuJSON = new JSONObject(menuString);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    storeMenu = StoreMenu.fromJSON(menuJSON);


                    //storeMenu = store.getStoreMenu();
                    aMenu = new MenuAdapter(getActivity(), storeMenu);
                    lineItems = new ArrayList<>();
                    elvMenu = (ExpandableListView) v.findViewById(R.id.elvMenu);
                    Log.d("DEBUG", "LISTVIEWEXPAND is " + elvMenu.toString());
                    elvMenu.setAdapter(aMenu);
                    elvMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            Item item = (Item) aMenu.getChild(groupPosition, childPosition);
                            Log.d("ITEMNAME", item.getName());
                            showCustomizeItemDialog(item);
                            return true;
                        }
                    });

                    View header = inflater.inflate(R.layout.menu_header, null, false);


                    String imageURL = (String)store.get("imageURL");

                    //Log.d("IMAGE", imageURL);

                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    MenuHeaderFragment menuHeaderFragment = MenuHeaderFragment.newInstance("Hello", imageURL);
                    ft.replace(R.id.flContainer, menuHeaderFragment);
                    ft.commit();
                    elvMenu.addHeaderView(header);

                    for(int i=0; i < aMenu.getGroupCount(); i++) {
                        elvMenu.expandGroup(i);
                    }

                    btnCart = (Button) v.findViewById(R.id.btnCart);
                    setCartButtonHeight();
                    btnCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Launch Intent!
                            if (lineItems.size() > 0) {
                                Intent i = new Intent(getActivity(), CartActivity.class);
                                TempOrder order = new TempOrder();
                                TempStore tempStore = new TempStore(store);
                                order.setStore(tempStore);
                                order.setLineItems(lineItems);
                                i.putExtra("order", order);
                                startActivity(i);
                            }
                        }
                    });


                }
            }
        });

        return v;
    }

    private void setCartButtonHeight() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)btnCart.getLayoutParams();
        if (lineItems.size() > 0) {
            params.height = 150;
        } else {
            params.height = 0;
        }
        btnCart.setLayoutParams(params);
    }



    //================================================================================
    // Navigation
    //================================================================================

    public void showCustomizeItemDialog(Item item) {
        FragmentManager fm = getChildFragmentManager();
        customizeDialog = CustomizeItemDialog.newInstance(item);
        customizeDialog.show(fm, "fragment_customize_item");
    }

    //================================================================================
    // CustomizeItemDialogListener
    //================================================================================

    @Override
    public void onFinishCustomizingLineItem(LineItem lineItem) {
        // Fire Intent!
        customizeDialog.dismiss();
        lineItems.add(lineItem);
        setCartButtonHeight();
    }

}
