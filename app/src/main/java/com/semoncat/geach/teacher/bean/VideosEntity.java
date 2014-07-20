package com.semoncat.geach.teacher.bean;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by SemonCat on 2014/7/18.
 */
@Root(name = "Video")
public class VideosEntity {

    @ElementList(name = "VideoPath")
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
