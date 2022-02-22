package com.robsutar.Fnf.PhaseCreator.PhaseDesignObjects;

import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.GraphicsManipulator;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PCDObject {
    protected BufferedImage image;
    protected Color selectedColor = EngineVisuals.changeOpacity(EngineVisuals.SPRAY,0.5f);
    protected Color color = EngineVisuals.SPRAY;
    protected boolean selected = false;
    protected boolean temporarySelected = false;
    protected int length;
    protected int priority = 0;
    private float selectedOpacity=0f;

    public static PCDObject getByPriority(int priority){
        return switch (priority) {
            case 1 -> new PCDOArrow.ArrowLeft();
            case 2 -> new PCDOArrow.ArrowDown();
            case 3 -> new PCDOArrow.ArrowUp();
            case 4 -> new PCDOArrow.ArrowRight();
            default -> null;
        };
    }

    public void setSelected(boolean selected){
        this.selected=selected;
    }
    public void changeSelect(){
        selected = !selected;
    }

    public void setTemporarySelected(boolean selected){
        this.temporarySelected=selected;
        if (selected) {
            selectedOpacity = 1.5f;
        }
    }

    public void setSelectedOpacity(float opacity) {
        this.selectedOpacity = opacity;
        if (selectedOpacity>1.5f){
            selectedOpacity=1.5f;
        } else if (selectedOpacity<0f){
            selectedOpacity = 0f;
        }
    }

    public void setLength(int length) {
        this.length = Math.max(0,length);
    }

    public boolean isSelected() {
        return selected;
    }

    public int getPriority(){return priority;}

    public void render(Graphics2D g2d,int x, int y,int pointDistance){
        g2d.translate(x,y);

        renderSlider(g2d,pointDistance);

        g2d.setColor(selectedColor);
        renderSelected(g2d);
        GraphicsManipulator.setOpacity(g2d,1f);
        renderImage(g2d);

        if (temporarySelected) {
            setSelectedOpacity(selectedOpacity-0.2f);
            if(selectedOpacity <= 0) {
                temporarySelected = false;
            }
        }
    }

    protected void renderSelected(Graphics2D g2d){
        if (image!=null){
            if (selected) {
                g2d.fillRect(-image.getWidth()/2,-image.getHeight()/2,
                    image.getWidth(),image.getHeight());
                g2d.setColor(color);
                g2d.drawRect(-image.getWidth()/2,-image.getHeight()/2,
                        image.getWidth(),image.getHeight());
            } else if (temporarySelected){
                GraphicsManipulator.setOpacity(g2d,selectedOpacity);
                g2d.fillRect(-image.getWidth()/2,-image.getHeight()/2,
                        image.getWidth(),image.getHeight());
            }
        }
    }

    protected void renderImage(Graphics2D g2d){
        if (image!=null){
            g2d.drawImage(image,-image.getWidth()/2,-image.getHeight()/2,null);
        }
    }

    protected void renderSlider(Graphics g2d,int pointDistance){
        if (length>0) {
            int width = image.getWidth() / 3;
            g2d.setColor(selectedColor);
            g2d.fillRect(-width / 2, 0, width, length*pointDistance);
            g2d.setColor(color);
            g2d.drawRect(-width / 2, 0, width, length*pointDistance);
        }
    }
}
