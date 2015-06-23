package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/7/15.
 */
public class MilkFragment extends Fragment {

    private OnPercentageChosenListener listener;

    public interface OnPercentageChosenListener {
        void onMilkPercentageChosen();
    }


    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            if (parentFragment instanceof OnPercentageChosenListener) {
                listener = (OnPercentageChosenListener) parentFragment;
            } else {
                throw new ClassCastException(parentFragment.toString()
                        + " must implement MyListFragment.OnItemSelectedListener");
            }
        } else {
            if (getActivity() instanceof OnPercentageChosenListener) {
                listener = (OnPercentageChosenListener) getActivity();
            } else {
                throw new ClassCastException(getActivity().toString()
                        + " must implement MyListFragment.OnItemSelectedListener");
            }
        }
//        storeMenu = getArguments().getParcelable("menu");
//        storeMenu = new StoreMenu(true);
//        aMenu = new MenuAdapter(getActivity(), storeMenu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_milk, container, false);
        Button btnContinue = (Button) v.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMilkPercentageChosen();
            }
        });

        return v;
    }
}
