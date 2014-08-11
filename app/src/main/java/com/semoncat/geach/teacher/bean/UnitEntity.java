package com.semoncat.geach.teacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by SemonCat on 2014/7/18.
 */
@Root(name = "Unit")
public class UnitEntity implements Parcelable {

    @Element(name = "Id")
    private int id;

    @Element(name = "Name")
    private String name;

    @Element(name = "PPT",required = false)
    private PPTsEntity PPTsEntity;

    private VideosEntity videosEntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PPTsEntity getPPTsEntity() {
        return PPTsEntity;
    }

    public void setPPTsEntity(PPTsEntity PPTsEntity) {
        this.PPTsEntity = PPTsEntity;
    }

    public VideosEntity getVideosEntity() {
        return videosEntity;
    }

    public void setVideosEntity(VideosEntity videosEntity) {
        this.videosEntity = videosEntity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.PPTsEntity, 0);
        dest.writeParcelable(this.videosEntity, 0);
    }

    public UnitEntity() {
    }

    private UnitEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.PPTsEntity = in.readParcelable(com.semoncat.geach.teacher.bean.PPTsEntity.class.getClassLoader());
        this.videosEntity = in.readParcelable(VideosEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<UnitEntity> CREATOR = new Parcelable.Creator<UnitEntity>() {
        public UnitEntity createFromParcel(Parcel source) {
            return new UnitEntity(source);
        }

        public UnitEntity[] newArray(int size) {
            return new UnitEntity[size];
        }
    };
}
