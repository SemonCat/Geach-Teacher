package com.semoncat.geach.teacher.util;

/**
 * Created by SemonCat on 2014/8/5.
 */
public class MathUtil {
    public static float constrain(float min, float max, float v) {
        return Math.max(min, Math.min(max, v));
    }

    public static float interpolate(float x1, float x2, float f) {
        return x1 + (x2 - x1) * f;
    }
}
