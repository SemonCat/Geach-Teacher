package com.semoncat.geach.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.util.CourseFileParser;
import com.semoncat.geach.teacher.util.CourseXMLParser;
import com.semoncat.geach.teacher.util.GeachConfigGetter;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/7/17.
 */
public class CourseGridAdapter extends BaseAdapter {
    private List<CoursesEntity> coursesEntities;

    private boolean[] IsResourceReady;

    public CourseGridAdapter() {
        coursesEntities = new ArrayList<CoursesEntity>();
        IsResourceReady = new boolean[coursesEntities.size()];
    }

    public CourseGridAdapter(List<CoursesEntity> coursesEntities) {
        this.coursesEntities = coursesEntities;
        IsResourceReady = new boolean[coursesEntities.size()];
    }

    public void refresh(List<CoursesEntity> coursesEntities) {
        this.coursesEntities = coursesEntities;
        IsResourceReady = new boolean[coursesEntities.size()];
    }

    public List<CoursesEntity> getCoursesEntities() {
        return coursesEntities;
    }

    @Override
    public int getCount() {
        return coursesEntities.size();
    }

    @Override
    public CoursesEntity getItem(int position) {
        return coursesEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return coursesEntities.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = ((LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_course_grid,
                    parent, false);

            holder = new ViewHolder();
            holder.Cover = (ImageView) convertView.findViewById(R.id.CourseCover);
            holder.Name = (TextView) convertView.findViewById(R.id.CourseName);
            holder.Number = (TextView) convertView.findViewById(R.id.CourseNumber);
            holder.ResourceState = (TextView) convertView.findViewById(R.id.ResourceState);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CoursesEntity coursesEntity = getItem(position);

        holder.Name.setText(coursesEntity.getName());

        String numberString = parent.getContext().getString(R.string.course_student_number, coursesEntity.getStudentsNumber());

        holder.Number.setText(numberString);

        GeachConfigGetter geachConfigGetter = GeachConfigGetter.getInstance(parent.getContext());

        if (geachConfigGetter != null) {
            try {
                boolean IsResourceReady = geachConfigGetter.validateCourseFile(coursesEntity);
                if (IsResourceReady){
                    holder.ResourceState.setText("");
                }

                this.IsResourceReady[position] = IsResourceReady;

            } catch (FileNotFoundException e) {
                holder.ResourceState.setText(parent.getContext().getString(R.string.fileNotFoundException));
                this.IsResourceReady[position] = false;

            } catch (Exception e) {
                holder.ResourceState.setText(parent.getContext().getString(R.string.xmlException));
                this.IsResourceReady[position] = false;
            }
        }


        return convertView;
    }

    public boolean IsResourceReady(int position){
        return this.IsResourceReady[position];
    }

    class ViewHolder {
        ImageView Cover;
        TextView Name;
        TextView Number;
        TextView ResourceState;
    }


}
