package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.app.ActionBar;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.OptionsAdapter;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.ItemOption;
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

//    private CupSizeFragment cupSizeFragment;
//    private MilkFragment milkFragment;

    private GridView gvOptions;
    private TableLayout tlChosen;
    private TableRow trOptions;
    private OptionsAdapter aOptions;
    private Item item;
    private Button btnAdd;

    private ArrayList<String> chosenOptions;

    public static CustomizeItemDialog newInstance(Item item) {
        CustomizeItemDialog dialog = new CustomizeItemDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        dialog.setArguments(args);
        return dialog;
    }

    //================================================================================
    // Lifecycle
    //================================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getArguments().getSerializable("item");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customize_item, container);
        gvOptions = (GridView) view.findViewById(R.id.gvOptions);
        tlChosen = (TableLayout) view.findViewById(R.id.tlChosen);
        trOptions = (TableRow) view.findViewById(R.id.trOptions);
        aOptions = new OptionsAdapter(getActivity(), item.getOptions());
        chosenOptions = new ArrayList<String>();
//        aOptions = new OptionsAdapter(getActivity(), lineItem.getItemOption());
        gvOptions.setAdapter(aOptions);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineItem lineItem = new LineItem(item, chosenOptions);
                listener.onFinishCustomizingLineItem(lineItem);
            }
        });

        setButtonVisability();

        gvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String op = aOptions.optionNameForPosition(position);
                addOptionAtIndex(position);
                aOptions.removeOption(op);
                setButtonVisability();
                Log.d("DEBUG", position + "");
            }
        });
        return view;
    }

    private void addOptionAtIndex(int pos) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.chosen_option, null);
        String op = (String) aOptions.getItem(pos);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(op);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonVisability();
            }
        });
        trOptions.addView(view);
        chosenOptions.add(op);
    }

    private void setButtonVisability() {
        if (aOptions.getCount() > 0) {
            btnAdd.setVisibility(Button.INVISIBLE);
        } else {
            btnAdd.setVisibility(Button.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int heiPx = metrics.heightPixels;
            int widPx = metrics.widthPixels;
            ViewGroup.LayoutParams params = new ActionBar.LayoutParams((int)(widPx*0.85), (int)(heiPx*0.85));
            dialog.getWindow().setLayout(params.width, params.height);
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
