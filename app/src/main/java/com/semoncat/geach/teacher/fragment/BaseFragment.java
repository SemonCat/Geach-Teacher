package com.semoncat.geach.teacher.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.semoncat.fragmenttransanim.AnimFrameLayout;
import com.semoncat.geach.teacher.MainActivity;

import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

/**
 * Created by SemonCat on 2014/7/15.
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getName();

    protected static final String EMPTY = "";

    private boolean alreadyLoaded;

    protected abstract void init();

    protected abstract void setupView(View rootView);

    protected abstract void setupAdapter();

    protected abstract void setupEvent();

    protected abstract int setupLayout();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null && !alreadyLoaded) {
            alreadyLoaded = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (setupLayout() == 0) {
            throw new IllegalArgumentException("You must overwrite setupLayout() method.");
        }

        AnimFrameLayout animFrameLayout = new AnimFrameLayout(container.getContext());

        if (fitSystemWindows() && getActivity() instanceof MainActivity) {
            SystemBarTintManager.SystemBarConfig config = ((MainActivity) getActivity()).getSystemBarConfig();
            animFrameLayout.setPadding(0, config.getPixelInsetTop(isActionbarVisible()), config.getPixelInsetRight(), config.getPixelInsetBottom());
        }

        View rootView = inflater.inflate(setupLayout(), container, false);

        init();
        setupView(rootView);
        setupAdapter();
        setupEvent();

        animFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        animFrameLayout.addView(rootView);


        animFrameLayout.setFocusableInTouchMode(true);

        animFrameLayout.requestFocus();

        animFrameLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return onBackPressed();
                }
                return false;
            }
        });

        return animFrameLayout;
    }

    protected boolean onBackPressed() {
        return false;
    }

    protected boolean fitSystemWindows() {
        return true;
    }

    protected void finish() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    protected SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    Toast mToast;

    protected void showToast(String Message) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(Message);
        }
        mToast.show();

    }

    protected void hideKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void hideActionbar() {
        if (getActivity() != null && getActivity().getActionBar() != null) {
            getActivity().getActionBar().hide();
        }
    }

    protected void showActionBar() {
        if (getActivity() != null && getActivity().getActionBar() != null) {
            getActivity().getActionBar().show();
        }
    }

    protected ActionBar getActionBar() {
        if (getActivity() != null) {
            return getActivity().getActionBar();
        }

        return null;
    }

    protected boolean isActionbarVisible() {
        if (getActivity() != null && getActivity().getActionBar() != null) {
            return getActivity().getActionBar().isShowing();
        }

        return false;
    }

    public boolean isAlreadyLoaded() {
        return alreadyLoaded;
    }

    protected void setActionBarTitle(String content) {
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(content);
        }
    }

    // This snippet hides the system bars.
    protected void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    protected void showSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    protected void showNaviDrawer() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showNaviDrawer();
        }
    }

    protected void hideNaviDrawer() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideNaviDrawer();
        }
    }

    protected void setDrawerListener(DrawerLayout.DrawerListener drawerListener) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setDrawerListener(drawerListener);
        }
    }

    protected GoogleNavigationDrawer getDrawer() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            return ((MainActivity) getActivity()).getDrawer();
        }

        return null;
    }
}
