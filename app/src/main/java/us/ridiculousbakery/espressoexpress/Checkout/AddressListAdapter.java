package us.ridiculousbakery.espressoexpress.Checkout;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by mrozelle on 6/14/2015.
 */
public class AddressListAdapter extends ArrayAdapter<Address> {

    private static class ViewHolder {
        TextView tvAddressName;
        TextView tvAddressDetail;
    }

    public AddressListAdapter(Context context, List<Address> addresses) {
        super(context, android.R.layout.simple_list_item_1, addresses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Address address = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.address_item, parent, false);
            viewHolder.tvAddressName = (TextView) convertView.findViewById(R.id.tvAddressName);
            viewHolder.tvAddressDetail = (TextView) convertView.findViewById(R.id.tvAddressDetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvAddressName.setText(address.getAddressLine(0));
        viewHolder.tvAddressDetail.setText(addressToString(address));
        return  convertView;
    }

    private String addressToString(Address address) {
        StringBuilder addressStringBuilder = new StringBuilder();
        String divider = ", ";
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressStringBuilder.append(divider).append(address.getAddressLine(i));
        }
        return addressStringBuilder.toString().substring(divider.length());
    }
}
