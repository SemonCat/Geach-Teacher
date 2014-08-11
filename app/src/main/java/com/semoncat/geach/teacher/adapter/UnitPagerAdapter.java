package com.semoncat.geach.teacher.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.PPTImage;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.semoncat.geach.teacher.bean.UnitEntity;
import com.semoncat.geach.teacher.bean.Video;
import com.semoncat.geach.teacher.bean.VideosEntity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by SemonCat on 2014/7/18.
 */
public class UnitPagerAdapter extends PagerAdapter {

    public interface OnPagerItemClickListener {
        boolean OnPagerItemTouch(UnitPagerAdapter adapter, View view, int position);

        void OnPagerItemClick(UnitPagerAdapter adapter, View view, int position);
    }

    private static final String TAG = UnitPagerAdapter.class.getName();

    private List<UnitEntity> unitEntities;

    private OnPagerItemClickListener listener;

    public UnitPagerAdapter() {
        unitEntities = new ArrayList<UnitEntity>();
    }

    public UnitPagerAdapter(List<UnitEntity> unitEntities) {
        this.unitEntities = unitEntities;
    }

    public void refresh(List<UnitEntity> unitEntities) {
        this.unitEntities = unitEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return unitEntities.size();
    }


    public UnitEntity getItem(int position) {
        return unitEntities.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View convertView = ((LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_unit,
                container, false);

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener != null) {
                    return listener.OnPagerItemTouch(UnitPagerAdapter.this, v, position);
                }
                return false;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnPagerItemClick(UnitPagerAdapter.this, v, position);
                }
            }
        });
        TextView UnitId = (TextView) convertView.findViewById(R.id.UnitId);
        TextView UnitName = (TextView) convertView.findViewById(R.id.UnitName);
        ImageView UnitCover = (ImageView) convertView.findViewById(R.id.UnitCover);

        UnitEntity unitEntity = getItem(position);

        String unitId = container.getResources()
                .getString(R.string.unit_id_format, unitEntity.getId());

        UnitId.setText(unitId);
        UnitName.setText(unitEntity.getName());

        PPTsEntity ppTsEntity = unitEntity.getPPTsEntity();


        if (ppTsEntity.getPptImages().size() > 0) {
            File pptCover = new File(ppTsEntity.getPptImages().get(0).getPPTPathTmp());
            Picasso.with(container.getContext()).load(pptCover).into(UnitCover);
            UnitCover.setVisibility(View.VISIBLE);
        } else {
            UnitCover.setVisibility(View.GONE);
        }

        container.addView(convertView);

        convertView.setTag(position);

        return convertView;
    }

    public int getPrev(int position) {
        if (position == 0) {
            return unitEntities.size() - 1;
        } else {
            return position - 1;
        }
    }

    public int getNext(int position) {
        if (position == (unitEntities.size() - 1)) {
            return 0;
        } else {
            return position + 1;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    public void setOnPagerItemClickListener(OnPagerItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((View) o);
    }


    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}