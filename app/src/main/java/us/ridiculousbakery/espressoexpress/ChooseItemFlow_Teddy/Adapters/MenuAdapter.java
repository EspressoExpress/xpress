package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/6/15.
 */
public class MenuAdapter extends BaseAdapter {


    private class ViewHolder {
        TextView name;
    }

    private LayoutInflater inflater;
    private StoreMenu storeMenu;

    //================================================================================
    // Constructors
    //================================================================================

    public MenuAdapter(Context context, StoreMenu storeMenu) {
        this.inflater = LayoutInflater.from(context);
        this.storeMenu = storeMenu;
    }

    //================================================================================
    // DataSource
    //================================================================================

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        Item item = storeMenu.getItems().get(i);

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.menu_item, viewGroup, false);
            viewHolder.name = (TextView) view.findViewById(R.id.tvName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(item.getName());

        return view;
    }

    @Override
    public Object getItem(int i) {
        return storeMenu.getItems().get(i);
    }

    @Override
    public int getCount() {
        return storeMenu.getItems().size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
