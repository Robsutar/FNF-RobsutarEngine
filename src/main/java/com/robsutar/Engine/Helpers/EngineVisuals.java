package com.robsutar.Engine.Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class EngineVisuals {
    public static final Color WET_ASPHALT = new Color(52, 73, 94);
    public static final Color MIDNIGHT_ASPHALT = new Color(50, 69, 89);
    public static final Color MIDNIGHT_BLUE = new Color(44, 62, 80);

    public static final Color SPRAY = new Color(130, 204, 221);
    public static final Color DUPAIN = new Color(96, 163, 188);
    public static final Color GOOD_SAMARITAN = new Color(96, 163, 188);
    public static final Color FOREST_BLUES = new Color(10, 61, 98);

    public static final Color BACKGROUND_COLOR_0 = MIDNIGHT_BLUE;
    public static final Color BACKGROUND_COLOR_1 = WET_ASPHALT;
    public static final Color BACKGROUND_COLOR_2 = MIDNIGHT_ASPHALT;
    public static final Color BACKGROUND_COLOR_3 = SPRAY;

    public static final Color MENU_COLOR_0 = WET_ASPHALT;

    public static final Color SHADOW_COLOR = new Color(0,0,0,128);

    public static Color changeOpacity(Color c,float opacity){
        return new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(255*opacity));
    }
    public static Color randomColor(){
        Random rnd = new Random();
        return new Color(rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));
    }
    public static BufferedImage resizeImage(BufferedImage image,int targetWidth,int targetHeight){
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        g2d.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }
}
