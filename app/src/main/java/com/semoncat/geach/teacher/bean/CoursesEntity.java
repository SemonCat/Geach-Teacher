package com.semoncat.geach.teacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SemonCat on 2014/7/16.
 */
public class CoursesEntity implements Parcelable {

    private static final String TAG = CoursesEntity.class.getName();

    private int id;
    private String name;
    private String memo;
    private boolean status = true;
    private int studentsNumber;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(int studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoursesEntity that = (CoursesEntity) o;

        if (id != that.id) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CoursesEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.memo);
        dest.writeByte(status ? (byte) 1 : (byte) 0);
        dest.writeInt(this.studentsNumber);
    }

    public CoursesEntity() {
    }

    private CoursesEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.memo = in.readString();
        this.status = in.readByte() != 0;
        this.studentsNumber = in.readInt();
    }

    public static final Parcelable.Creator<CoursesEntity> CREATOR = new Parcelable.Creator<CoursesEntity>() {
        public CoursesEntity createFromParcel(Parcel source) {
            return new CoursesEntity(source);
        }

        public CoursesEntity[] newArray(int size) {
            return new CoursesEntity[size];
        }
    };
}