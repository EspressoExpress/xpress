package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.MenuAdapter;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/7/15.
 */
public class CupSizeFragment extends Fragment implements View.OnClickListener {

    private OnSizeSelectedListener listener;

    private ImageButton ibSmall;
    private ImageButton ibMedium;
    private ImageButton ibLarge;
    private ImageButton ibXLarge;

    public interface OnSizeSelectedListener {
        public void onCupSizeSelected();
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            if (parentFragment instanceof OnSizeSelectedListener) {
                listener = (OnSizeSelectedListener) parentFragment;
            } else {
                throw new ClassCastException(parentFragment.toString()
                        + " must implement MyListFragment.OnItemSelectedListener");
            }
        } else {
            if (getActivity() instanceof OnSizeSelectedListener) {
                listener = (OnSizeSelectedListener) getActivity();
            } else {
                throw new ClassCastException(getActivity().toString()
                        + " must implement MyListFragment.OnItemSelectedListener");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cup_size, container, false);
        ibSmall = (ImageButton) v.findViewById(R.id.ibSmall);
        ibMedium = (ImageButton) v.findViewById(R.id.ibMedium);
        ibLarge = (ImageButton) v.findViewById(R.id.ibLarge);
        ibXLarge = (ImageButton) v.findViewById(R.id.ibXLarge);

        ibSmall.setOnClickListener(this);
        ibMedium.setOnClickListener(this);
        ibLarge.setOnClickListener(this);
        ibXLarge.setOnClickListener(this);

        return v;
    }


    //================================================================================
    // Listener
    //================================================================================

    @Override
    public void onClick(View view) {
        if (view == ibSmall) {
            listener.onCupSizeSelected();
        } else if (view == ibMedium) {
            listener.onCupSizeSelected();
        } else if (view == ibLarge) {
            listener.onCupSizeSelected();
        } else if (view == ibXLarge) {
            listener.onCupSizeSelected();
        }
    }
}
