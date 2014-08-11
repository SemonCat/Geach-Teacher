package com.semoncat.geach.teacher.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.adapter.StudentAdapter;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.StudentsEntity;
import com.semoncat.geach.teacher.rest.GeachRestService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SemonCat on 2014/8/6.
 */
public class StudentListFragment extends DialogFragment implements Callback<List<StudentsEntity>> {

    private CoursesEntity coursesEntity;

    private GridView studentListGrid;
    private StudentAdapter studentAdapter;

    public static StudentListFragment newInstance(CoursesEntity coursesEntity) {
        StudentListFragment studentListFragment = new StudentListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CoursesEntity.class.getName(), coursesEntity);
        studentListFragment.setArguments(bundle);

        return studentListFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        coursesEntity = getArguments().getParcelable(CoursesEntity.class.getName());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_student_list, null);

        studentListGrid = (GridView) rootView.findViewById(R.id.studentList);

        studentAdapter = new StudentAdapter();
        studentListGrid.setAdapter(studentAdapter);

        GeachRestService.getInstance().getStudentList(coursesEntity.getId(), this);

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }

    @Override
    public void success(List<StudentsEntity> studentsEntities, Response response) {
        studentAdapter.refresh(studentsEntities);
    }

    @Override
    public void failure(RetrofitError error) {

    }

}
