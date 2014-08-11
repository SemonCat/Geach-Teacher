package com.semoncat.geach.teacher.bean;

/**
 * Created by SemonCat on 2014/8/7.
 */
public class GraffitiObjectEntity {

    private static final String TAG = GraffitiObjectEntity.class.getName();

    private int id;
    private int wallId;
    private int studentId;
    private String url;
    private String comment;
    private String memo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWallId() {
        return wallId;
    }

    public void setWallId(int wallId) {
        this.wallId = wallId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

        GraffitiObjectEntity that = (GraffitiObjectEntity) o;

        if (id != that.id) return false;
        if (studentId != that.studentId) return false;
        if (wallId != that.wallId) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + wallId;
        result = 31 * result + studentId;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }

}
