package com.semoncat.geach.teacher.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

/**
 * Created by SemonCat on 2014/8/6.
 */
public class HackyNaviDrawer extends GoogleNavigationDrawer {

    private boolean isLocked;

    public HackyNaviDrawer(Context context) {
        super(context);
        isLocked = false;
    }

    public HackyNaviDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    public HackyNaviDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isLocked = false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            } catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLocked) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
