package com.semoncat.geach.teacher.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.semoncat.geach.teacher.util.ImageBlurrer;
import com.squareup.picasso.Transformation;

/**
 * Created by SemonCat on 2014/8/5.
 */
public class BlurTransform implements Transformation {

    private static final String TAG = BlurTransform.class.getName();

    private ImageBlurrer imageBlurrer;

    public BlurTransform(ImageBlurrer blurrer) {
        imageBlurrer = blurrer;
    }

    @Override
    public Bitmap transform(Bitmap source) {

        Bitmap result = imageBlurrer.blurBitmap(source,
                15, 0);

        Log.d(TAG,"transform");

        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "blur";
    }
}
