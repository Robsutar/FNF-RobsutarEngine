package com.robsutar.Fnf.Animated;

import com.robsutar.Engine.RenderableObjects.GameObject;
import com.robsutar.Fnf.AnimationBuilder.Animation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AnimatedObject extends GameObject {
    Animation animation;
    int fX,fY;
    public AnimatedObject(Animation animation){
        this.animation=animation;
        animation.playAnimation("moveIdle");
    }

    public Animation getAnimation() {
        return animation;
    }

    public void bpmTick(){
        image = animation.getActualImage();
        fX = animation.getActualFrameX()+animation.getActualWidth()/2;
        fY = animation.getActualFrameY()+animation.getActualHeight()/2;
        animation.nextAnimationFrame();
    }

    @Override
    protected void renderDrawImage(Graphics2D g2d) {
        g2d.translate(-fX,-fY);
        super.renderDrawImage(g2d);
    }
}
