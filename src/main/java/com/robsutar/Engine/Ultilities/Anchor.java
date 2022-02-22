package com.robsutar.Engine.Ultilities;

import com.robsutar.Engine.Window.WindowManager;

public abstract class Anchor {

    public static AnchorMiddle ANCHOR_MIDDLE = new AnchorMiddle();

    public abstract int getX();
    public abstract int getY();

    private static class AnchorMiddle extends Anchor{

        @Override
        public int getX() {
            return WindowManager.getWidth()/2;
        }

        @Override
        public int getY() {
            return WindowManager.getHeight()/2;
        }
    }
}
