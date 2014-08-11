package com.semoncat.geach.teacher.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.Matrix3f;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.ScriptIntrinsicColorMatrix;
import android.widget.ImageView;

import com.semoncat.geach.teacher.R;

public class ImageBlurrer {

    public static final int MAX_SUPPORTED_BLUR_PIXELS = 25;
    private RenderScript mRS;

    private ScriptIntrinsicBlur mSIBlur;
    private ScriptIntrinsicColorMatrix mSIGrey;
    private Allocation mTmp1;
    private Allocation mTmp2;

    public ImageBlurrer(Context context) {
        mRS = RenderScript.create(context);
        mSIBlur = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
        mSIGrey = ScriptIntrinsicColorMatrix.create(mRS, Element.U8_4(mRS));
    }

    public void blurImageView(ImageView imageView, float radius, float desaturateAmount) {

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        imageView.setImageBitmap(blurBitmap(bitmap, radius, desaturateAmount));
    }

    public Bitmap blurBitmap(Bitmap src, float radius, float desaturateAmount) {
        Bitmap dest = Bitmap.createBitmap(src);

        if ((int) radius == 0) {
            return dest;
        }


        if (mTmp1 != null) {
            mTmp1.destroy();
        }
        if (mTmp2 != null) {
            mTmp2.destroy();
        }

        mTmp1 = Allocation.createFromBitmap(mRS, src);
        mTmp2 = Allocation.createFromBitmap(mRS, dest);

        mSIBlur.setRadius((int) radius);
        mSIBlur.setInput(mTmp1);
        mSIBlur.forEach(mTmp2);

        if (desaturateAmount > 0) {
            desaturateAmount = MathUtil.constrain(0, 1, desaturateAmount);
            Matrix3f m = new Matrix3f(new float[]{
                    MathUtil.interpolate(1, 0.299f, desaturateAmount),
                    MathUtil.interpolate(0, 0.299f, desaturateAmount),
                    MathUtil.interpolate(0, 0.299f, desaturateAmount),

                    MathUtil.interpolate(0, 0.587f, desaturateAmount),
                    MathUtil.interpolate(1, 0.587f, desaturateAmount),
                    MathUtil.interpolate(0, 0.587f, desaturateAmount),

                    MathUtil.interpolate(0, 0.114f, desaturateAmount),
                    MathUtil.interpolate(0, 0.114f, desaturateAmount),
                    MathUtil.interpolate(1, 0.114f, desaturateAmount),
            });
            mSIGrey.setColorMatrix(m);
            mSIGrey.forEach(mTmp2, mTmp1);
            mTmp1.copyTo(dest);
        } else {
            mTmp2.copyTo(dest);
        }
        return dest;
    }

    public void destroy() {
        mSIBlur.destroy();
        if (mTmp1 != null) {
            mTmp1.destroy();
        }
        if (mTmp2 != null) {
            mTmp2.destroy();
        }
        mRS.destroy();
    }
}