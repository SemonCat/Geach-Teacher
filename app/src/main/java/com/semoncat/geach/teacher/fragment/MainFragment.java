package com.semoncat.geach.teacher.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.adapter.CourseGridAdapter;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.UsersTeacherEntity;
import com.semoncat.geach.teacher.rest.GeachRestService;
import com.semoncat.geach.teacher.ui.SecretTextView;
import com.semoncat.geach.teacher.util.ConstantUtil;
import com.semoncat.geach.teacher.util.CourseFileParser;
import com.semoncat.geach.teacher.util.CourseXMLParser;
import com.semoncat.geach.teacher.util.GeachConfigGetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by SemonCat on 2014/7/16.
 */
public class MainFragment extends BaseFragment implements Callback<List<CoursesEntity>>, AdapterView.OnItemClickListener {

    private static final String TAG = MainFragment.class.getName();

    private static final int WELCOME_ANIM_TIME = 1500;
    private static final int WELCOME_ANIM_TIMEOUT = 1000;

    private static final int RESOURCE_NOT_READY_ANIM_TIME = 1000;

    private static final int COURSE_SCALE_OUT_ANIM_TIME = 500;

    private GridView CourseGrid;
    private CourseGridAdapter courseGridAdapter;
    private SecretTextView WelcomeView;
    private UsersTeacherEntity usersTeacherEntity;
    private Handler handler;

    private RefreshActionItem RefreshCourse;

    private GeachRestService geachRestService;

    private GeachConfigGetter geachConfigGetter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            List<CoursesEntity> coursesEntities = savedInstanceState.getParcelableArrayList(CoursesEntity.class.getName());
            if (coursesEntities != null) {
                refreshAdapter(coursesEntities);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.refresh_button);
        RefreshCourse = (RefreshActionItem) item.getActionView();
        RefreshCourse.setMenuItem(item);
        RefreshCourse.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
        RefreshCourse.setRefreshActionListener(new RefreshActionItem.RefreshActionListener() {
            @Override
            public void onRefreshButtonClick(RefreshActionItem refreshActionItem) {

                updateCourse();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CoursesEntity.class.getName(), new ArrayList<CoursesEntity>(courseGridAdapter.getCoursesEntities()));
    }

    @Override
    protected void init() {
        handler = new Handler();

        geachRestService = GeachRestService.getInstance();
        usersTeacherEntity = getArguments().getParcelable(UsersTeacherEntity.class.getName());

        geachConfigGetter = GeachConfigGetter.getInstance(getActivity());

        updateCourse();

        String actionbarTitle = getString(R.string.teacher_courses,
                usersTeacherEntity.getName(),
                usersTeacherEntity.getTitle());

        setActionBarTitle(actionbarTitle);
    }

    @Override
    protected void setupView(View rootView) {
        WelcomeView = (SecretTextView) rootView.findViewById(R.id.WelcomeText);

        WelcomeView.setText(
                getString(R.string.welcome_back_string, usersTeacherEntity.getName(), usersTeacherEntity.getTitle()));

        WelcomeView.setDuration(WELCOME_ANIM_TIME);


        CourseGrid = (GridView) rootView.findViewById(R.id.CourseGrid);

        if (!isAlreadyLoaded()) {
            showWelcomeAnim();
        } else {
            showCourseGrid(true);
        }
    }

    @Override
    protected void setupAdapter() {

    }


    @Override
    protected void setupEvent() {
        CourseGrid.setOnItemClickListener(this);
    }

    @Override
    protected int setupLayout() {
        return R.layout.fragment_main;
    }

    private void updateCourse() {
        if (RefreshCourse != null) {
            RefreshCourse.showProgress(true);
        }

        geachRestService.getCourses(MainFragment.this);
    }

    @Override
    public void success(List<CoursesEntity> coursesEntities, Response response) {
        refreshAdapter(coursesEntities);

        if (RefreshCourse != null) {
            RefreshCourse.showProgress(false);
        }
    }

    @Override
    public void failure(RetrofitError error) {

    }

    private void refreshAdapter(List<CoursesEntity> coursesEntities) {
        courseGridAdapter = new CourseGridAdapter(coursesEntities);
        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(courseGridAdapter);
        animAdapter.setAbsListView(CourseGrid);
        CourseGrid.setAdapter(animAdapter);

    }

    private void showWelcomeAnim() {
        WelcomeView.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WelcomeView.hide();
            }
        }, WELCOME_ANIM_TIMEOUT);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showCourseGrid(true);
            }
        }, WELCOME_ANIM_TIME + WELCOME_ANIM_TIMEOUT);
    }

    private void showCourseGrid(boolean isVisible) {
        CourseGrid.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

        if (courseGridAdapter.IsResourceReady(position)) {
            CoursesEntity coursesEntity = (CoursesEntity) parent.getAdapter().getItem(position);

            toCourseFragment(coursesEntity);

        } else {
            YoYo.with(Techniques.Swing)
                    .duration(RESOURCE_NOT_READY_ANIM_TIME)
                    .playOn(view);
        }

    }

    private void toCourseFragment(CoursesEntity coursesEntity) {

        CourseFragment courseFragment = new CourseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CoursesEntity.class.getName(), coursesEntity);
        courseFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .addToBackStack(MainFragment.class.getName())
                .setCustomAnimations(R.anim.scalexy_enter,
                        R.anim.scalexy_exit,
                        R.anim.scalexy_enter,
                        R.anim.scalexy_exit)
                .replace(R.id.container, courseFragment)
                .commit();
    }
}
