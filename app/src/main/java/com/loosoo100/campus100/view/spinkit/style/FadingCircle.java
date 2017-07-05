package com.loosoo100.campus100.view.spinkit.style;

import android.animation.ValueAnimator;

import com.loosoo100.campus100.view.spinkit.animation.SpriteAnimatorBuilder;
import com.loosoo100.campus100.view.spinkit.sprite.CircleContainer;
import com.loosoo100.campus100.view.spinkit.sprite.CircleSprite;
import com.loosoo100.campus100.view.spinkit.sprite.Sprite;

/**
 * Created by ybq.
 */
public class FadingCircle extends CircleContainer {

    @Override
    public Sprite[] onCreateChild() {
        Dot[] dots = new Dot[12];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new Dot();
            dots[i].setAnimationDelay(100 * i + -1200);
        }
        return dots;
    }

    class Dot extends CircleSprite {

        public Dot() {
            setAlpha(0);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.39f, 0.4f, 1f};
            return new SpriteAnimatorBuilder(this).
                    alpha(fractions, 0, 0, 255, 0).
                    duration(1200).
                    easeInOut(fractions).build();
        }
    }
}
