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
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.semoncat.fragmenttransanim.AnimFrameLayout;

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

        View rootView = inflater.inflate(setupLayout(), container, false);

        init();
        setupView(rootView);
        setupAdapter();
        setupEvent();

        animFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        animFrameLayout.addView(rootView);

        return animFrameLayout;
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

    private void hideActionbar(){
        if (getActivity()!=null && getActivity().getActionBar()!=null){
            getActivity().getActionBar().hide();
        }
    }

    private void showActionBar(){
        if (getActivity()!=null && getActivity().getActionBar()!=null){
            getActivity().getActionBar().hide();
        }
    }

    public boolean isAlreadyLoaded() {
        return alreadyLoaded;
    }

    protected void setActionBarTitle(String content){
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar!=null){
            actionBar.setTitle(content);
        }
    }
}
