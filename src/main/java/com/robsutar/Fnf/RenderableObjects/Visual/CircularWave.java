package com.robsutar.Fnf.RenderableObjects.Visual;

import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Threads.Renderable;
import com.robsutar.Engine.Threads.Ticable;

import java.awt.*;

public class CircularWave implements Renderable, Ticable {
    Color color;
    private int age = 0,x,y;
    private final int ageLimit;
    private float opacity = 1f;

    BasicStroke stroke;

    public CircularWave(Color c, int ageLimit,int thickness, int x, int y){
        color = c;this.ageLimit=ageLimit;this.x=x;this.y=y;
        this.stroke = new BasicStroke(thickness);
    }

    @Override
    public void render(Graphics2D g2d) {
        GraphicsManipulator.setOpacity(g2d,opacity);
        g2d.setColor(color);
        g2d.setStroke(stroke);

        int w = age*5;

        g2d.drawOval(x-w/2,y-w/2,w,w);
    }

    @Override
    public void tick() {
        age++;
        if (age>ageLimit){
            opacity-=0.05f;
        }
        if (opacity<=0){
            killAll();
        }
    }
}
