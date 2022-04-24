package com.robsutar.Fnf.PhaseCreator.PhaseDesignObjects;

import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.GraphicsManipulator;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PCDObject {
    protected BufferedImage image;
    protected Color selectedColor = EngineVisuals.changeOpacity(EngineVisuals.SPRAY,0.5f);
    protected Color color = EngineVisuals.SPRAY;
    protected int priority = 0;

    public int getPriority(){return priority;}

    public void render(Graphics2D g2d,int x, int y){
        g2d.translate(x,y);

        GraphicsManipulator.setOpacity(g2d,1f);
        renderImage(g2d);
    }

    public BufferedImage getImage(){return image;}

    public Color getSelectedColor() {
        return selectedColor;
    }

    public Color getColor() {
        return color;
    }

    protected void renderImage(Graphics2D g2d){
        if (image!=null){
            g2d.drawImage(image,-image.getWidth()/2,-image.getHeight()/2,null);
        }
    }
}
