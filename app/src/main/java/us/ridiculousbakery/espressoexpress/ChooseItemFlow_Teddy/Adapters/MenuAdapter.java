package us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.ProfileImageHelper;
import us.ridiculousbakery.espressoexpress.Model.Item;
import us.ridiculousbakery.espressoexpress.Model.StoreMenu;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/6/15.
 */
public class MenuAdapter extends BaseExpandableListAdapter {


    private class ViewHolder {
        TextView name;
        TextView price;
        ImageView image;
    }

    private class GroupViewHolder {
        TextView category;
    }

    private LayoutInflater inflater;
    private Context context;
    private StoreMenu storeMenu;

    public Context getContext() {
        return context;
    }

    //================================================================================
    // Constructors
    //================================================================================

    public MenuAdapter(Context context, StoreMenu storeMenu) {
        this.inflater = LayoutInflater.from(context);
        this.storeMenu = storeMenu;
    }

    //================================================================================
    // DataSource - Children
    //================================================================================


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Item> groupItems = (ArrayList<Item>) storeMenu.getCategories().values().toArray()[groupPosition];
        return groupItems.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        ArrayList<Item> groupItems = (ArrayList<Item>) storeMenu.getCategories().values().toArray()[groupPosition];
        Item item = groupItems.get(childPosition);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.menu_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.tvPrice);
//            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.image.setImageResource(0);
        viewHolder.price.setText("$4-6");
        viewHolder.name.setText(item.getName());

//        if (item.getImageURL() != null) {
//            Picasso.with(getContext()).load(item.getImageURL()).fit().transform(ProfileImageHelper.roundTransformation()).into(viewHolder.image);
//        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Item> groupItems = (ArrayList<Item>) storeMenu.getCategories().values().toArray()[groupPosition];
        return groupItems.size();
    }

    //================================================================================
    // DataSource - Group
    //================================================================================

    @Override
    public Object getGroup(int groupPosition) {
        ArrayList<Item> groupItems = (ArrayList<Item>) storeMenu.getCategories().values().toArray()[groupPosition];
        return groupItems;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;

        String category = (String) storeMenu.getCategories().keySet().toArray()[groupPosition];

        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.menu_group, parent, false);
            viewHolder.category = (TextView) convertView.findViewById(R.id.tvCategory);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        viewHolder.category.setText(category);
        return convertView;    }

    @Override
    public int getGroupCount() {
        return storeMenu.getCategories().values().size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
