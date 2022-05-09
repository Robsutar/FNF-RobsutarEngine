package com.robsutar.Engine.Files;

import java.io.File;

public abstract class AssetsFile {
    private final String name;
    public AssetsFile(File file){
        this.name= file.getName();
    }
    public AssetsFile(String name){
        this.name= name;
    }

    public String getName() {
        return name;
    }
}
