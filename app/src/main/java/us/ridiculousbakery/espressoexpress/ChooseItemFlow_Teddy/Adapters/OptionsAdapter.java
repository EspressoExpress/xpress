package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.ItemOption;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/13/15.
 */
public class OptionsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ItemOption options;

    private class ViewHolder {
        public TextView name;
    }

    public OptionsAdapter(Context context, ItemOption options) {
        this.inflater = LayoutInflater.from(context);
        this.options = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

//        ArrayList<Item> groupItems = (ArrayList<Item>) storeMenu.getCategories().values().toArray()[groupPosition];
//        Item item = groupItems.get(childPosition);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.option_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.name.setText(item.getName());
        return convertView;

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return "Hello";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
