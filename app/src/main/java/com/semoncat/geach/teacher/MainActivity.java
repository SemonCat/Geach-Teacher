package com.semoncat.geach.teacher;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.fragment.LoginFragment;
import com.semoncat.geach.teacher.rest.GeachRestService;

import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

import retrofit.RetrofitError;

public class MainActivity extends Activity {

    private SystemBarTintManager.SystemBarConfig config;

    private ActionBarDrawerToggle drawerToggle;
    private GoogleNavigationDrawer mDrawer;

    private DrawerLayout.DrawerListener drawerListener;

    public final static int NAVI_MENU_HOME = 1;
    public final static int NAVI_MENU_SCHOOL = 2;
    public final static int NAVI_MENU_STUDENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        setSystemBarTint();

        setupDrawer();

        getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
    }

    private void setSystemBarTint() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        config = tintManager.getConfig();
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.statusbar_bg);
    }

    public SystemBarTintManager.SystemBarConfig getSystemBarConfig() {
        return config;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void setupDrawer() {
        /*
         * We get the GoogleNavigationDrawer object
         * in order to allow further method usage
         */
        mDrawer = (GoogleNavigationDrawer) findViewById(R.id.navi_drawer);

        /**Header View**/

        View headerView = ((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_navi_header, null);


        headerView.setPadding(0,
                config.getPixelInsetTop(getActionBar() != null && getActionBar().isShowing()),
                config.getPixelInsetRight(),
                config.getPixelInsetBottom());


        mDrawer.setMenuHeader(headerView, false);


        /*
         * We get the drawerToggle object order to
         * allow showing the NavigationDrawer icon
         */
        drawerToggle = new ActionBarDrawerToggle(this,
                mDrawer,
                R.drawable.ic_navigation_drawer,
                R.string.app_name,
                R.string.app_name) {


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (drawerListener != null) {
                    drawerListener.onDrawerSlide(drawerView, slideOffset);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (drawerListener != null) {
                    drawerListener.onDrawerStateChanged(newState);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (drawerListener != null) {
                    drawerListener.onDrawerOpened(drawerView);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerListener != null) {
                    drawerListener.onDrawerClosed(drawerView);
                }
            }
        };


        mDrawer.setDrawerListener(drawerToggle); //Attach the DrawerListener

        hideNaviDrawer();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerMenuOpen()){
            mDrawer.closeDrawerMenu();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Declare the behaviour of clicking at the
         * application icon, opening and closing the drawer
         */
        if (item.getItemId() == android.R.id.home) {
            if (mDrawer != null) {
                if (mDrawer.isDrawerMenuOpen()) {
                    mDrawer.closeDrawerMenu();
                } else {
                    mDrawer.openDrawerMenu();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNaviDrawer() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public void hideNaviDrawer() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    public GoogleNavigationDrawer getDrawer(){
        return mDrawer;
    }

    public void setDrawerListener(DrawerLayout.DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }
}
