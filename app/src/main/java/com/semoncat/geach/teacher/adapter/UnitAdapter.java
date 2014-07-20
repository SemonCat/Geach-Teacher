package com.semoncat.geach.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.UnitEntity;

import java.util.List;

/**
 * Created by SemonCat on 2014/7/18.
 */
public class UnitAdapter extends BaseAdapter{

    private List<UnitEntity> unitEntities;

    public UnitAdapter() {
    }

    public UnitAdapter(List<UnitEntity> unitEntities) {
        this.unitEntities = unitEntities;
    }

    @Override
    public int getCount() {
        return unitEntities.size();
    }

    @Override
    public UnitEntity getItem(int position) {
        return unitEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return unitEntities.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_unit,
                    parent, false);

            holder = new ViewHolder();
            holder.UnitName = (TextView) convertView.findViewById(R.id.UnitName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UnitEntity unitEntity = getItem(position);

        holder.UnitName.setText(unitEntity.getName());

        return convertView;
    }

    class ViewHolder {

        TextView UnitName;
    }
}
