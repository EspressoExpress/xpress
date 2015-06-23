package us.ridiculousbakery.espressoexpress.StorePicker;

import android.content.Context;
import android.content.res.TypedArray;
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


    public void setContent(final Store store, final StoreElementListener listener){
        if(tvName==null) tvName = (TextView) findViewById(R.id.tvName);
        if(ivBg==null) ivBg = (ImageView) findViewById(R.id.ivBackground);

        tvName.setText(store.getName());
        TypedArray ta= getResources().obtainTypedArray(R.array.store_bgs);
        ivBg.setImageDrawable(ta.getDrawable(store.getBackground()));

        ta.recycle();


    }

}
