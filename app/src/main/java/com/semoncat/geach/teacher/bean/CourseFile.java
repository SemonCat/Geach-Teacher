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
public class CourseFile{

    private int id;

    @ElementList(name = "Unit")
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

}
