package com.robsutar.Engine.Helpers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class GraphicsManipulator {

    public static AffineTransform standardTransform = new AffineTransform();

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public static void setOpacity(Graphics2D g2d,float opacity){
        if (opacity>1f){opacity = 1f;} else if (opacity<0){opacity=0f;}
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
    }
    public static void resetGraphics(Graphics2D g2d,AffineTransform at){
        resetGraphics(g2d);
        g2d.setTransform(at);
    }
    public static void resetGraphics(Graphics2D g2d){
        g2d.setColor(Color.white);
        g2d.setTransform(standardTransform);
        setOpacity(g2d,1f);
    }
}
