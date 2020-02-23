package com.mike_milk.bihu.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class ScaleAnimatorUtils {
    //设置缩放动画，淡入和淡出
    public static void setScale(View view) {
        //创建一个AnimatorSet对象
        AnimatorSet set = new AnimatorSet();
        //设置动画的时间和效果
        ObjectAnimator animator_x = ObjectAnimator.ofFloat(view, "scaleX", 1.5f, 1.2f, 1f, 0.5f, 0.7f, 1f);
        ObjectAnimator animator_y = ObjectAnimator.ofFloat(view, "scaleY", 1.5f, 1.2f, 1f, 0.5f, 0.7f, 1f);
        set.play(animator_x).with(animator_y);
        //设置他的持续时间
        set.setDuration(500);
        //启动动画
        set.start();
    }

}
