package com.robsutar.Fnf.PhaseCreator;

import com.robsutar.Assets;
import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Fnf.AnimationBuilder.Animation;
import com.robsutar.Fnf.AnimationBuilder.AnimationFileHook;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Arrows {

    public static class Arrow{
        private final Animation animation = new Animation(Assets.ARROW_ANIMATION);

        final String name,color;
        
        private Arrow(String name,String color){
            this.name=name;this.color=color;
            playIdle();
        }
        
        public void playIdle(){
            animation.playAnimation(name+"Idle");
        }
        public void playHoldPiece(){
            animation.playAnimation(color+"HoldPiece");
        }
        public void playHoldEnd(){
            animation.playAnimation(color+"HoldEnd");
        }
        public void playPress(){
            animation.playAnimation(name+"Press");
        }
        public void playConfirm(){
            animation.playAnimation(name+"Confirm");
        }

        public Animation getAnimation() {
            return animation;
        }

        public void render(Graphics2D g2d, int x, int y, double scale){
            BufferedImage image = animation.getActualImage();
            AffineTransform at = g2d.getTransform();
            int fX = -animation.getActualFrameX()-animation.getActualWidth()/2;
            int fY = -animation.getActualFrameY()-animation.getActualHeight()/2;
            g2d.scale(scale,scale);
            g2d.drawImage(image, fX+x,fY+y,null);
            GraphicsManipulator.resetGraphics(g2d,at);
        }
    }
    public static class ArrowUp extends Arrow{
        public ArrowUp() {
            super("up","green");
        }
    }
    public static class ArrowDown extends Arrow{
        public ArrowDown() {
            super("down","blue");
        }
    }
    public static class ArrowLeft extends Arrow{
        public ArrowLeft() {
            super("left","purple");
        }
    }
    public static class ArrowRight extends Arrow{
        public ArrowRight() {
            super("right","red");
        }
    }
}
