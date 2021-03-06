package us.ridiculousbakery.espressoexpress.InProgress.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import us.ridiculousbakery.espressoexpress.ChooseItemFlow_Teddy.ProfileImageHelper;
import us.ridiculousbakery.espressoexpress.DisplayHelper;
import us.ridiculousbakery.espressoexpress.Model.Message;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by teddywyly on 6/14/15.
 */
public class ChatListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Message> messages;
    private String userID;

    private class ViewHolder {
        public TextView text;
        public ImageView imageLeft;
        public ImageView imageRight;
    }


    public ChatListAdapter(Context context, String userID, ArrayList<Message> messages) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.userID = userID;
        this.messages = messages;
    }

    public Context getContext() {
        return context;
    }

    public void clear() {
        this.messages.clear();
    }

    public void addAll(List<Message> messages) {
        this.messages.addAll(messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        Message message = (Message) getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.imageLeft = (ImageView) convertView.findViewById(R.id.ivProfileLeft);
            viewHolder.imageRight = (ImageView) convertView.findViewById(R.id.ivProfileRight);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final  boolean isMe = message.getUserId().equals(userID);

        if (isMe) {
            viewHolder.imageRight.setVisibility(View.VISIBLE);
            viewHolder.imageLeft.setVisibility(View.GONE);
            viewHolder.text.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

        } else {
            viewHolder.imageLeft.setVisibility(View.VISIBLE);
            viewHolder.imageRight.setVisibility(View.GONE);
            viewHolder.text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        final ImageView profileView = isMe ? viewHolder.imageRight : viewHolder.imageLeft;

        Picasso.with(getContext()).load(DisplayHelper.getProfileUrlFromEmail(message.getTargetUserEmail())).fit().transform(ProfileImageHelper.circleTransformation(67)).into(profileView);


        viewHolder.text.setText(message.getText());
        return convertView;

    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}
