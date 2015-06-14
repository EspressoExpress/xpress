package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Checkout.CartActivity;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.MenuAdapter;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
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

    //================================================================================
    // Constructors
    //================================================================================

    public static MenuFragment newInstance(StoreMenu storeMenu) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putSerializable("menu", storeMenu);
        fragment.setArguments(args);
        return fragment;
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeMenu = (StoreMenu) getArguments().getSerializable("menu");
        storeMenu = new StoreMenu(true);
        aMenu = new MenuAdapter(getActivity(), storeMenu);
        lineItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_list, container, false);
        elvMenu = (ExpandableListView) v.findViewById(R.id.elvMenu);
        elvMenu.setAdapter(aMenu);
        elvMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Item item = (Item) aMenu.getChild(groupPosition, childPosition);
                showCustomizeItemDialog(item);
                return true;
            }
        });

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
                    startActivity(i);
                    Log.d("DEBUG", "NEW INTENT");
                }
            }
        });

        View header = inflater.inflate(R.layout.menu_header, container, false);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        MenuHeaderFragment menuHeaderFragment = new MenuHeaderFragment();
        ft.replace(R.id.flContainer, menuHeaderFragment);
        ft.commit();
        elvMenu.addHeaderView(header);

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
