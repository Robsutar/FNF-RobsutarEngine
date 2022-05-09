package com.robsutar.Fnf.PhaseInit.PhaseBuilder;

import com.robsutar.Assets;
import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Fnf.AnimationLoader.Anim;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PBArrowSet {

    private final Anim anim;

    public final PBArrowTarget.ArrowDown arrowDown = new PBArrowTarget.ArrowDown();
    public final PBArrowTarget.ArrowLeft arrowLeft = new PBArrowTarget.ArrowLeft();
    public final PBArrowTarget.ArrowRight arrowRight = new PBArrowTarget.ArrowRight();
    public final PBArrowTarget.ArrowUp arrowUp = new PBArrowTarget.ArrowUp();

    private final int width;

    public PBArrowSet() {
        this.anim = new Anim(Assets.ARROW_ANIM);
        width=anim.fileHook.animationConfigurations.get(0).atlas.get(0).image.getWidth();
    }

    public void bpmTick(){
        anim.nextAnimationFrame();
        arrowLeft.getAnimation().nextAnimationFrame();
        arrowRight.getAnimation().nextAnimationFrame();
        arrowDown.getAnimation().nextAnimationFrame();
        arrowUp.getAnimation().nextAnimationFrame();
    }

    public void renderByPanel(Graphics2D g2d, int panel, int x, int y, int width) {
        AffineTransform at = g2d.getTransform();
        BufferedImage image;
        switch (panel) {
            case 1 -> image = anim.fileHook.animationConfigurations.get(15).atlas.get(0).image;
            case 2 -> image = anim.fileHook.animationConfigurations.get(4).atlas.get(0).image;
            case 3 -> image = anim.fileHook.animationConfigurations.get(9).atlas.get(0).image;
            case 4 -> image = anim.fileHook.animationConfigurations.get(17).atlas.get(0).image;
            default -> image = anim.fileHook.animationConfigurations.get(0).atlas.get(0).image;
        }
        g2d.translate(x,y);
        g2d.scale((double) width/this.width,(double) width/this.width);
        g2d.translate(-image.getWidth()/2,-image.getHeight()/2);
        g2d.drawImage(image,0,0,null);
        GraphicsManipulator.resetGraphics(g2d,at);
    }

    public void renderTargetByPanel(Graphics2D g2d,int panel,int x, int y, int width){
        AffineTransform at = g2d.getTransform();
        PBArrowTarget target = getArrowTarget(panel);
        BufferedImage image = target.getAnimation().getActualImage();
        g2d.translate(x,y);
        g2d.scale((double) width/this.width,(double) width/this.width);
        g2d.translate(-image.getWidth()/2,-image.getHeight()/2);
        g2d.drawImage(image,0,0,null);
        GraphicsManipulator.resetGraphics(g2d,at);
    }

    public PBArrowTarget getArrowTarget(int panel){
        return switch (panel) {
            case 1 -> arrowLeft;
            case 2 -> arrowDown;
            case 3 -> arrowUp;
            default -> arrowRight;
        };
    }
}
