package us.ridiculousbakery.espressoexpress.StorePicker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Activities.MenuActivity;
import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.StorePickerActivity;

/**
 * Created by bkuo on 6/8/15.
 */
public class StoreListAdapter extends ArrayAdapter<Store> {
    private MapTargetListener mapTargetListener;

    public StoreListAdapter(Context context){
        super(context, R.layout.store_item);
        mapTargetListener=(StorePickerActivity)context;
    }
    public StoreListAdapter(Context context, int resource, List<Store> objects) {
        super(context, resource, objects);
    }
    public void setMapTargetListener(MapTargetListener m){
        mapTargetListener = m;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        final Store  store = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_item, parent, false);
            viewholder = new ViewHolder();
            viewholder.ivLogo = (ImageView) convertView.findViewById(R.id.ivLogo);
            viewholder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewholder.btnRequest = (Button) convertView.findViewById(R.id.btnRequest);
            viewholder.btnDeliver = (Button) convertView.findViewById(R.id.btnDeliver);
            viewholder.vgStoreItem = (ViewGroup) convertView.findViewById(R.id.vgStoreItem);

            convertView.setTag(viewholder);

        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.tvName.setText(store.getName());
        viewholder.ivLogo.setImageDrawable(getContext().getResources().getDrawable(store.getLogo()));

        viewholder.btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MenuActivity.class);
                i.putExtra("menu", new StoreMenu(true));
                i.putExtra("store", store);

                getContext().startActivity(i);
            }
        });
        viewholder.vgStoreItem.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(mapTargetListener!=null){
                            Log.i("ZZZZZZZ", "onNewMapTArget: "+position);
                            mapTargetListener.onMapsRequired();

                            mapTargetListener.onNewMapTarget(position);
                        }

                    }
                }



        );
//        viewholder.btnDeliver.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), TweetDetailActivity.class);
//                i.putExtra("user", tweet.getUser());
//                i.putExtra("tweet", tweet);
//                getContext().startActivity(i);
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
    public interface MapTargetListener{
        public void onNewMapTarget(int index);
        public void onMapsRequired();
    }
}
