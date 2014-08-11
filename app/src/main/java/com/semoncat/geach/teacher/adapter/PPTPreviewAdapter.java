package com.semoncat.geach.teacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.PPTImage;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by SemonCat on 2014/4/16.
 */
public class PPTPreviewAdapter extends BaseAdapter {

    private PPTsEntity ppTsEntity;

    public PPTPreviewAdapter(PPTsEntity ppTsEntity) {
        this.ppTsEntity = ppTsEntity;
    }

    @Override
    public int getCount() {
        return ppTsEntity.getPptImages().size();
    }

    @Override
    public PPTImage getItem(int position) {
        return ppTsEntity.getPptImages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = ((LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_ppt_preview,
                    parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.PPT_Preview_Image = (ImageView) convertView.findViewById(R.id.PPT_Preview_Image);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        String ImagePath = getItem(position).getPPTPathTmp();

        Picasso.with(parent.getContext())
                .load(new File(ImagePath))
                .resize(200, 200)
                .into(mViewHolder.PPT_Preview_Image);

        return convertView;
    }

    class ViewHolder {
        ImageView PPT_Preview_Image;
    }
}
