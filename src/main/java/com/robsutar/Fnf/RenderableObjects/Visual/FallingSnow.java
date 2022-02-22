package com.robsutar.Fnf.RenderableObjects.Visual;

import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Threads.Renderable;
import com.robsutar.Engine.Threads.Ticable;
import com.robsutar.Engine.Window.WindowManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallingSnow implements Renderable, Ticable {
    private List<Snow> snows;
    private final int snowAmount,w;
    private int speed = 5;

    private Random rnd = new Random();

    public FallingSnow(int snowAmount, int width ){
        this.snowAmount=snowAmount;
        this.w=width;

        buildSnows();

        spawnAll();
    }

    private void buildSnows(){
        snows = new ArrayList<>();
        for (int i = 0; i < snowAmount;i++){
            snows.add(new Snow());
        }
    }

    @Override
    public void tick() {
        for (Snow s:snows){
            s.tick();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        for (Snow s:snows){
            GraphicsManipulator.resetGraphics(g2d);
            s.render(g2d);
        }
    }
    private class Snow extends Rectangle{
        float rotation = 0;
        int velocity;

        Color color = Color.white;

        private Snow(){
            resetLocation();
        }

        private void resetLocation(){
            int siz = rnd.nextInt(w)+w/2;
            setSize(siz,siz);
            color = EngineVisuals.changeOpacity(color,rnd.nextInt(100)/100f);
            velocity = rnd.nextInt(speed);
            setLocation(rnd.nextInt(WindowManager.getWidth())-WindowManager.getHeight(),-rnd.nextInt(WindowManager.getHeight()));
        }

        private void tick(){
            int factor = velocity;
            rotation+=factor;
            setLocation(x+factor,y+factor);

            if (x>WindowManager.getWidth()||y>WindowManager.getHeight()) {
                resetLocation();
            }
        }

        private void render(Graphics2D g2d){
            g2d.translate(x,y);
            g2d.rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);

            g2d.setColor(color);
            g2d.fillRect(0,0, width, height);
        }
    }
}
