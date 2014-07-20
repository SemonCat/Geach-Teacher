package com.semoncat.geach.teacher.util;

import android.content.Context;

import com.semoncat.geach.teacher.bean.CourseFile;
import com.semoncat.geach.teacher.bean.CoursesEntity;

import java.io.File;

/**
 * Created by SemonCat on 2014/7/20.
 */
public class GeachConfigGetter {

    private static Context context;

    private static GeachConfigGetter geachConfigGetter;

    private static CourseFileParser courseFileParser;

    private GeachConfigGetter() {

    }

    public static GeachConfigGetter getInstance(Context context) {
        if (geachConfigGetter == null) {
            init(context);
        }

        return geachConfigGetter;
    }

    private static void init(Context context) {
        geachConfigGetter = new GeachConfigGetter();

        courseFileParser = new CourseXMLParser(context, new File(ConstantUtil.GEACH_ABS_DIR));


    }

    public void getCourseFile(CoursesEntity coursesEntity, CourseFileParser.OnCourseFileReady callback) {
        courseFileParser.getCourseFile(coursesEntity, callback);
    }

    public boolean validateCourseFile(CoursesEntity coursesEntity) throws Exception {
        return courseFileParser.validateCourseFile(coursesEntity);
    }
}
