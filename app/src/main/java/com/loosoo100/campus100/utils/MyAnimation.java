package com.loosoo100.campus100.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * @author yang 动画
 */
public class MyAnimation {
    /**
     * 渐变动画
     */
    public static AlphaAnimation getAlphaAnimation(int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(false);
        return alphaAnimation;
    }

    /**
     * 缩放动画(从下到上 校园巴士)
     */
    public static ScaleAnimation getScaleAnimationToTopBus() {
        ScaleAnimation scale = new ScaleAnimation(1f, 1f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从上到下 校园巴士)
     */
    public static ScaleAnimation getScaleAnimationToBottomBus() {
        ScaleAnimation scale = new ScaleAnimation(1f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从下到上)
     */
    public static ScaleAnimation getScaleAnimationToTop() {
        ScaleAnimation scale = new ScaleAnimation(1f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从上到下)
     */
    public static ScaleAnimation getScaleAnimationToBottom() {
        ScaleAnimation scale = new ScaleAnimation(1f, 1f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 抛物线
     *
     * @param view
     */
    public static void paowuxian(final View view) {

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(1000);
        valueAnimator.setObjectValues(new PointF());
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 500 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                view.setX(point.x);
                view.setY(point.y);

            }
        });
    }

    /**
     * 抛物线动画
     */
    public static void startParabolaAnimation(View imageView, float X, float Y) {
        int count = 300;
        float a = -1f / 75f;
        Keyframe[] keyframes = new Keyframe[count];
        final float keyStep = 1f / (float) count;
        float key = keyStep;
        for (int i = 0; i < count; ++i) {
            keyframes[i] = Keyframe.ofFloat(key, i + 1);
            key += keyStep;
        }

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe(
                "translationX", keyframes);
        key = keyStep;
        for (int i = 0; i < count; ++i) {
            keyframes[i] = Keyframe.ofFloat(key, -a * (i + 1) * (i + 1) + 4
                    * (i + 1));
            key += keyStep;
        }

        PropertyValuesHolder pvhY = PropertyValuesHolder.ofKeyframe(
                "translationY", keyframes);
        ObjectAnimator yxBouncer = ObjectAnimator.ofPropertyValuesHolder(
                imageView, pvhY, pvhX).setDuration(1500);
        yxBouncer.setInterpolator(new BounceInterpolator());
        yxBouncer.start();
    }

    /**
     * 缩放动画(从右上到左下)
     */
    public static ScaleAnimation getScaleAnimationToLeftBottom() {
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从右上到左下2)
     */
    public static ScaleAnimation getScaleAnimationToLeftBottom2() {
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        scale.setDuration(300);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从左下到右上)
     */
    public static ScaleAnimation getScaleAnimationToRightTop() {
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从左下到右上2)
     */
    public static ScaleAnimation getScaleAnimationToRightTop2() {
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        scale.setDuration(300);
        scale.setFillAfter(false);
        return scale;
    }


    /**
     * 缩放动画(从右下到左上)
     */
    public static ScaleAnimation getScaleAnimationToLeftTop() {
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        scale.setDuration(300);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从左上到右下)
     */
    public static ScaleAnimation getScaleAnimationToRightBottom() {
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        scale.setDuration(300);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从右到左)
     */
    public static ScaleAnimation getScaleAnimationToLeft() {
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(从左到右)
     */
    public static ScaleAnimation getScaleAnimationToRight() {
        ScaleAnimation scale = new ScaleAnimation(1f, 0f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(200);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画
     */
    public static ScaleAnimation getScaleAnimation() {
        ScaleAnimation scale = new ScaleAnimation(0.95f, 1.00f, 0.95f, 1.00f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(30);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画
     */
    public static ScaleAnimation getScaleAnimation(boolean loop) {
        ScaleAnimation scale = new ScaleAnimation(0.9f, 1.2f, 0.9f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(500);
        if (loop) {
            scale.setRepeatCount(Animation.INFINITE);
        }
        scale.setRepeatMode(Animation.REVERSE);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画
     */
    public static ScaleAnimation getScaleAnimationStore() {
        ScaleAnimation scale = new ScaleAnimation(0.9f, 1.00f, 0.9f, 1.00f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(30);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(定位按钮)
     */
    public static ScaleAnimation getScaleAnimationLocation() {
        ScaleAnimation scale = new ScaleAnimation(0.8f, 1.1f, 0.8f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(300);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 缩放动画(下方按钮)
     */
    public static ScaleAnimation getScaleAnimationDown() {
        ScaleAnimation scale = new ScaleAnimation(0.95f, 1.1f, 0.95f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(100);
        scale.setFillAfter(false);
        return scale;
    }

    /**
     * 平移动画(购物车布局)
     */
    public static TranslateAnimation getTranslateAnimation() {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF,
                0.02f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        translate.setDuration(300);
        translate.setFillAfter(false);
        ;
        return translate;
    }

    /**
     * 平移动画(购物车布局)
     */
    public static TranslateAnimation getTranslateAnimation2() {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        translate.setDuration(300);
        translate.setFillAfter(false);
        ;
        return translate;
    }

    /**
     * 平移动画(购物车布局)
     */
    public static TranslateAnimation getTranslateAnimation3(float x, float y) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, x,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, y);
        translate.setDuration(1);
        translate.setFillAfter(true);
        return translate;
    }

    /**
     * 旋转动画
     */
    public static RotateAnimation getRotateAnimation(float x) {
        RotateAnimation rotate = new RotateAnimation(0, x,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                1f);
        // 设置动画时间
        rotate.setDuration(1);
        // 设置动画结束后控件停留在结束的状态
        rotate.setFillAfter(true);
        return rotate;
    }

}
