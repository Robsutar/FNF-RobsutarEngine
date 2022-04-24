package com.robsutar.Fnf.AnimationBuilder;

import com.robsutar.Engine.Helpers.SystemPrinter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class Animation {
    public final AnimationFileHook fileHook;
    private BufferedImage actualImage;
    private AnimationFileHook.AnimationConfiguration actualConfiguration;
    private int actualPosition;

    private int actualFrameX,actualFrameY;
    private int actualWidth,actualHeight;

    public Animation(AnimationFileHook fileHook) {
        this.fileHook = fileHook;
        actualConfiguration = fileHook.animationConfigurations.get(0);
        nextAnimationFrame();
        actualConfiguration = fileHook.animationConfigurations.get(0);
        actualWidth = actualConfiguration.atlas.get(0).image.getWidth();
        actualHeight = actualConfiguration.atlas.get(0).image.getHeight();
        actualPosition = 0;
    }

    public void playAnimation(String command){
        for(AnimationFileHook.AnimationConfiguration c: fileHook.animationConfigurations){
            if (Objects.equals(c.command, command)||Objects.equals(c.name, command)){
                playAnimation(c);
                break;
            }
        }
    }

    public void playAnimation(AnimationFileHook.AnimationConfiguration config){
        actualConfiguration = config;
        actualWidth = actualConfiguration.atlas.get(0).image.getWidth();
        actualHeight = actualConfiguration.atlas.get(0).image.getHeight();
        actualPosition = 0;
        nextAnimationFrame();
    }

    public void nextAnimationFrame() {
        if(actualPosition >= actualConfiguration.atlas.size()) {
            if (actualConfiguration.shouldLoop) {
                actualPosition = 0;
            } else {
                actualPosition = (actualConfiguration.atlas.size()-1);
            }
        }
        if (actualPosition<0){
            actualPosition=0;
        }
        AnimationFileHook.AtlasImage actualAtlas = actualConfiguration.atlas.get(actualPosition);
        actualImage = actualAtlas.image;
        actualFrameX = actualAtlas.frameX-actualConfiguration.x;
        actualFrameY = actualAtlas.frameY-actualConfiguration.y;
        actualPosition++;
    }

    public BufferedImage getActualImage() {
        return actualImage;
    }

    public int getActualFrameX() {
        return actualFrameX;
    }

    public int getActualFrameY() {
        return actualFrameY;
    }

    public int getActualWidth() {
        return actualWidth;
    }

    public int getActualHeight() {
        return actualHeight;
    }
}
