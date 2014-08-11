package com.semoncat.geach.teacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SemonCat on 2014/7/18.
 */
@Root(name = "Video")
public class VideosEntity implements Parcelable {

    @ElementList(name = "VideoPath")
    private List<Video> videos = new ArrayList<Video>();

    public void addVideo(Video video){
        videos.add(video);
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(videos);
    }

    public VideosEntity() {
    }

    private VideosEntity(Parcel in) {
        in.readTypedList(videos, Video.CREATOR);
    }

    public static final Parcelable.Creator<VideosEntity> CREATOR = new Parcelable.Creator<VideosEntity>() {
        public VideosEntity createFromParcel(Parcel source) {
            return new VideosEntity(source);
        }

        public VideosEntity[] newArray(int size) {
            return new VideosEntity[size];
        }
    };
}
