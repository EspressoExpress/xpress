package us.ridiculousbakery.espressoexpress.Checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.LineItem;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/23/2015.
 */
public class CartItemAdapterSwipe extends ArraySwipeAdapter<LineItem> {

    private static class ViewHolder {
        TextView tvItemName;
        TextView tvItemOption;
        TextView tvPrice;
        LinearLayout bottom;
        SwipeLayout swipeLayout;
    }

    public CartItemAdapterSwipe(Context context, List<LineItem> lineItems) {
        super(context, android.R.layout.simple_list_item_1, lineItems);
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LineItem lineItem = (LineItem) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
            viewHolder.swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
            viewHolder.bottom = (LinearLayout) convertView.findViewById(R.id.bottom_wrapper);
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
            viewHolder.tvItemOption = (TextView) convertView.findViewById(R.id.tvItemOption);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvItemName.setText(lineItem.getItem().getName());
        viewHolder.tvPrice.setText("$" + StringHelper.priceToString(lineItem.getPrice()));




        //set show mode.
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, convertView.findViewById(R.id.bottom_wrapper));
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        viewHolder.bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
