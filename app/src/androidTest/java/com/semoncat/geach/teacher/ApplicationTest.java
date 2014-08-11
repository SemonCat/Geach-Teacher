package com.semoncat.geach.teacher;

import android.app.Application;
import android.os.Environment;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.semoncat.geach.teacher.bean.CourseFile;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.PPTImage;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.semoncat.geach.teacher.bean.UnitEntity;
import com.semoncat.geach.teacher.rest.GeachRestService;
import com.semoncat.geach.teacher.util.ConstantUtil;
import com.semoncat.geach.teacher.util.CourseFileParser;
import com.semoncat.geach.teacher.util.CourseXMLParser;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static final String TAG = ApplicationTest.class.getName();

    public ApplicationTest() {
        super(Application.class);
    }


    /*
    public void testZip(){



        try {
            CourseXMLParser courseXMLParser = new CourseXMLParser(getContext(),new File(ConstantUtil.GEACH_ABS_DIR));

            File cache = new File(getContext().getCacheDir()+"/tmp");

            File file = courseXMLParser.getCourseZipFile(1);

            ZipUtil.unpackEntry(file, "Data/Unit01/PPT", cache);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    */


    public void testGenerateXML(){

        try {
            File geachDir = new File(Environment.getExternalStorageDirectory()+ ConstantUtil.GEACH_DIR+"/CourseData.xml");

            CourseFile courseFile = new CourseFile();


            for (int i = 0;i<5;i++){

                UnitEntity unitEntity = new UnitEntity();
                unitEntity.setId(i+1);
                unitEntity.setName("測試 Unit"+(i+1));

                PPTImage pptImage = new PPTImage();
                pptImage.setFileName("02");
                pptImage.setTopicId(1);

                PPTsEntity ppTsEntity = new PPTsEntity();

                ppTsEntity.addPPTImage(pptImage);

                unitEntity.setPPTsEntity(ppTsEntity);

                courseFile.addUnitEntity(unitEntity);
            }

            Serializer serializer = new Persister();

            serializer.write(courseFile,geachDir);

            Log.d(TAG,"IsValidate:"+serializer.validate(CourseFile.class,geachDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}