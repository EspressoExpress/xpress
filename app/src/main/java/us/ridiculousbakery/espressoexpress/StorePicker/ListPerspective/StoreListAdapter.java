package us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/8/15.
 */
public class StoreListAdapter extends ArrayAdapter<Store> {
    private ListItemListener listListener;

    public StoreListAdapter(Context context, ArrayList<Store> stores, ListItemListener listener){
        super(context, R.layout.standard_store_item, stores);
        listListener =listener;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        final Store  store = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fancy_store_item, parent, false);

//            viewholder = new ViewHolder();
//            viewholder.ivLogo = (ImageView) convertView.findViewById(R.id.ivLogo);
//            viewholder.tvName = (TextView) convertView.findViewById(R.id.tvName);
//            viewholder.btnRequest = (Button) convertView.findViewById(R.id.btnRequest);
//            viewholder.btnDeliver = (Button) convertView.findViewById(R.id.btnDeliver);
//            viewholder.vgStoreItem = (ViewGroup) convertView.findViewById(R.id.vgStoreItem);
//            convertView.setTag(viewholder);


        } else {
//            viewholder = (ViewHolder) convertView.getTag();
        }
//        viewholder.tvName.setText(store.getName());
//        viewholder.ivLogo.setImageDrawable(getContext().getResources().getDrawable(store.getLogo()));
        ((ItemStoreLayout)convertView).setContent(store, listListener);
//        viewholder.btnRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listListener.gotoMenu(store);
//            }
//        });


//        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewholder.ivProfileImage);
//
        return convertView;
    }

    private class ViewHolder {
        public ImageView ivLogo;
        public TextView tvName;
        public ViewGroup vgStoreItem;
        public Button btnDeliver;
        public Button btnRequest;
    }
    public interface ListItemListener {

        public void gotoMenu(Store store);
    }


}
