package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/7/15.
 */
public class MilkFragment extends Fragment {

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        storeMenu = getArguments().getParcelable("menu");
//        storeMenu = new StoreMenu(true);
//        aMenu = new MenuAdapter(getActivity(), storeMenu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_milk, container, false);
//        lvMenu = (ListView) v.findViewById(R.id.lvMenu);
//        lvMenu.setAdapter(aMenu);
//        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                // Present Dialog Fragment
//                Item item = (Item) aMenu.getItem(i);
//                showCustomizeItemDialog(item);
//            }
//        });
        return v;
    }
}
