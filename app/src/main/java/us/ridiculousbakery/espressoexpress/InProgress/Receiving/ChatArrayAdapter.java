package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

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
 * Created by teddywyly on 6/14/15.
 */
public class ChatArrayAdapter extends ArrayAdapter<Message> {

    private LayoutInflater inflater;
    private Context context;
    private String userID;


    public ChatArrayAdapter(Context context,  List objects) {
        super(context, R.layout.chat_item, objects);
    }

    private class ViewHolder {
        public TextView text;
        public ImageView ivProfile;
    }


    public Context getContext() {
        return context;
    }
     private int[] layouts = new int[]{R.layout.sender_chat_item, R.layout.receiver_chat_item};
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        Message message = (Message) getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(layouts[getItemViewType(position)], parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        Picasso.with(getContext()).load(DisplayHelper.getProfileUrlFromEmail(message.getTargetUserEmail())).fit().transform(ProfileImageHelper.circleTransformation(67)).into(profileView);


        viewHolder.text.setText(message.getMessage());
        return convertView;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getMessage_type();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }



    //    @Override
//    public int getCount() {
//        return messages.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return messages.get(position);
//    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }



}
