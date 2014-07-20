package com.semoncat.geach.teacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by SemonCat on 2014/7/18.
 */
@Root(name = "Unit")
public class UnitEntity{

    @Element(name = "Id")
    private int id;

    @Element(name = "Name")
    private String name;

    @Element(name = "PPT")
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

}
