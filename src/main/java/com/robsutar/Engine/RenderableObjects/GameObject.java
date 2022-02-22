package com.robsutar.Engine.RenderableObjects;

import com.robsutar.Engine.Threads.Ticable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameObject extends SimpleRenderable implements Ticable {
    protected BufferedImage image;

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
    }

    @Override
    protected void renderDrawImage(Graphics2D g2d) {
        super.renderDrawImage(g2d);
        if (image!=null){
            g2d.drawImage(image,0,0,null);
        }
    }

    @Override
    public void tick() {

    }
}
