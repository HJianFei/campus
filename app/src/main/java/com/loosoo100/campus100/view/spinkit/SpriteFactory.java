package com.loosoo100.campus100.view.spinkit;

import com.loosoo100.campus100.view.spinkit.sprite.Sprite;
import com.loosoo100.campus100.view.spinkit.style.ChasingDots;
import com.loosoo100.campus100.view.spinkit.style.Circle;
import com.loosoo100.campus100.view.spinkit.style.CubeGrid;
import com.loosoo100.campus100.view.spinkit.style.DoubleBounce;
import com.loosoo100.campus100.view.spinkit.style.FadingCircle;
import com.loosoo100.campus100.view.spinkit.style.FoldingCube;
import com.loosoo100.campus100.view.spinkit.style.Pulse;
import com.loosoo100.campus100.view.spinkit.style.RotatingCircle;
import com.loosoo100.campus100.view.spinkit.style.RotatingPlane;
import com.loosoo100.campus100.view.spinkit.style.ThreeBounce;
import com.loosoo100.campus100.view.spinkit.style.WanderingCubes;
import com.loosoo100.campus100.view.spinkit.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            default:
                break;
        }
        return sprite;
    }
}
