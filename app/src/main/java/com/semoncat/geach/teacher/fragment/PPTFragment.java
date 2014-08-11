package com.semoncat.geach.teacher.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.martinappl.components.ui.containers.HorizontalList;
import com.melnykov.fab.FloatingActionButton;
import com.nineoldandroids.animation.Animator;
import com.semoncat.geach.teacher.MainActivity;
import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.adapter.PPTPagerAdapter;
import com.semoncat.geach.teacher.adapter.PPTPreviewAdapter;
import com.semoncat.geach.teacher.adapter.VideoListAdapter;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.semoncat.geach.teacher.bean.UnitEntity;
import com.semoncat.geach.teacher.bean.VideosEntity;
import com.semoncat.geach.teacher.transform.BlurTransform;
import com.semoncat.geach.teacher.ui.HackyViewPager;
import com.semoncat.geach.teacher.util.ImageBlurrer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

import java.io.File;

import javax.xml.transform.Transformer;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SemonCat on 2014/8/4.
 */
public class PPTFragment extends BaseFragment implements PhotoViewAttacher.OnPhotoTapListener {

    private static final String TAG = PPTFragment.class.getName();

    public static final String UNIT_ENTITY = UnitEntity.class.getName();

    private static final String PPTPREFIX = "pptfragment_";

    private static final int PPT_CONTROL_BUTTON_FADEIN_OUT_TIME = 300;

    private static final int SHOW_AND_HIDE_ANIM_TIME = 500;

    private static final int HIDE_FUNCTION_UI_TIMEOUT = 3000;

    private UnitEntity unitEntity;

    private HackyViewPager PPTViewPager;

    private View PPTPreviewContainer, VideoListContainer;

    private HListView PPTPreview, VideoList;

    private ImageButton PPTPrev, PPTNext;

    private Button toggleToPPT, toggleToVideo;

    private Handler handler;

    private Runnable HideFunctionUICallback;

    private FloatingActionButton ActionMenuButton;

    private PPTPagerAdapter pptPagerAdapter;

