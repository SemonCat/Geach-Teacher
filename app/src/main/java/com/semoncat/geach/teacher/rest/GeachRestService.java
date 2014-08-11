package com.semoncat.geach.teacher.rest;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.GraffitiObjectEntity;
import com.semoncat.geach.teacher.bean.GraffitiWallEntity;
import com.semoncat.geach.teacher.bean.StudentsEntity;
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
import retrofit.ErrorHandler;
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
    public interface UnauthorizedListener {
        void UnauthorizedEvent(RetrofitError cause);
    }

    static class UnAuthorizedErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
            if (r != null && r.getStatus() == 401) {
                if (unauthorizedListener != null) {
                    unauthorizedListener.UnauthorizedEvent(cause);
                }
            }
            return cause;
        }
    }


    private static final String TAG = GeachRestService.class.getName();

    private static GeachRestService geachRestService;

    private static Geach geachService;

    private static Executor executor;

    private static UnauthorizedListener unauthorizedListener;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private static ApiRequestInterceptor requestInterceptor;

    interface Geach {

        @GET("/rest/teacher/detail")
        void getDetail(Callback<UsersTeacherEntity> callback);

        @GET("/rest/teacher/courses")
        void getCourses(Callback<List<CoursesEntity>> cb);

        @GET("/rest/teacher/courses")
        List<CoursesEntity> getCoursesSync();

        @GET("/rest/teacher/courses/{id}/students")
        void getStudentList(
                @Path("id")
                int courseId,
                Callback<List<StudentsEntity>> callback);

        @GET("/rest/teacher/graffiti/wall")
        void getGraffitiWallList(Callback<List<GraffitiWallEntity>> callback);

        @GET("/rest/teacher/graffiti/wall/{id}/object")
        void getGraffitiObjectList(@Path("id")
                                   int id,
                                   Callback<List<GraffitiObjectEntity>> callback);
    }

    public static GeachRestService getInstance() {

        if (geachRestService == null) {
            geachRestService = new GeachRestService();
        }

        return geachRestService;
    }

    private static void init() {
        requestInterceptor = new ApiRequestInterceptor();

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ConstantUtil.GEACH_SERVER)
                .setRequestInterceptor(requestInterceptor)
                .setErrorHandler(new UnAuthorizedErrorHandler())
                .build();

        // Create an instance of our GitHub API interface.
        geachService = restAdapter.create(Geach.class);

        executor = Executors.newFixedThreadPool(5);
    }

    private GeachRestService() {
        init();
    }

    public void setUnauthorizedListener(UnauthorizedListener unauthorizedListener) {
        GeachRestService.unauthorizedListener = unauthorizedListener;
    }

    public void Login(String email, String password) {
        requestInterceptor.setAccount(email);
        requestInterceptor.setPassword(password);
    }

    public void getDetail(Callback<UsersTeacherEntity> callback) {
        geachService.getDetail(callback);
    }

    public List<CoursesEntity> getCoursesSync() {
        return geachService.getCoursesSync();
    }

    public void getCourses(Callback<List<CoursesEntity>> callback) {
        geachService.getCourses(callback);
    }

    public void getStudentList(int courseId, Callback<List<StudentsEntity>> callback) {
        geachService.getStudentList(courseId, callback);
    }

    public void getGraffitiWallList(Callback<List<GraffitiWallEntity>> callback) {
        geachService.getGraffitiWallList(callback);
    }

    public void getGraffitiObjectList(int id,
                                      Callback<List<GraffitiObjectEntity>> callback) {
        geachService.getGraffitiObjectList(id, callback);
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
