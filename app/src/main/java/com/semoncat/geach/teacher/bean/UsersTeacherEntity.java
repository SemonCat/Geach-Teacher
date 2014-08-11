package com.semoncat.geach.teacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

/**
 * Created by SemonCat on 2014/7/17.
 */
public class UsersTeacherEntity implements Parcelable {

    private static final String TAG = UsersTeacherEntity.class.getName();

    private int id;
    private String name;
    private String photo;
    private String title;
    private String school;
    private String memo;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

        UsersTeacherEntity that = (UsersTeacherEntity) o;

        if (id != that.id) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (school != null ? !school.equals(that.school) : that.school != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.title);
        dest.writeString(this.school);
        dest.writeString(this.memo);
    }

    public UsersTeacherEntity() {
    }

    private UsersTeacherEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.photo = in.readString();
        this.title = in.readString();
        this.school = in.readString();
        this.memo = in.readString();
    }

    public static final Creator<UsersTeacherEntity> CREATOR = new Creator<UsersTeacherEntity>() {
        public UsersTeacherEntity createFromParcel(Parcel source) {
            return new UsersTeacherEntity(source);
        }

        public UsersTeacherEntity[] newArray(int size) {
            return new UsersTeacherEntity[size];
        }
    };
}