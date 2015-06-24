package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.TreeMap;

import at.markushi.ui.CircleButton;
import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters.OptionsAdapter;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.Model.SelectedOption;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/6/15.
 */
public class CustomizeItemDialog extends DialogFragment {


    public interface CustomizeItemDialogListener {
        void onFinishCustomizingLineItem(LineItem lineItem);
    }

    private CustomizeItemDialogListener listener;

//    private GridView gvOptions;
    private StaggeredGridView gvOptions;
    private TableLayout tlChosen;
    private TableRow trOptions;
    private OptionsAdapter aOptions;
    private Item item;
    private Button btnAdd;
    private TextView tvChoicePrompt;

    private ArrayList<SelectedOption> chosenOptions;

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
        gvOptions = (StaggeredGridView) view.findViewById(R.id.gvOptions);
        tlChosen = (TableLayout) view.findViewById(R.id.tlChosen);
        trOptions = (TableRow) view.findViewById(R.id.trOptions);
        TreeMap<String, ArrayList<String>> optionsCopy = (TreeMap) item.getOptions().clone();
        aOptions = new OptionsAdapter(getActivity(), optionsCopy);
        chosenOptions = new ArrayList<>();
        gvOptions.setAdapter(aOptions);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        tvChoicePrompt = (TextView) view.findViewById(R.id.tvChoicePrompt);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double price = 0;
                for (int i=0; i<chosenOptions.size(); i++) {
                    SelectedOption op = chosenOptions.get(i);
                    if (op.getCategory().equals("Size")) {
                        String size = op.getName();
                        String parts[] = size.split("-");
//                        Log.d("PARTSDEBUG", parts[0]);
//                        Log.d("COUNT", parts.length +"");
                        op.setName(parts[0]);
                        price = Double.parseDouble(parts[1]);
                    }
                }

                LineItem lineItem = new LineItem(item, chosenOptions, 0.00);
                lineItem.setPrice(price);
                listener.onFinishCustomizingLineItem(lineItem);
            }
        });

        gvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (aOptions.positionIsInFirstGroup(position)) {
                    removeGroupIncludingItemAtPostion(position);
                    updateViewFromModel();
                }
            }
        });

        updateViewFromModel();
        return view;
    }

    private void removeGroupIncludingItemAtPostion(int position) {
        String op = aOptions.optionNameForPosition(position);
        addOptionAtIndex(position);
        aOptions.removeOption(op);
    }

    private void updateViewFromModel() {
        setButtonVisability();
        syncViewWithChosenOptions();

    }

    private void addOptionAtIndex(int pos) {
        String op = (String) aOptions.getItem(pos);
        String cat = aOptions.optionNameForPosition(pos);
        SelectedOption chosenOption = new SelectedOption(op, cat);
        chosenOptions.add(chosenOption);
    }

    private void setButtonVisability() {
        if (aOptions.getCount() > 0) {
            btnAdd.setVisibility(Button.INVISIBLE);
        } else {
            btnAdd.setVisibility(Button.VISIBLE);
        }
    }

    private void syncViewWithChosenOptions() {
        trOptions.removeAllViews();
        for (int i=0; i<chosenOptions.size(); i++) {
            SelectedOption op = chosenOptions.get(i);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.chosen_option, null);
            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            tvName.setText(op.getName());
            tvCategory.setText(op.getCategory());
            CircleButton btnCancel = (CircleButton) view.findViewById(R.id.btnCancel);
            btnCancel.setTag(op);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add Back what was clicked
                    SelectedOption opToDelete = (SelectedOption) v.getTag();
                    chosenOptions.remove(opToDelete);
                    aOptions.addOption(opToDelete.getCategory(), item.getOptions().get(opToDelete.getCategory()));
                    //aOptions.addOption(opToDelete.getName(), item.getOptions().get(opToDelete.getCategory()));
                    setButtonVisability();
                    syncViewWithChosenOptions();
                }
            });
            trOptions.addView(view);
        }
        if (aOptions.firstGroupKey() != null) {
            tvChoicePrompt.setText(aOptions.firstGroupKey());
        } else {
            tvChoicePrompt.setText("");
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


}