    private boolean NeedHideActionBar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PPTViewPager.setCurrentItem(getPPTPage());
        updatePPTControlButtonState();
    }

    @Override
    protected boolean onBackPressed() {
        if (isActionbarVisible()) {
            hideFunctionUI();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected void init() {
        NeedHideActionBar = false;

        handler = new Handler();

        unitEntity = getArguments().getParcelable(UNIT_ENTITY);

        HideFunctionUICallback = new Runnable() {
            @Override
            public void run() {
                if (isVisible()) {
                    hideFunctionUI();
                }
            }
        };

        setHasOptionsMenu(true);

        setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View view) {
                NeedHideActionBar = !isActionbarVisible();
                showActionBar();
            }

            @Override
            public void onDrawerClosed(View view) {
                if (NeedHideActionBar && isVisible()) {
                    hideActionbar();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ppt, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.closeMenu:
                hideFunctionUI();
                getDrawer().closeDrawerMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupView(View rootView) {
        setActionBarTitle(unitEntity.getName());

        PPTPreviewContainer = rootView.findViewById(R.id.PPTPreviewContainer);
        VideoListContainer = rootView.findViewById(R.id.VideoListContainer);
        PPTViewPager = (HackyViewPager) rootView.findViewById(R.id.PPTViewPager);
        PPTPreview = (HListView) rootView.findViewById(R.id.PPTPreview);
        VideoList = (HListView) rootView.findViewById(R.id.VideoList);
        VideoList.setEmptyView(rootView.findViewById(R.id.VideoListEmptyView));
        PPTPrev = (ImageButton) rootView.findViewById(R.id.PPT_Prev);
        PPTNext = (ImageButton) rootView.findViewById(R.id.PPT_Next);

        ActionMenuButton = (FloatingActionButton) rootView.findViewById(R.id.ActionMenuButton);

        toggleToPPT = (Button) rootView.findViewById(R.id.toggleToPPT);
        toggleToVideo = (Button) rootView.findViewById(R.id.toggleToVideo);

        toggleToPPT();

    }

    @Override
    protected void setupAdapter() {
        PPTsEntity ppTsEntity = unitEntity.getPPTsEntity();

        if (ppTsEntity != null) {
            pptPagerAdapter = new PPTPagerAdapter(ppTsEntity);
            PPTViewPager.setAdapter(pptPagerAdapter);
            pptPagerAdapter.setOnPhotoTapListener(this);

            PPTPreviewAdapter pptPreviewAdapter = new PPTPreviewAdapter(ppTsEntity);
            PPTPreview.setAdapter(pptPreviewAdapter);

        }

        VideosEntity videosEntity = unitEntity.getVideosEntity();

        if (videosEntity != null) {
            VideoListAdapter videoListAdapter = new VideoListAdapter(videosEntity);
            VideoList.setAdapter(videoListAdapter);
        }
    }

    @Override
    protected void setupEvent() {
        ActionMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFunctionUI();
            }
        });

        PPTPreview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PPTViewPager.setCurrentItem(position, true);
                hideFunctionUI();
            }
        });

        VideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenMovie(unitEntity.getVideosEntity().getVideos().get(i).getVideoPath());
            }
        });

        PPTViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {
                PPTPreview.setSelection(position);
                updatePPTControlButtonState();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        PPTPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PPTViewPager.setCurrentItem(PPTViewPager.getCurrentItem() - 1, true);
            }
        });

        PPTNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PPTViewPager.setCurrentItem(PPTViewPager.getCurrentItem() + 1, true);
            }
        });

        PPTPrev.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PPTViewPager.setCurrentItem(0, true);
                return false;
            }
        });

        PPTNext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PPTViewPager.setCurrentItem(PPTViewPager.getAdapter().getCount() - 1, true);
                return false;
            }
        });

        toggleToPPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleToPPT();
            }
        });

        toggleToVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleToVideo();
            }
        });

    }

    @Override
    protected int setupLayout() {
        return R.layout.fragment_ppt;
    }

    @Override
    public void onPhotoTap(View view, float v, float v2) {
        if (isActionbarVisible()) {
            hideFunctionUI();
        } else {
            showFunctionUI();
        }
    }

    private void showFunctionUI() {
        showActionBar();
        PPTPreviewContainer.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.FadeIn)
                .duration(SHOW_AND_HIDE_ANIM_TIME)
                .playOn(PPTPreviewContainer);

        ActionMenuButton.hide();

    }

    private void hideFunctionUI() {
        hideActionbar();

        YoYo.with(Techniques.FadeOut)
                .duration(SHOW_AND_HIDE_ANIM_TIME)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!isActionbarVisible()) {
                            PPTPreviewContainer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(PPTPreviewContainer);

        ActionMenuButton.show();

    }

    private void autoHideFunctionUI() {
        handler.removeCallbacks(HideFunctionUICallback);
        handler.postDelayed(HideFunctionUICallback, HIDE_FUNCTION_UI_TIMEOUT);
    }

    private void updatePPTControlButtonState() {

        if (PPTViewPager.getCurrentItem() == PPTViewPager.getAdapter().getCount() - 1) {
            Log.d(TAG, "Max");
            fadein(PPTPrev);
            fadeout(PPTNext);
        } else if (PPTViewPager.getCurrentItem() == 0) {
            Log.d(TAG, "Min");
            fadein(PPTNext);
            fadeout(PPTPrev);
        } else {
            Log.d(TAG, "Both");
            fadein(PPTPrev);
            fadein(PPTNext);
        }
    }

    private void OpenMovie(String Path) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Path));
            intent.setDataAndType(Uri.parse(Path), "video/mp4");
            startActivity(intent);
        } catch (ActivityNotFoundException mActivityNotFoundException) {
            Toast.makeText(getActivity(), "找不到播放器", Toast.LENGTH_SHORT).show();
        }
    }

    private void fadeout(final View view) {

        if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
            return;
        }

        YoYo.with(Techniques.FadeOut)
                .duration(PPT_CONTROL_BUTTON_FADEIN_OUT_TIME)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(view);
    }

    private void fadein(final View view) {

        if (view.getVisibility() == View.VISIBLE) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(PPT_CONTROL_BUTTON_FADEIN_OUT_TIME)
                .playOn(view);
    }

    private void toggleToPPT() {
        toggleToPPT.setSelected(true);
        toggleToVideo.setSelected(false);

        VideoListContainer.setVisibility(View.GONE);
        PPTPreview.setVisibility(View.VISIBLE);
    }

    private void toggleToVideo() {
        toggleToPPT.setSelected(false);
        toggleToVideo.setSelected(true);

        VideoListContainer.setVisibility(View.VISIBLE);
        PPTPreview.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        savePPTPage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void savePPTPage() {
        int page = PPTViewPager.getCurrentItem();
        getSharedPreferences().edit().putInt(PPTPREFIX + unitEntity.getId(), page).commit();
    }

    private int getPPTPage() {
        return getSharedPreferences().getInt(PPTPREFIX + unitEntity.getId(), 0);
    }
}
