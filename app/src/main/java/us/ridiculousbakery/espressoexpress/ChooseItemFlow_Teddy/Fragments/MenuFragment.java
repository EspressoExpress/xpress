package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.melnykov.fab.FloatingActionButton;
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
    private FloatingActionButton btnCart;
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

        String storeID = getArguments().getString("storeId");
        storeID = "1NoCwWrzM5";

        lineItems = new ArrayList<>();

        btnCart = (FloatingActionButton) v.findViewById(R.id.btnCart);
        setCartButtonHeight();


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

                    final View header = inflater.inflate(R.layout.menu_header, null, false);


                    String imageURL = (String)store.get("imageURL");

                    //Log.d("IMAGE", imageURL);

                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    final MenuHeaderFragment menuHeaderFragment = MenuHeaderFragment.newInstance(store.getName(), imageURL);
                    ft.replace(R.id.flContainer, menuHeaderFragment);
                    ft.commit();
                    elvMenu.addHeaderView(header);

                    for(int i=0; i < aMenu.getGroupCount(); i++) {
                        elvMenu.expandGroup(i);
                    }

                    elvMenu.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            final float headerHeight = header.getHeight() - ((AppCompatActivity) getActivity()).getSupportActionBar().getHeight();
//                            Log.d("HEIGHT", headerHeight + "");
//                            Log.d("HEIGHT", header.getTop() + "");

                            float head = header.getTop() * -1;
                            float base = (float) (headerHeight / 2.5);
                            float scalingFactor = 1 - (head / base);

//                            Log.d("FACTOR", scalingFactor + "");
//                            Log.d("HEAD", head + "");
//
//
                            AppCompatActivity act = (AppCompatActivity) getActivity();
                            ActionBar bar = act.getSupportActionBar();

                            if (headerHeight - head < bar.getHeight() / 6) {
                                bar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                                bar.setTitle(Html.fromHtml("<font color='#ffffff'>" + store.getName() + "</font>"));
                                bar.setTitle(store.getName());
                            } else {
                                bar.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                                bar.setTitle("");
                            }


                            float magic = headerHeight / 2;

                            if (head > magic) {
                                menuHeaderFragment.translateTitleTextVertical(head - magic);
                            }

                            float alphaScale = head / headerHeight;
                            if (alphaScale < 0.4) {
                                alphaScale = 0.4f;
                            }
                            menuHeaderFragment.setOverlayAlpha(alphaScale);

                            if (scalingFactor > 0.5f) {
                                menuHeaderFragment.scaleTitleText(scalingFactor);
                            }


                        }
                    });


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
//
    private void setCartButtonHeight() {

        if (lineItems.size() > 0) {
            if (btnCart.getScaleX() == 0.0f) {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(btnCart, "scaleX", 0.0f, 1.0f)
                                .setDuration(500),
                        ObjectAnimator.ofFloat(btnCart, "scaleY", 0.0f, 1.0f)
                                .setDuration(500)
                );
                set.start();
            }
        } else {
            btnCart.setScaleX(0);
            btnCart.setScaleY(0);
        }
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
