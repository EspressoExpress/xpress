package us.ridiculousbakery.espressoexpress.NavDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 5/28/15.
 */
public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {


    public NavDrawerListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public NavDrawerListAdapter(Context context, List<NavDrawerItem> objects) {
        super(context, R.layout.drawer_item, objects);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public NavDrawerItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_item, parent, false);
        }
//        (ImageView) convertView.findViewById(R.id.ivIcon);
        TextView tv = (TextView) convertView.findViewById(R.id.tvTitle);
        tv.setText(getItem(position).getTitle());
        ImageView iv = (ImageView) convertView.findViewById(R.id.ivIcon);
        iv.setImageDrawable(getContext().getResources().getDrawable(getItem(position).getIcon()));
//        Picasso.with(getContext()).load(Uri.parse(getItem(position).getIcon())).into(iv);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
