package com.semoncat.geach.teacher.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.martinappl.components.ui.containers.HorizontalList;
import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.adapter.UnitAdapter;
import com.semoncat.geach.teacher.adapter.UnitPagerAdapter;
import com.semoncat.geach.teacher.bean.CourseFile;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.UnitEntity;
import com.semoncat.geach.teacher.util.ConstantUtil;
import com.semoncat.geach.teacher.util.CourseFileParser;
import com.semoncat.geach.teacher.util.CourseXMLParser;
import com.semoncat.geach.teacher.util.EndlessViewPager.EndlessViewPager;
import com.semoncat.geach.teacher.util.GeachConfigGetter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SemonCat on 2014/7/18.
 */
public class CourseFragment extends BaseFragment implements UnitPagerAdapter.OnPagerItemClickListener{

    private static final String TAG = CourseFragment.class.getName();

    private static final int UNIT_VIEW_SLIDE_ANIM_TIME = 500;

    private ViewGroup ViewPagerContainer;

    private EndlessViewPager UnitViewPager;

    private ProgressBar UnitDataLoadingBar;

    private GeachConfigGetter geachConfigGetter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CoursesEntity coursesEntity = getArguments().getParcelable(CoursesEntity.class.getName());

        geachConfigGetter = GeachConfigGetter.getInstance(getActivity());

        geachConfigGetter.getCourseFile(coursesEntity, new CourseFileParser.OnCourseFileReady() {
            @Override
            public void OnSuccess(CourseFile courseFile) {
                updateUnit(courseFile);
                UnitDataLoadingBar.setVisibility(View.GONE);
            }

            @Override
            public void OnFail(Exception e) {
                e.printStackTrace();
                UnitDataLoadingBar.setVisibility(View.GONE);
            }
        });

        setActionBarTitle(coursesEntity.getName());
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setupView(View rootView) {
        UnitViewPager = (EndlessViewPager) rootView.findViewById(R.id.UnitViewPager);
        UnitViewPager.setOffscreenPageLimit(5);

        ViewPagerContainer = (ViewGroup) rootView.findViewById(R.id.ViewPagerContainer);

        UnitDataLoadingBar = (ProgressBar) rootView.findViewById(R.id.UnitDataLoadingBar);
    }

    @Override
    protected void setupAdapter() {

    }

    private void updateUnit(CourseFile courseFile) {
        UnitPagerAdapter unitAdapter = new UnitPagerAdapter(courseFile.getUnitEntities());
        UnitViewPager.setAdapter(unitAdapter);
        unitAdapter.setOnPagerItemClickListener(this);
    }

    @Override
    protected void setupEvent() {
        ViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
                return UnitViewPager.dispatchTouchEvent(event);
            }
        });


    }

    @Override
    protected int setupLayout() {
        return R.layout.fragment_course;
    }

    @Override
    public void OnPagerItemClick(UnitPagerAdapter adapter, View view, int position) {

        int prevPosition = adapter.getPrev(position);
        int nextPosition = adapter.getNext(position);


        for (int i = 0; i < UnitViewPager.getChildCount(); i++) {
            final View mView = UnitViewPager.getChildAt(i);

            ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0f, mView.getWidth());
            valueAnimator.setDuration(UNIT_VIEW_SLIDE_ANIM_TIME);

            if (mView.getTag().equals(prevPosition)) {
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mView.setTranslationX((-(Float) animation.getAnimatedValue()));
                    }
                });
                valueAnimator.start();
            } else if (mView.getTag().equals(nextPosition)) {

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mView.setTranslationX(((Float) animation.getAnimatedValue()));
                    }
                });
                valueAnimator.start();
            }
        }
    }
}
