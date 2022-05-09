package com.robsutar.Fnf.PhaseInit;

import com.robsutar.Fnf.AnimationLoader.Anim;

import java.util.ArrayList;
import java.util.List;

public class PhasePlayerContents {
    public final List<PhaseObject> objects = new ArrayList<>();

    public Anim anchorAnim;

    public void refactor(){
        objects.sort((e1,e2) -> (int) (e1.index-e2.index));
    }
}
