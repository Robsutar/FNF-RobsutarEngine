package com.robsutar.Engine.RenderableObjects;

import com.robsutar.Engine.Threads.Renderable;
import com.robsutar.Engine.Ultilities.Anchor;
import com.robsutar.Engine.Ultilities.RectBox;

import java.awt.*;

public abstract class SimpleRenderable extends RectBox implements Renderable {

    protected Anchor anchor = Anchor.ANCHOR_MIDDLE;

    @Override
    public void render(Graphics2D g2d) {
        renderDrawAnchor(g2d);
        renderDrawPosition(g2d);

        renderDrawImage(g2d);
    }

    protected void renderDrawImage(Graphics2D g2d){

    }

    private void renderRotate(Graphics2D g2d){
        //g2d.rotate(Math.toRadians(getRotation()),getWidth()/2,getHeight()/2);
    }

    protected void renderDrawAnchor(Graphics2D g2d){
        g2d.translate(anchor.getX(),anchor.getY());
    }

    protected void renderDrawPosition(Graphics2D g2d){
        g2d.translate(x,y);
    }
}
