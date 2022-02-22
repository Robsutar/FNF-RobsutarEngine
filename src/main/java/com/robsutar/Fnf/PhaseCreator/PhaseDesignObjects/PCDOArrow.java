package com.robsutar.Fnf.PhaseCreator.PhaseDesignObjects;

import com.robsutar.Assets;
import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.GraphicsManipulator;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PCDOArrow extends PCDObject{
    public PCDOArrow(BufferedImage arrowImage, int priority,Color color){
        image = arrowImage;this.priority=priority;
        this.selectedColor = EngineVisuals.changeOpacity(color,0.5f);this.color = color;
    }
    public static final class ArrowUp extends PCDOArrow {
        public ArrowUp() {
            super(GraphicsManipulator.resizeImage(Assets.ARROW_UP,75,75), 3,Color.green);
        }
    }
    public static final class ArrowDown extends PCDOArrow {
        public ArrowDown() {
            super(GraphicsManipulator.resizeImage(Assets.ARROW_DOWN,75,75), 2,Color.CYAN);
        }
    }
    public static final class ArrowLeft extends PCDOArrow {
        public ArrowLeft() {
            super(GraphicsManipulator.resizeImage(Assets.ARROW_LEFT,75,75), 1,Color.MAGENTA);
        }
    }
    public static final class ArrowRight extends PCDOArrow {
        public ArrowRight() {
            super(GraphicsManipulator.resizeImage(Assets.ARROW_RIGHT,75,75), 4,Color.RED);
        }
    }
}
