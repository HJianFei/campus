package com.loosoo100.campus100.view.spinkit.style;

import android.animation.ValueAnimator;

import com.loosoo100.campus100.view.spinkit.animation.SpriteAnimatorBuilder;
import com.loosoo100.campus100.view.spinkit.sprite.CircleSprite;
import com.loosoo100.campus100.view.spinkit.sprite.Sprite;
import com.loosoo100.campus100.view.spinkit.sprite.SpriteContainer;

/**
 * Created by ybq.
 */
public class DoubleBounce extends SpriteContainer {

    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new Bounce(), new Bounce()
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        super.onChildCreated(sprites);
        sprites[1].setAnimationDelay(-1000);
    }

    class Bounce extends CircleSprite {

        public Bounce() {
            setAlpha(153);
            setScale(0f);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.5f, 1f};
            return new SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f, 0f).
                    duration(2000).
                    easeInOut(fractions)
                    .build();
        }
    }
}
