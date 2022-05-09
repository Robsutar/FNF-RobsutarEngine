package com.robsutar.Engine.Files;

import com.robsutar.Engine.Helpers.FileManager;
import org.json.simple.JSONObject;

import java.io.File;

public class JsonFile extends AssetsFile{
    public final JSONObject json;
    public JsonFile(File file) {
        super(file);
        json= FileManager.loadJson(file);
    }
    public JsonFile(JSONObject json,String name) {
        super(name);
        this.json=json;
    }
}
