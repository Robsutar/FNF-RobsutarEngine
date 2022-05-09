package com.robsutar.Fnf.Animated;

import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Engine.RenderableObjects.GameObject;
import com.robsutar.Fnf.AnimationLoader.Anim;

import java.awt.*;

public class AnimObject extends GameObject {
    Anim animation;
    int fX,fY;
    public AnimObject(Anim animation){
        this.animation=animation;
        animation.playAnimation("moveIdle");
    }

    public Anim getAnim() {
        return animation;
    }

    public void bpmTick(){
        image = animation.getActualImage();
        fX = animation.getActualFrameX()+animation.getActualWidth()/2;
        fY = animation.getActualFrameY()+animation.getActualHeight()/2;
        animation.nextAnimationFrame();
        image=animation.getActualImage();
    }

    @Override
    protected void renderDrawImage(Graphics2D g2d) {
        g2d.translate(-fX,-fY);
        super.renderDrawImage(g2d);
    }
}
