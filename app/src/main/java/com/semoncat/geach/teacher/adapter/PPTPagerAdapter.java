package com.semoncat.geach.teacher.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.PPTImage;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SemonCat on 2014/8/4.
 */
public class PPTPagerAdapter extends PagerAdapter {

    private PPTsEntity ppTsEntity;

    private PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener;

    public PPTPagerAdapter(PPTsEntity ppTsEntity) {
        this.ppTsEntity = ppTsEntity;
    }

    @Override
    public int getCount() {
        return ppTsEntity.getPptImages().size();
    }


    public PPTImage getItem(int position) {
        return ppTsEntity.getPptImages().get(position);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {


        ViewGroup viewGroup = (ViewGroup) ((LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.adapter_ppt, container,false);

        final PhotoView photoView = (PhotoView) viewGroup.findViewById(R.id.PPT_Content);

        if (onPhotoTapListener != null) {
            photoView.setOnPhotoTapListener(onPhotoTapListener);
        }


        Picasso.with(container.getContext())
                .load(new File(getItem(position).getPPTPathTmp())).into(photoView);

        container.addView(viewGroup);

        return viewGroup;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener) {
        this.onPhotoTapListener = onPhotoTapListener;
    }

}
