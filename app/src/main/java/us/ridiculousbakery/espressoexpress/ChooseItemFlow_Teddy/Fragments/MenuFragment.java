package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.Header;
import org.json.JSONObject;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.MenuAdapter;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.Model.User;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/6/15.
 */
public class MenuFragment extends Fragment implements CustomizeItemDialog.CustomizeItemDialogListener {

    private MenuAdapter aMenu;
    private StoreMenu storeMenu;
    private ListView lvMenu;

    //================================================================================
    // Constructors
    //================================================================================

    public static MenuFragment newInstance(StoreMenu storeMenu) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelable("menu", storeMenu);
        fragment.setArguments(args);
        return fragment;
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeMenu = getArguments().getParcelable("menu");
        storeMenu = new StoreMenu(true);
        aMenu = new MenuAdapter(getActivity(), storeMenu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_list, container, false);
        lvMenu = (ListView) v.findViewById(R.id.lvMenu);
        lvMenu.setAdapter(aMenu);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Present Dialog Fragment
                Item item = (Item) aMenu.getItem(i);
                showCustomizeItemDialog(item);
            }
        });
        return v;
    }

    //================================================================================
    // Navigation
    //================================================================================

    public void showCustomizeItemDialog(Item item) {
        FragmentManager fm = getChildFragmentManager();
        CustomizeItemDialog customizeDialog = CustomizeItemDialog.newInstance(item);
        customizeDialog.show(fm, "fragment_customize_item");
    }

    //================================================================================
    // CustomizeItemDialogListener
    //================================================================================


    @Override
    public void onFinishCustomizingLineItem(LineItem lineItem) {

    }
}
