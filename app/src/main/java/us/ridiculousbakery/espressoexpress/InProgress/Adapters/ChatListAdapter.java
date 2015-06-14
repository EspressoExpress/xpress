package us.ridiculousbakery.espressoexpress.InProgress.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import us.ridiculousbakery.espressoexpress.Model.ItemOption;
import us.ridiculousbakery.espressoexpress.Model.Message;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/14/15.
 */
public class ChatListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Message> messages;

    private class ViewHolder {
        public TextView text;
    }


    public ChatListAdapter(Context context, ArrayList<Message> messages) {
        this.inflater = LayoutInflater.from(context);
        this.messages = messages;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        String op = (String) getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.option_item, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(op);
        return convertView;

    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}
