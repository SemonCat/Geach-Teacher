package com.semoncat.geach.teacher.util;

import android.os.Environment;

/**
 * Created by SemonCat on 2014/7/15.
 */
public class ConstantUtil {

    public static final String GEACH_SERVER = "http://54.214.24.26:8080/GeachServer";

    public static final String GEACH_DIR = "/Geach";

    public static final String GEACH_ABS_DIR = Environment.getExternalStorageDirectory()+ ConstantUtil.GEACH_DIR;

    public static final String GEACH_CONFIG_FILE_NAME = "CourseData.xml";

    public static final String GEACH_FILE_EXT = "gch";

    public static final String GEACH_FILE_NAME_PATTENR = "%1$s";

    public static final String GEACH_FILE_PPT_PATTERN = "Data/Unit%1$02d/PPT/";

    //請用大寫
    public static final String[] GEACH_FILE_PPT_EXT_FILTER = new String[]{"PNG","JPG"};

}
