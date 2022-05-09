package com.robsutar.Engine;

import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Helpers.KeyManager;
import com.robsutar.Engine.Helpers.PauseableThread;
import com.robsutar.Engine.Threads.*;
import com.robsutar.Engine.Ultilities.ActionStatic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
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

    public static List<SimpleThread> ISimpleThreads = new ArrayList<>();
    public static void addObject(SimpleThread o) {ISimpleThreads.add(o);}
    public static void removeObject(SimpleThread o) {ISimpleThreads.remove(o);}

    public static void dispose() {
        List<FullSpawn> fullSpawns = new ArrayList<>();
        fullSpawns.addAll(ITicables);
        fullSpawns.addAll(IRenderables);
        fullSpawns.addAll(IBpmTicables);
        fullSpawns.addAll(IKeyboardInteractives);
        fullSpawns.addAll(ISimpleThreads);
        for(FullSpawn f:fullSpawns){
            f.killAll();
        }
    }

    public static void tick(){
        List<Ticable> Ticables = new ArrayList<>(ITicables);
        for(Ticable i: Ticables){
            i.tick();
        }
    }

    public static void render(Graphics2D g2d){
        AffineTransform at = g2d.getTransform();
        List<Renderable> Renderables = new ArrayList<>(IRenderables);

        GraphicsManipulator.resetGraphics(g2d,at);
        for(Renderable i: Renderables){
            i.render(g2d);
            GraphicsManipulator.resetGraphics(g2d,at);
        }
        GraphicsManipulator.resetGraphics(g2d,at);
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
