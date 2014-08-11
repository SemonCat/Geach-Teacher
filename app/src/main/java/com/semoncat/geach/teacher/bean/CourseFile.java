package com.semoncat.geach.teacher.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SemonCat on 2014/7/18.
 */
@Root(name = "Course")
public class CourseFile implements Parcelable {

    private int id;

    @ElementList(name = "UnitList")
    private List<UnitEntity> unitEntities;

    public void addUnitEntity(UnitEntity unitEntity) {

        if (unitEntities == null) {
            unitEntities = new ArrayList<UnitEntity>();
        }

        unitEntities.add(unitEntity);
    }

    public List<UnitEntity> getUnitEntities() {
        return unitEntities;
    }

    public void setUnitEntities(List<UnitEntity> unitEntities) {
        this.unitEntities = unitEntities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(unitEntities);
    }

    public CourseFile() {
    }

    private CourseFile(Parcel in) {
        this.id = in.readInt();
        in.readTypedList(unitEntities, UnitEntity.CREATOR);
    }

    public static final Parcelable.Creator<CourseFile> CREATOR = new Parcelable.Creator<CourseFile>() {
        public CourseFile createFromParcel(Parcel source) {
            return new CourseFile(source);
        }

        public CourseFile[] newArray(int size) {
            return new CourseFile[size];
        }
    };
}
