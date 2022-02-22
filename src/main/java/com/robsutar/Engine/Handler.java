package com.robsutar.Engine;

import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Helpers.KeyManager;
import com.robsutar.Engine.Threads.BpmTicable;
import com.robsutar.Engine.Threads.KeyboardInteractive;
import com.robsutar.Engine.Threads.Renderable;
import com.robsutar.Engine.Threads.Ticable;
import com.robsutar.Engine.Ultilities.ActionStatic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public final class Handler {

    public static List<Ticable> ITicables = new ArrayList<>();
    public static void addObject(Ticable o) {ITicables.add(o);}
    public static void removeObject(Ticable o) {ITicables.remove(o);}

    public static List<Renderable> IRenderables = new ArrayList<>();
    public static void addObject(Renderable o) {IRenderables.add(o);}
    public static void removeObject(Renderable o) {IRenderables.remove(o);}

    public static List<BpmTicable> IBpmTicables = new ArrayList<>();
    public static void addObject(BpmTicable o) {IBpmTicables.add(o);}
    public static void removeObject(BpmTicable o) {IBpmTicables.remove(o);}

    public static List<KeyboardInteractive> IKeyboardInteractives = new ArrayList<>();
    public static void addObject(KeyboardInteractive o) {IKeyboardInteractives.add(o);}
    public static void removeObject(KeyboardInteractive o) {IKeyboardInteractives.remove(o);}

    public static void tick(){
        List<Ticable> Ticables = new ArrayList<>(ITicables);
        for(Ticable i: Ticables){
            i.tick();
        }
    }

    public static void render(Graphics2D g2d){
        List<Renderable> Renderables = new ArrayList<>(IRenderables);

        GraphicsManipulator.resetGraphics(g2d);
        for(Renderable i: Renderables){
            i.render(g2d);
            GraphicsManipulator.resetGraphics(g2d);
        }
        GraphicsManipulator.resetGraphics(g2d);
    }

    public static void bpmTick(long age){
        List<BpmTicable> BpmTicables = new ArrayList<>(IBpmTicables);
        for(BpmTicable i: BpmTicables){
            i.bpmTick(age);
        }
    }

    public static void keyPressed(KeyEvent e){
        KeyManager.keyPressed(e);

        if (e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_Z){
            ActionStatic.UndoAction();
        } else if (e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_Y){
            ActionStatic.RedoAction();
        }

        List<KeyboardInteractive> KeyboardInteractives = new ArrayList<>(IKeyboardInteractives);
        for(KeyboardInteractive i: KeyboardInteractives){
            i.keyPressed(e);
        }
    }
    public static void keyReleased(KeyEvent e){
        KeyManager.keyReleased(e);

        List<KeyboardInteractive> KeyboardInteractives = new ArrayList<>(IKeyboardInteractives);
        for(KeyboardInteractive i: KeyboardInteractives){
            i.keyReleased(e);
        }
    }
}
