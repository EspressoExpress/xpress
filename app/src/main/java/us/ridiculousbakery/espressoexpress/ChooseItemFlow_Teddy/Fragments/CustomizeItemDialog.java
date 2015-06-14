package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.User;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/6/15.
 */
public class CustomizeItemDialog extends DialogFragment {


    public interface CustomizeItemDialogListener {
        void onFinishCustomizingLineItem(LineItem lineItem);
    }


    private CustomizeItemDialogListener listener;

    private CupSizeFragment cupSizeFragment;
    private MilkFragment milkFragment;

    private LineItem lineItem;

    public static CustomizeItemDialog newInstance(Item item) {
        CustomizeItemDialog dialog = new CustomizeItemDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putParcelable("item", item);
        dialog.setArguments(args);
        return dialog;
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customize_item, container);
        //showCupSizeFragment(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Item item = getArguments().getParcelable("item");
        lineItem = new LineItem(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            if (parentFragment instanceof CustomizeItemDialogListener) {
                listener = (CustomizeItemDialogListener) parentFragment;
            } else {
                throw new ClassCastException(parentFragment.toString()
                        + " must implement CustomizeItemDialogListener");
            }
        } else {
            if (getActivity() instanceof CustomizeItemDialogListener) {
                listener = (CustomizeItemDialogListener) getActivity();
            } else {
                throw new ClassCastException(getActivity().toString()
                        + " must implement CustomizeItemDialogListener");
            }
        }
    }

    //================================================================================
    // Fragment Switches
    //================================================================================

//    private void showCupSizeFragment(View view) {
//        if (cupSizeFragment == null) {
//            cupSizeFragment = new CupSizeFragment();
//        }
//        FrameLayout flContainer = (FrameLayout) view.findViewById(R.id.flContainer);
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//        ft.replace(R.id.flContainer, cupSizeFragment);
//        ft.commit();
//    }
//
//    private void showMilkFragment(View view) {
//        if (milkFragment == null) {
//            milkFragment = new MilkFragment();
//        }
//        FrameLayout flContainer = (FrameLayout) view.findViewById(R.id.flContainer);
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//        ft.replace(R.id.flContainer, milkFragment);
//        ft.commit();
//    }

    //================================================================================
    // Animation
    //================================================================================
    private void slideInToTop(View v, boolean animated) {
//        v.animate().
//                translationY(0).
//                alpha(1).
//                setDuration(animated ? ANIMATION_DURATION : 0).
//                setInterpolator(ANIMATION_INTERPOLATOR);
    }

    //================================================================================
    // OnSizeSelectedListener
    //================================================================================

//    @Override
//    public void onCupSizeSelected() {
//        // Store Size in Options and show next fragment
//        showMilkFragment(getView());
//    }

    //================================================================================
    // OnPercentageChosenListener
    //================================================================================

//    @Override
//    public void onMilkPercentageChosen() {
//        listener.onFinishCustomizingLineItem(lineItem);
//        dismiss();
//    }

}
