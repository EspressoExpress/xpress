package us.ridiculousbakery.espressoexpress.StorePicker.ListPerspective;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;
import us.ridiculousbakery.espressoexpress.StorePicker.StoreElementListener;

/**
 * Created by bkuo on 6/14/15.
 */
public class StandardItemLayout extends ItemStoreLayout{

    private ImageView ivLogo;
    private TextView tvName;
    private Button btnRequest;
    private Button btnDeliver;
    private ViewGroup vgStoreItem;

    public StandardItemLayout(Context context) {
        super(context);
    }

    public StandardItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StandardItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    public void setContent(final Store store, final StoreElementListener listListener){
        if(ivLogo==null)ivLogo = (ImageView) findViewById(R.id.ivLogo);
        if(tvName==null)tvName = (TextView) findViewById(R.id.tvName);
        if(btnRequest==null)btnRequest = (Button) findViewById(R.id.btnRequest);
        
        if(btnDeliver==null) btnDeliver = (Button) findViewById(R.id.btnDeliver);
        if(vgStoreItem==null)vgStoreItem = (ViewGroup) findViewById(R.id.vgStoreItem);
        if(tvName==null) tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(store.getName());
        tvName.setText(store.getName());
        ivLogo.setImageDrawable(getContext().getResources().getDrawable(store.getLogo()));

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.gotoMenu(store);
            }
        });
    }
}
