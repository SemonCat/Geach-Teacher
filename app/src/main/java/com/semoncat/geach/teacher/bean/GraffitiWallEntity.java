package com.semoncat.geach.teacher.bean;

/**
 * Created by SemonCat on 2014/8/7.
 */

import java.util.List;

/**
 * Created by SemonCat on 2014/8/7.
 */
public class GraffitiWallEntity {

    private static final String TAG = GraffitiWallEntity.class.getName();
    private int id;
    private int courseId;
    private String name;
    private Type type = Type.Graffiti;
    private String memo;
    private List<GraffitiObjectEntity> graffitiObjectsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraffitiWallEntity that = (GraffitiWallEntity) o;

        if (courseId != that.courseId) return false;
        if (id != that.id) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + courseId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }

    public List<GraffitiObjectEntity> getGraffitiObjectsById() {
        return graffitiObjectsById;
    }

    public void setGraffitiObjectsById(List<GraffitiObjectEntity> graffitiObjectsById) {
        this.graffitiObjectsById = graffitiObjectsById;
    }

    public enum Type {
        Graffiti, Camera;
    }
}