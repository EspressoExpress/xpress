package us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;

/**
 * Created by bkuo on 6/14/15.
 */
public class FancyItemLayout extends ItemStoreLayout {

    private TextView tvName;
    private ImageView ivBg;

    public FancyItemLayout(Context context) {
        super(context);
    }

    public FancyItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FancyItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setContent(Store store, StoreListAdapter.ListItemListener l){
        if(tvName==null) tvName = (TextView) findViewById(R.id.tvName);
        if(ivBg==null) ivBg = (ImageView) findViewById(R.id.ivBackground);

        tvName.setText(store.getName());
        ivBg.setImageResource(store.getBackground());
//        setBackground(getResources().getDrawable(store.getBackground()));
        //        viewholder.ivLogo.setImageDrawable(getContext().getResources().getDrawable(store.getLogo()));

    }
}
