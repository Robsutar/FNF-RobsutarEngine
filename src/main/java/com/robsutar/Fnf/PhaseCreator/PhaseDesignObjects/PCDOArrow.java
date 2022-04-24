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
            super(Assets.ARROW_ANIMATION.animationConfigurations.get(9).atlas.get(0).image, 3,Color.green);
        }
    }
    public static final class ArrowDown extends PCDOArrow {
        public ArrowDown() {
            super(Assets.ARROW_ANIMATION.animationConfigurations.get(4).atlas.get(0).image, 2,Color.CYAN);
        }
    }
    public static final class ArrowLeft extends PCDOArrow {
        public ArrowLeft() {
            super(Assets.ARROW_ANIMATION.animationConfigurations.get(15).atlas.get(0).image, 1,Color.MAGENTA);
        }
    }
    public static final class ArrowRight extends PCDOArrow {
        public ArrowRight() {
            super(Assets.ARROW_ANIMATION.animationConfigurations.get(17).atlas.get(0).image, 4,Color.RED);
        }
    }
}
