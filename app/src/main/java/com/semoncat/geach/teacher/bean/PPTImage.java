package com.semoncat.geach.teacher.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.NotActiveException;

/**
 * Created by SemonCat on 2014/7/18.
 */
@Root(name = "Image")
public class PPTImage implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.PPTPathTmp);
        dest.writeInt(this.topicId);
    }

    public PPTImage() {
    }

    private PPTImage(Parcel in) {
        this.fileName = in.readString();
        this.PPTPathTmp = in.readString();
        this.topicId = in.readInt();
    }

    public static final Parcelable.Creator<PPTImage> CREATOR = new Parcelable.Creator<PPTImage>() {
        public PPTImage createFromParcel(Parcel source) {
            return new PPTImage(source);
        }

        public PPTImage[] newArray(int size) {
            return new PPTImage[size];
        }
    };
}
