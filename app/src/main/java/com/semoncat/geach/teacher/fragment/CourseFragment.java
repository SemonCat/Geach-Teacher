package com.semoncat.geach.teacher.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.semoncat.geach.teacher.MainActivity;
import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.adapter.UnitPagerAdapter;
import com.semoncat.geach.teacher.bean.CourseFile;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.UnitEntity;
import com.semoncat.geach.teacher.util.CourseFileParser;
import com.semoncat.geach.teacher.ui.EndlessViewPager.EndlessViewPager;
import com.semoncat.geach.teacher.util.GeachConfigGetter;

import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

/**
 * Created by SemonCat on 2014/7/18.
 */
public class CourseFragment extends BaseFragment implements UnitPagerAdapter.OnPagerItemClickListener {

    private static final String TAG = CourseFragment.class.getName();

    private static final String COURSE_PREFIX = "course_";

    public static int SCHOOL_FRAGMENT_ID;

    private static final int UNIT_VIEW_SLIDE_ANIM_TIME = 500;

    private static final int RESOURCE_NOT_READY_ANIM_TIME = 1000;

    private ViewGroup ViewPagerContainer;

    private ViewPager UnitViewPager;

    private View UnitDataLoadingBar;

    private GeachConfigGetter geachConfigGetter;

    private CoursesEntity coursesEntity;

    private Handler handler;

    private boolean IsAnim;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showActionBar();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSelectedCourse();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        coursesEntity = getArguments().getParcelable(CoursesEntity.class.getName());

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

        handler = new Handler();

        getDrawer().check(MainActivity.NAVI_MENU_SCHOOL);

        getDrawer().setOnNavigationSectionSelected(new GoogleNavigationDrawer.OnNavigationSectionSelected() {
            @Override
            public void onSectionSelected(View view, int position, long id) {
                switch (position) {
                    case MainActivity.NAVI_MENU_HOME:

                        if (MainFragment.HOME_FRAGMENT_ID != -1) {

                            getFragmentManager().popBackStack(MainFragment.HOME_FRAGMENT_ID, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        }

                        break;

                    case MainActivity.NAVI_MENU_SCHOOL:

                        if (SCHOOL_FRAGMENT_ID != -1) {

                            getFragmentManager().popBackStack(SCHOOL_FRAGMENT_ID, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        }

                        break;

                    case MainActivity.NAVI_MENU_STUDENT:

                        StudentListFragment.newInstance(coursesEntity).show(getFragmentManager(), null);

                        break;
                }

            }
        });
        showNaviDrawer();
    }


    @Override
    protected void init() {
        IsAnim = false;
    }

    @Override
    protected void setupView(View rootView) {
        UnitViewPager = (ViewPager) rootView.findViewById(R.id.UnitViewPager);
        UnitViewPager.setOffscreenPageLimit(5);

        ViewPagerContainer = (ViewGroup) rootView.findViewById(R.id.ViewPagerContainer);

        UnitDataLoadingBar = rootView.findViewById(R.id.UnitDataLoadingBar);
    }

    @Override
    protected void setupAdapter() {

    }

    private void updateUnit(CourseFile courseFile) {

        UnitPagerAdapter unitAdapter = new UnitPagerAdapter(courseFile.getUnitEntities());
        UnitViewPager.setAdapter(unitAdapter);
        unitAdapter.setOnPagerItemClickListener(this);
        UnitViewPager.setCurrentItem(getSelectedCourse());
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
    public boolean OnPagerItemTouch(UnitPagerAdapter adapter, View view, int position) {
        if (position != UnitViewPager.getCurrentItem()) {
            return true;
        }
        return false;
    }

    @Override
    public void OnPagerItemClick(final UnitPagerAdapter adapter, View view, final int position) {

        final UnitEntity unitEntity = adapter.getItem(position);

        if (IsAnim) {
            return;
        }

        if (unitEntity.getPPTsEntity().getPptImages().size() <= 0) {
            YoYo.with(Techniques.Swing)
                    .duration(RESOURCE_NOT_READY_ANIM_TIME)
                    .playOn(view);
            return;
        }

        int prevPosition = adapter.getPrev(position);
        int nextPosition = adapter.getNext(position);


        if (UnitViewPager.getAdapter().getCount() > 2) {
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

        hideActionbar();

        IsAnim = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toPPTFragment(unitEntity);
            }
        }, UNIT_VIEW_SLIDE_ANIM_TIME);

    }

    private void toPPTFragment(UnitEntity unitEntity) {
        Bundle bundle = new Bundle();

        bundle.putParcelable(PPTFragment.UNIT_ENTITY, unitEntity);

        PPTFragment pptFragment = new PPTFragment();
        pptFragment.setArguments(bundle);

        SCHOOL_FRAGMENT_ID = getFragmentManager().beginTransaction()
                .addToBackStack(CourseFragment.class.getName())
                .setCustomAnimations(
                        android.R.animator.fade_in, android.R.animator.fade_out,
                        android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, pptFragment)
                .commit();
    }

    private void saveSelectedCourse() {
        int position = UnitViewPager.getCurrentItem();
        getSharedPreferences().edit().putInt(COURSE_PREFIX + coursesEntity.getId(), position).commit();
    }

    private int getSelectedCourse() {
        return getSharedPreferences().getInt(COURSE_PREFIX + coursesEntity.getId(), 0);
    }
}
