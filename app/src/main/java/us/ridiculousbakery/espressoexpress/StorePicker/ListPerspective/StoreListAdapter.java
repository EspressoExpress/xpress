package us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.ItemStoreLayout;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;

/**
 * Created by bkuo on 6/8/15.
 */
public class StoreListAdapter extends ArrayAdapter<Store> {
    private StoreElementListener listListener;

    public StoreListAdapter(Context context, List<Store> stores, StoreElementListener listener){
        super(context, R.layout.fancy_store_item, stores);
        listListener =listener;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         Store store = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fancy_store_item, parent, false);
        }
        ((ItemStoreLayout)convertView).setContent(store, listListener);
        (convertView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.onListStoreElementClicked(position);

            }
        });

        return convertView;
    }


}
