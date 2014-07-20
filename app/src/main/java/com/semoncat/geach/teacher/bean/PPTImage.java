package com.semoncat.geach.teacher.bean;

import android.graphics.Bitmap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.NotActiveException;

/**
 * Created by SemonCat on 2014/7/18.
 */
@Root(name = "Image")
public class PPTImage {

    @Element(name = "FileName")
    private String fileName;

    private String PPTPathTmp;

    @Element(name = "TopicId")
    private int topicId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPPTPathTmp() {
        return PPTPathTmp;
    }

    public void setPPTPathTmp(String PPTPathTmp) {
        this.PPTPathTmp = PPTPathTmp;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
