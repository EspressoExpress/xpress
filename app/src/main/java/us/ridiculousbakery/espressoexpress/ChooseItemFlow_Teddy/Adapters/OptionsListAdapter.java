package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import us.ridiculousbakery.espressoexpress.Model.CheckItem;
import us.ridiculousbakery.espressoexpress.Model.SelectedOption;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/24/15.
 */
public class OptionsListAdapter extends BaseAdapter {

    public interface OptionsListAdapterListener {
        public void itemChosen();
    }

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<CheckItem> mData = new ArrayList<CheckItem>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    public OptionsListAdapterListener listener;

    private LayoutInflater mInflater;

    public int numberOfChecked() {
        int count = 0;
        for (int i=0; i<mData.size(); i++) {
            CheckItem h = getItem(i);
            if (h.isChecked) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<SelectedOption> getChosenOptions() {

        ArrayList<SelectedOption> newOptions = new ArrayList<>();

        String key = "";
        String value = "";

        for (int i=0; i<mData.size(); i++) {

            if (getItemViewType(i) == TYPE_SEPARATOR) {
                key = getItem(i).name;
            } else {
                CheckItem it = getItem(i);
                if (it.isChecked) {
                    value = it.name;
                }
            }
            if (!key.isEmpty() && value.isEmpty()) {
                SelectedOption chosenOption = new SelectedOption(value, key);
                newOptions.add(chosenOption);
                key = "";
                value = "";
            }

        }
        return newOptions;

    }

    public OptionsListAdapter(Context context, TreeMap<String, ArrayList<String>> options) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(Map.Entry<String, ArrayList<String>> entry : options.entrySet()) {
            String key = entry.getKey();
            addSectionHeaderItem(key);
            List<String> strings = entry.getValue();
            for (int i=0; i<strings.size(); i++) {
                String item = strings.get(i);
                addItem(item);
            }
        }


    }

    public void addItem(final String item) {
        mData.add(new CheckItem(item));
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(new CheckItem(item));
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CheckItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        CheckViewHolder checkHolder = null;

        int rowType = getItemViewType(position);

        switch (rowType) {
            case TYPE_ITEM:
                checkHolder = new CheckViewHolder();
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.menu_checkbox_item, null);
                    checkHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cbName);
                    convertView.setTag(checkHolder);
                } else {
                    checkHolder = (CheckViewHolder) convertView.getTag();
                }

                checkHolder.checkBox.setText(mData.get(position).name);
                checkHolder.checkBox.setChecked(mData.get(position).isChecked);

                checkHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d("ISCHECKED", isChecked +"");

                        CheckItem jj = getItem(position);
                        jj.isChecked = isChecked;
                        if (isChecked) {
                            for (int i=position-1; i>=0; i--) {
                                int type = getItemViewType(i);
                                if (type == TYPE_ITEM) {
                                    CheckItem cItem = getItem(i);
                                    cItem.isChecked = false;
                                } else {
                                    break;
                                }
                            }
                            for (int j=position+1; j<mData.size(); j++) {
                                int type = getItemViewType(j);
                                if (type == TYPE_ITEM) {
                                    CheckItem cItem = getItem(j);
                                    cItem.isChecked = false;
                                } else {
                                    break;
                                }
                            }
                            notifyDataSetChanged();

                            listener.itemChosen();
                        }

                    }
                });

                break;
            case TYPE_SEPARATOR:

                holder = new ViewHolder();

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.menu_category, null);
                    convertView.setTag(holder);
                    holder.textView = (TextView) convertView.findViewById(R.id.tvCategory);

                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.textView.setText(mData.get(position).name);

                break;
        }


        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

    public static class CheckViewHolder {
        public CheckBox checkBox;
    }



}


