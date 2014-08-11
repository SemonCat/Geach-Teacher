package com.semoncat.geach.teacher.fragment;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dd.CircularProgressButton;
import com.nvanbenschoten.motion.ParallaxImageView;
import com.semoncat.geach.teacher.MainActivity;
import com.semoncat.geach.teacher.R;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.UsersTeacherEntity;
import com.semoncat.geach.teacher.rest.GeachRestService;
import com.semoncat.geach.teacher.util.SecurePreferences;
import com.squareup.picasso.Picasso;


import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SemonCat on 2014/7/15.
 */
public class LoginFragment extends BaseFragment implements Callback<UsersTeacherEntity> {

    private static final String TAG = LoginFragment.class.getName();

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";


    private CircularProgressButton LoginButton;
    private ParallaxImageView LogoImage;
    private EditText EmailField;
    private EditText PasswordField;
    private ViewGroup LoginField;

    private GeachRestService geachRestService;

    private Handler handler;

    private static final int FAILURE_STATE_TIMEOUT = 1000;
    private static final int SUCCESS_STATE_TIMEOUT = 600;

    private SecurePreferences securePreferences;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        securePreferences = new SecurePreferences(getActivity());

        if (savedInstanceState != null) {
            EmailField.setText(savedInstanceState.getString(EMAIL, EMPTY));
            PasswordField.setText(savedInstanceState.getString(PASSWORD, EMPTY));
        } else {
            String email = getSharedPreferences().getString(EMAIL, EMPTY);
            if (!TextUtils.isEmpty(email)) {
                EmailField.setText(email);
                PasswordField.requestFocus();
            }

            String password = securePreferences.getString(PASSWORD, EMPTY);
            if (!TextUtils.isEmpty(password)) {
                PasswordField.setText(password);
            }

        }


        handler = new Handler();
        geachRestService = GeachRestService.getInstance();

        YoYo.with(Techniques.FlipInX)
                .duration(2000)
                .playOn(LogoImage);

        YoYo.with(Techniques.SlideInUp)
                .duration(1000)
                .playOn(LoginField);


    }

    @Override
    protected void init() {

    }

    @Override
    protected void setupView(View rootView) {
        LoginButton = (CircularProgressButton) rootView.findViewById(R.id.LoginButton);
        EmailField = (EditText) rootView.findViewById(R.id.EmailField);
        PasswordField = (EditText) rootView.findViewById(R.id.PasswordField);
        LoginField = (ViewGroup) rootView.findViewById(R.id.LoginField);

        LogoImage = (ParallaxImageView) rootView.findViewById(R.id.LogoImage);
        LogoImage.registerSensorManager();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LogoImage != null) {
            LogoImage.unregisterSensorManager();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LogoImage != null) {
            LogoImage.registerSensorManager();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(EMAIL, EmailField.getText().toString());
        outState.putString(PASSWORD, PasswordField.getText().toString());

    }

    @Override
    protected void setupAdapter() {

    }

    @Override
    protected void setupEvent() {
        LoginButton.setIndeterminateProgressMode(true);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (LoginButton.getProgress() == 0) {
                    LoginButton.setProgress(50);

                    geachRestService.Login(EmailField.getText().toString(),
                            PasswordField.getText().toString());

                    geachRestService.getDetail(LoginFragment.this);

                } else if (LoginButton.getProgress() == -1) {
                    LoginButton.setProgress(0);
                }
            }
        });
    }

    @Override
    public void success(final UsersTeacherEntity usersTeacherEntity, Response response) {
        LoginButton.setProgress(100);
        rememberMe();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainFragment(usersTeacherEntity);
            }
        }, SUCCESS_STATE_TIMEOUT);

        setHeadInfo(usersTeacherEntity);

        setupGeachRestService();
    }

    @Override
    public void failure(RetrofitError error) {

        YoYo.with(Techniques.Shake)
                .duration(FAILURE_STATE_TIMEOUT)
                .playOn(LoginField);

        PasswordField.setText(EMPTY);

        LoginButton.setProgress(-1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginButton.setProgress(0);
            }
        }, FAILURE_STATE_TIMEOUT);
    }

    private void rememberMe() {
        getSharedPreferences().edit().putString(EMAIL, EmailField.getText().toString()).commit();

        //getSharedPreferences().edit().putString(PASSWORD,PasswordField.getText().toString()).commit();
        securePreferences.edit().putString(PASSWORD, PasswordField.getText().toString()).commit();
    }

    @Override
    protected int setupLayout() {
        return R.layout.fragment_login;
    }

    private void toMainFragment(UsersTeacherEntity usersTeacherEntity) {
        hideKeyboard();

        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(UsersTeacherEntity.class.getName(), usersTeacherEntity);
        mainFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_fragment_horizontal_left_in,
                R.anim.slide_fragment_horizontal_left_out,
                R.anim.slide_fragment_horizontal_left_in,
                R.anim.slide_fragment_horizontal_left_out);
        fragmentTransaction.replace(R.id.container, mainFragment);
        fragmentTransaction.commit();
    }

    private void setHeadInfo(UsersTeacherEntity usersTeacherEntity) {
        View rootView = getDrawer().getMenuHeader();

        ImageView Head = (ImageView) rootView.findViewById(R.id.Head);
        TextView Name = (TextView) rootView.findViewById(R.id.Name);
        TextView EMail = (TextView) rootView.findViewById(R.id.Email);

        Picasso.with(getActivity()).load(usersTeacherEntity.getPhoto()).into(Head);

        Name.setText(usersTeacherEntity.getName());
        EMail.setText(usersTeacherEntity.getSchool());
    }


    private void setupGeachRestService() {
        final Handler handler = new Handler();
        GeachRestService.getInstance().setUnauthorizedListener(new GeachRestService.UnauthorizedListener() {
            @Override
            public void UnauthorizedEvent(RetrofitError cause) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showUnauthorizedDialog();
                    }
                });
            }
        });
    }

    private void showUnauthorizedDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.login_timeout_title)
                .setMessage(R.string.login_timeout_message)
                .setCancelable(false)
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                }).show();
    }


}
