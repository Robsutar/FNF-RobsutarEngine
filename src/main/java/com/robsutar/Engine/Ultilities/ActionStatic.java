package com.robsutar.Engine.Ultilities;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ActionStatic {
    private ActionListener doAction;
    private ActionListener undoAction;

    public ActionStatic(ActionListener doA, ActionListener undoA)
    {
        doAction = doA;
        undoAction = undoA;
    }

    public void doAction()
    {
        Do();
        toRedo = new ArrayList<>();
    }
    private void Do()
    {
        doAction.actionPerformed(null);
        toUndo.add(this);
    }
    private void Undo()
    {
        undoAction.actionPerformed(null);
        toRedo.add(this);
    }

    private static List<ActionStatic> toUndo = new ArrayList<>();
    private static List<ActionStatic> toRedo = new ArrayList<>();

    public static void RedoAction()
    {
        if (toRedo.toArray().length>0)
        {
            toRedo.get(toRedo.toArray().length-1).Do();
            toRedo.remove(toRedo.get(toRedo.toArray().length - 1));
        }
    }
    public static void UndoAction()
    {
        if (toUndo.toArray().length>0)
        {
            toUndo.get(toUndo.toArray().length - 1).Undo();
            toUndo.remove(toUndo.get(toUndo.toArray().length - 1));
        }
    }
}
