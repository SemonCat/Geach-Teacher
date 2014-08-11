package com.semoncat.geach.teacher.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SemonCat on 2014/7/17.
 */
@Root(name = "PPT")
public class PPTsEntity implements Parcelable {

    @ElementList(name = "ImageList",required = false)
    private List<PPTImage> pptImages = new ArrayList<PPTImage>();

    public void addPPTImage(PPTImage pptImage){
        pptImages.add(pptImage);
    }

    public List<PPTImage> getPptImages() {
        return pptImages;
    }

    public void setPptImages(List<PPTImage> pptImages) {
        this.pptImages = pptImages;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(pptImages);
    }

    public PPTsEntity() {
    }

    private PPTsEntity(Parcel in) {
        in.readTypedList(pptImages, PPTImage.CREATOR);
    }

    public static final Parcelable.Creator<PPTsEntity> CREATOR = new Parcelable.Creator<PPTsEntity>() {
        public PPTsEntity createFromParcel(Parcel source) {
            return new PPTsEntity(source);
        }

        public PPTsEntity[] newArray(int size) {
            return new PPTsEntity[size];
        }
    };
}
