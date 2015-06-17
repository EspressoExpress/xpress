package us.ridiculousbakery.espressoexpress.StorePicker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import us.ridiculousbakery.espressoexpress.Model.Store;

/**
 * Created by bkuo on 6/14/15.
 */
public abstract class ItemStoreLayout extends RelativeLayout{
    public ItemStoreLayout(Context context) {
        super(context);
    }

    public ItemStoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemStoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    abstract public void setContent(Store store, StoreElementListener listener);
}
