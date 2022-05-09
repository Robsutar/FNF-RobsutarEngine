package com.robsutar.Fnf.PhaseInit;

import com.robsutar.Fnf.AnimationBuilder.AnimationFileHook;
import com.robsutar.Fnf.AnimationLoader.AnimFileHook;

import java.io.File;

public class PhaseAnimatedFile extends PhaseFile{
    final AnimFileHook animation;
    public PhaseAnimatedFile(File jsonFile) {
        super(jsonFile);
        animation = new AnimFileHook(jsonFile);
    }

    @Override
    public AnimFileHook getObject() {
        return animation;
    }
}
