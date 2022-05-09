package com.robsutar.Fnf.PhaseInit;

import java.io.File;

public abstract class PhaseFile {
    final File file;
    public PhaseFile(File file){
        this.file=file;
    }
    public String getName(){
        return file.getName();
    }
    public abstract Object getObject();
}
