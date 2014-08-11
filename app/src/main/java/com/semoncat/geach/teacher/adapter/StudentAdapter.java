package com.semoncat.geach.teacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;
import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.StudentsEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by SemonCat on 2014/1/16.
 */
public class StudentAdapter extends BaseAdapter {

    private static final String TAG = StudentAdapter.class.getName();

    private Handler handler;

    private List<StudentsEntity> mData;


    public StudentAdapter() {
        this(new ArrayList<StudentsEntity>());
    }

    public StudentAdapter(List<StudentsEntity> data) {
        this.mData = data;
        this.handler = new Handler();
    }


    public void refresh(List<StudentsEntity> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void refreshOnUiThread(final List<StudentsEntity> data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mData = data;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public StudentsEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_student,
                    parent, false);

            holder = new ViewHolder();
            holder.Icon = (ImageView) convertView.findViewById(R.id.Icon);
            holder.StudentID = (TextView) convertView.findViewById(R.id.student_id);
            holder.Name = (TextView) convertView.findViewById(R.id.name);
            holder.Department = (TextView) convertView.findViewById(R.id.department);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StudentsEntity mStudent = this.getItem(position);
        holder.Name.setText(mStudent.getName());
        holder.StudentID.setText(mStudent.getStudentId());
        holder.Department.setText(mStudent.getDepartment());


        String URL = mStudent.getPhoto();
        if (URL!=null && URL.startsWith("http")) {
            //imageLoader.displayImage(URL,holder.Icon,options);
            Picasso.with(parent.getContext()).load(URL).resize(100, 100).into(holder.Icon);
        } else {
            holder.Icon.setImageResource(R.drawable.ic_default_head);
        }

        return convertView;
    }


    class ViewHolder {
        ImageView Icon;
        TextView Name;
        TextView StudentID;
        TextView Department;
    }
}
