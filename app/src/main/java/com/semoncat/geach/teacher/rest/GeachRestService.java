package com.semoncat.geach.teacher.rest;

import android.util.Log;

import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.UsersTeacherEntity;
import com.semoncat.geach.teacher.util.ConstantUtil;
import com.squareup.okhttp.Call;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by SemonCat on 2014/7/15.
 */
public class GeachRestService {
    private static final String TAG = GeachRestService.class.getName();

    private static GeachRestService geachRestService;

    private static Geach geachService;

    private static Executor executor;

    interface Geach {
        @FormUrlEncoded
        @POST("/rest/security/login")
        void login(
                @Field("username") String email,
                @Field("password") String password,
                Callback<String> cb
        );

        @FormUrlEncoded
        @POST("/rest/security/login")
        String loginSync(
                @Field("username") String email,
                @Field("password") String password
        );

        @GET("/rest/teacher/detail")
        void getDetail(Callback<UsersTeacherEntity> callback);

        @GET("/rest/teacher/detail")
        UsersTeacherEntity getDetailSync();

        @GET("/rest/teacher/courses")
        void getCourses(Callback<List<CoursesEntity>> cb);

        @GET("/rest/teacher/courses")
        List<CoursesEntity> getCoursesSync();
    }

    public static GeachRestService getInstance(){

        if (geachRestService==null){
            geachRestService = new GeachRestService();
        }

        return geachRestService;
    }

    private static void init(){
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ConstantUtil.GEACH_SERVER)
                .build();

        // Create an instance of our GitHub API interface.
        geachService = restAdapter.create(Geach.class);

        executor = Executors.newFixedThreadPool(5);
    }

    private GeachRestService() {
        init();
    }

    public void Login(String email,String password,Callback<String> callback){
        geachService.login(email,password,callback);
    }

    public String LoginSync(String email,String password){
        return geachService.loginSync(email, password);
    }

    public void LoginAndGetDetail(final String email,final String password,final Callback<UsersTeacherEntity> callback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                LoginSync(email, password);
                getDetail(callback);
            }
        });
    }

    public void getDetail(Callback<UsersTeacherEntity> callback){
        geachService.getDetail(callback);
    }

    public List<CoursesEntity> getCoursesSync(){
        return geachService.getCoursesSync();
    }

    public void getCourses(Callback<List<CoursesEntity>> callback){
        geachService.getCourses(callback);
    }
}
