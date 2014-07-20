package com.semoncat.geach.teacher.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.semoncat.geach.teacher.bean.CourseFile;
import com.semoncat.geach.teacher.bean.CoursesEntity;
import com.semoncat.geach.teacher.bean.PPTsEntity;
import com.semoncat.geach.teacher.bean.PPTImage;
import com.semoncat.geach.teacher.bean.UnitEntity;

import org.apache.commons.io.FilenameUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.ConvertException;
import org.simpleframework.xml.core.Persister;
import org.zeroturnaround.zip.ZipEntryCallback;
import org.zeroturnaround.zip.ZipInfoCallback;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;
import org.zeroturnaround.zip.commons.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;

/**
 * Created by SemonCat on 2014/7/18.
 */
public class CourseXMLParser implements CourseFileParser {

    private static final String TAG = CourseFileParser.class.getName();

    private static final String GEACH_CONFIG_TEMP_PATH = "/geach_config";

    private static final String GEACH_CONFIG_CHECKSUM_PATH = "/checksum";

    private final File GeachDir;

    private Context context;

    private Executor executor;

    private Handler handler;

    public CourseXMLParser(Context context, File GeachDir) {
        this.GeachDir = GeachDir;
        this.context = context;
        executor = Executors.newFixedThreadPool(5);
        handler = new Handler(context.getMainLooper());
    }

    private File getGeachConfigCacheDir() {
        File file = new File(context.getCacheDir() + GEACH_CONFIG_TEMP_PATH);

        if (!file.isDirectory()) {
            file.mkdirs();
        }

        return file;
    }

    public boolean validateCourseFile(CoursesEntity coursesEntity) throws Exception {

        File cFile = getCourseZipFile(coursesEntity);

        String cacheFile = getGeachConfigCacheDir() + "/temp.xml";

        File xmlFile = new File(cacheFile);

        ZipUtil.unpackEntry(cFile, ConstantUtil.GEACH_CONFIG_FILE_NAME, xmlFile);

        boolean result = new Persister().validate(CourseFile.class, xmlFile);

        xmlFile.delete();

        return result;
    }

    @Override
    public void getCourseFile(final CoursesEntity coursesEntity, final OnCourseFileReady callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final CourseFile courseFile = getCourseFileSync(coursesEntity);
                    if (callback != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.OnSuccess(courseFile);
                            }
                        });

                    }
                } catch (final Exception e) {
                    if (callback != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.OnFail(e);
                            }
                        });

                    }
                }
            }
        });
    }

    @Override
    public CourseFile getCourseFileSync(CoursesEntity coursesEntity) throws Exception {

        File courseFileObject = unpackCourseFile(coursesEntity);

        File xmlFile = new File(courseFileObject + File.separator + ConstantUtil.GEACH_CONFIG_FILE_NAME);

        Serializer serializer = new Persister();
        if (!serializer.validate(CourseFile.class, xmlFile)) {
            throw new ConvertException("XMLConvert Fail!");
        }

        CourseFile courseFile = new Persister().read(CourseFile.class, xmlFile);

        courseFile.setId(coursesEntity.getId());


        for (UnitEntity unitEntity : courseFile.getUnitEntities()) {

            PPTsEntity ppTsEntity = getPPTFile(courseFileObject, unitEntity.getId());

            unitEntity.setPPTsEntity(ppTsEntity);

        }


        return courseFile;
    }

    private File unpackCourseFile(CoursesEntity coursesEntity) throws IOException {

        File cFile = getCourseZipFile(coursesEntity);

        File targetFile = new File(getGeachConfigCacheDir() + File.separator + cFile.getName());

        File checkSum = new File(targetFile + GEACH_CONFIG_CHECKSUM_PATH);

        if (checkSum.exists()) {

            String md5 = getStringFromFile(checkSum);

            if (MD5.checkMD5(md5, cFile)) {
                return targetFile;
            }

        }

        ZipUtil.unpack(cFile, targetFile);
        writeToFile(MD5.calculateMD5(cFile), checkSum);


        return targetFile;
    }

    public PPTsEntity getPPTFile(File courseUnpackPath, int unit) throws FileNotFoundException {

        final PPTsEntity PPTsEntity = new PPTsEntity();

        String pptFilePath = String.format(ConstantUtil.GEACH_FILE_PPT_PATTERN, unit);

        File pptFileDir = new File(courseUnpackPath + File.separator + pptFilePath);

        if (pptFileDir.isDirectory()) {
            for (File pptFile : pptFileDir.listFiles()) {
                String fileName = pptFile.getName();

                if (!pptFile.isDirectory() && FilenameUtils.isExtension(fileName.toUpperCase(), ConstantUtil.GEACH_FILE_PPT_EXT_FILTER)) {
                    PPTImage pptImage = new PPTImage();
                    pptImage.setFileName(fileName);
                    pptImage.setPPTPathTmp(pptFile.getPath());
                    PPTsEntity.addPPTImage(pptImage);
                }
            }
        }

        return PPTsEntity;
    }

    public File[] getGeachZipFile() throws FileNotFoundException {
        List<File> files = new ArrayList<File>();
        for (File file : GeachDir.listFiles()) {
            //獲得副檔名為 GEACH_FILE_EXT 的 File
            if (ConstantUtil.GEACH_FILE_EXT.equalsIgnoreCase(FilenameUtils.getExtension(file.getPath()))) {
                files.add(file);
            }
        }

        if (files.size() <= 0) {
            throw new FileNotFoundException();
        }

        return files.toArray(new File[files.size()]);
    }

    public File getCourseZipFile(CoursesEntity coursesEntity) throws FileNotFoundException {
        return getCourseZipFile(coursesEntity.getId());
    }

    public File getCourseZipFile(int courseId) throws FileNotFoundException {
        File[] geachZipFile = getGeachZipFile();
        for (File file : geachZipFile) {
            //如果 GeachFile 的檔名跟 Course 一樣，則視為該課程之檔案

            String fileName = String.format(ConstantUtil.GEACH_FILE_NAME_PATTENR,courseId);

            if (file.getName().startsWith(fileName)) {
                return file;
            }
        }

        throw new FileNotFoundException("CourseZipFileNotFound,forget put file?");
    }

    private boolean isGeachSupportImage(ZipEntry zipEntry, int unit) {

        String pathPattern = String.format(ConstantUtil.GEACH_FILE_PPT_PATTERN, unit);

        return !zipEntry.isDirectory() &&
                zipEntry.getName().startsWith(pathPattern) &&
                isGeachSupportImageExt(zipEntry.getName());
    }

    private boolean isGeachSupportImageExt(String fileName) {
        return FilenameUtils.isExtension(fileName, ConstantUtil.GEACH_FILE_PPT_EXT_FILTER);
    }

    private void writeToFile(String data, File target) {
        try {
            FileWriter writer = new FileWriter(target);

            /** Saving the contents to the file*/
            writer.write(data);

            /** Closing the writer object */
            writer.close();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString().trim();
    }

    public static String getStringFromFile(File fl) throws IOException {
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

}
