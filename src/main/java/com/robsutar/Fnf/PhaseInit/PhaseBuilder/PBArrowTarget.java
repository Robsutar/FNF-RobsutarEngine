package com.robsutar.Fnf.PhaseInit.PhaseBuilder;

import com.robsutar.Assets;
import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Fnf.AnimationBuilder.Animation;
import com.robsutar.Fnf.AnimationLoader.Anim;
import com.robsutar.Fnf.PhaseCreator.Arrows;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class PBArrowTarget {
    private final Anim animation = new Anim(Assets.ARROW_ANIM);

    final String name, color;
    public final String moveName;

    private PBArrowTarget(String name, String color) {
        this.name = name;
        this.moveName="move"+name.substring(0, 1).toUpperCase() + name.substring(1);;
        this.color = color;
        playIdle();
    }

    public void playIdle() {
        animation.playAnimation(name + "Idle");
    }

    public void playHoldPiece() {
        animation.playAnimation(color + "HoldPiece");
    }

    public void playHoldEnd() {
        animation.playAnimation(color + "HoldEnd");
    }

    public void playPress() {
        animation.playAnimation(name + "Press");
    }

    public void playConfirm() {
        animation.playAnimation(name + "Confirm");
    }

    public Anim getAnimation() {
        return animation;
    }

    public static class ArrowUp extends PBArrowTarget {
        public ArrowUp() {
            super("up", "green");
        }
    }

    public static class ArrowDown extends PBArrowTarget {
        public ArrowDown() {
            super("down", "blue");
        }
    }

    public static class ArrowLeft extends PBArrowTarget {
        public ArrowLeft() {
            super("left", "purple");
        }
    }

    public static class ArrowRight extends PBArrowTarget {
        public ArrowRight() {
            super("right", "red");
        }
    }
}
