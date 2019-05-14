package com.maciek.v2.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maciek.v2.R;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Geezy on 02.08.2018.
 */

public class SlidingImageAdapter extends PagerAdapter {
    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageAdapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);

        String stringUrl = IMAGES.get(position);
        if(IMAGES.get(position).contains("null")){
            stringUrl="/data/user/0/com.maciek.droganowegoczlowieka/files/turysta-dialog-malzenski.jpg";
        }
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL("file://"+stringUrl);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bitmap);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
