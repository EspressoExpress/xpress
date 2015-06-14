package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.ItemOption;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/13/15.
 */
public class OptionsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private TreeMap<String, ArrayList<String>> options;
    //private ItemOption options;

    private class ViewHolder {
        public TextView name;
    }


    private ArrayList<String> sortedOptionValues() {
        ArrayList<String> internalOptions = new ArrayList<>();
        for(Map.Entry<String, ArrayList<String>> entry : options.entrySet()) {
            List<String> strings = entry.getValue();
            for (int i=0; i<strings.size(); i++) {
                internalOptions.add(strings.get(i));
            }
        }
        return internalOptions;
    }

    public OptionsAdapter(Context context, TreeMap<String, ArrayList<String>> options) {
        this.inflater = LayoutInflater.from(context);
        this.options = options;

    }

    public void removeOption(String name) {
        options.remove(name);
//        options.getOptions().remove(option);
        notifyDataSetChanged();
    }

    public void addOption(String name, ArrayList<String> choices) {
        options.put(name, choices);
        notifyDataSetChanged();
    }

    public String optionNameForPosition(int position) {
        int pos = position;
        for(Map.Entry<String, ArrayList<String>> entry : options.entrySet()) {
            List<String> strings = entry.getValue();
            for (int i=0; i<strings.size(); i++) {
                if (pos == 0) {
                    return entry.getKey();
                }
                pos--;
            }
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        String op = (String) getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.option_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(op);


        ArrayList<String> firstGroup = options.get(options.firstKey());
        if (position < firstGroup.size()) {
            convertView.setAlpha(1.0f);
        } else {
            convertView.setAlpha(0.5f);
        }

        return convertView;

    }



    @Override
    public int getCount() {
        return sortedOptionValues().size();
    }

    @Override
    public Object getItem(int position) {
        return sortedOptionValues().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
