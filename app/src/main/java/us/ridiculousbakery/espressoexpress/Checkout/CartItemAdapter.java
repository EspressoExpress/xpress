package us.ridiculousbakery.espressoexpress.Checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/7/2015.
 */
public class CartItemAdapter extends ArrayAdapter<LineItem> {

    private static class ViewHolder {
        TextView tvItemName;
        TextView tvItemOption;
        TextView tvPrice;
    }

    public CartItemAdapter(Context context, List<LineItem> lineItems) {
        super(context, android.R.layout.simple_list_item_1, lineItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LineItem lineItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
            viewHolder.tvItemOption = (TextView) convertView.findViewById(R.id.tvItemOption);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvItemName.setText(lineItem.getItem().getName());
        return convertView;
    }
}
