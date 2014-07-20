package com.semoncat.geach.teacher.util;

import com.semoncat.geach.teacher.bean.CourseFile;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.semoncat.geach.teacher.bean.UnitEntity;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by SemonCat on 2014/7/17.
 */
public interface CourseFileParser {

    public interface OnCourseFileReady{
        void OnSuccess(CourseFile courseFile);
        void OnFail(Exception e);
    }

    public boolean validateCourseFile(CoursesEntity coursesEntity) throws Exception;

    public void getCourseFile(CoursesEntity coursesEntity,OnCourseFileReady callback);

    public CourseFile getCourseFileSync(CoursesEntity coursesEntity) throws Exception;

    public File[] getGeachZipFile() throws FileNotFoundException;

    public File getCourseZipFile(CoursesEntity coursesEntity) throws FileNotFoundException;

    public File getCourseZipFile(int courseId) throws FileNotFoundException;

}
