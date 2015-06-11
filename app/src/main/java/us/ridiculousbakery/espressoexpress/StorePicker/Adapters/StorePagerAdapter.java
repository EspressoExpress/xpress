package us.ridiculousbakery.espressoexpress.StorePicker.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import us.ridiculousbakery.espressoexpress.Model.Store;
import us.ridiculousbakery.espressoexpress.R;


/**
 * Created by bkuo on 6/8/15.
 */
public class StorePagerAdapter extends PagerAdapter {
    // Views that can be reused.
    private final List<View> mDiscardedViews = new ArrayList<View>();
    // Views that are already in use.
    private final SparseArray<View> mBindedViews = new SparseArray<View>();
    private ArrayList<Store> stores;
    private final LayoutInflater mInflator;
    private final int mResourceId;
    private final Context mCtx;
    private PagerItemListener pagerListener;
    private Button btnRequest;
    private Button btnDeliver;


    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == mBindedViews.get(stores.indexOf(object));
    }

    public StorePagerAdapter(Context context) {
        mCtx = context;
        mInflator = LayoutInflater.from(context);
        mResourceId = R.layout.store_item;
    }
    public StorePagerAdapter(Context context, ArrayList<Store> list, PagerItemListener listener) {
        this(context);
        stores = list;
        pagerListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View child = mDiscardedViews.isEmpty() ?
                mInflator.inflate(mResourceId, container, false) :
                mDiscardedViews.remove(0);

        Store data = stores.get(position);
        initView(child, data, position);

        mBindedViews.append(position, child);
        container.addView(child, 0);
        return data;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mBindedViews.get(position);
        if (view != null) {
            mDiscardedViews.add(view);
            mBindedViews.remove(position);
            container.removeView(view);
        }
    }

    private void initView(View v, final Store item, int position) {
        Log.i("ZZZZZZ", "initView: position:" + position + " store item " + item.getName());
        ((TextView)v.findViewById(R.id.tvName)).setText(item.getName());
        ((ImageView)v.findViewById(R.id.ivLogo)).setImageDrawable(mCtx.getResources().getDrawable(item.getLogo()));
        btnRequest = (Button) v.findViewById(R.id.btnRequest);
        btnDeliver = (Button) v.findViewById(R.id.btnDeliver);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerListener.gotoMenu(item);
            }
        });
    }


    public void addAll(ArrayList<Store> stores) {
        this.stores = stores;
        notifyDataSetChanged();
    }

    public interface PagerItemListener {
        public void gotoMenu(Store store);
    }
}
