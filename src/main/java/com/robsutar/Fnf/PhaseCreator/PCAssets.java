package com.robsutar.Fnf.PhaseCreator;

import com.formdev.flatlaf.json.Json;
import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Helpers.PrintColor;
import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Fnf.Animated.AnimatedObject;
import com.robsutar.Fnf.AnimationBuilder.Animation;
import com.robsutar.Fnf.AnimationBuilder.AnimationFileHook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PCAssets {
    final PCMainPanel handler;

    public List<AnimatedFile> animatedObjects = new ArrayList<>();

    public PCAssets(PCMainPanel handler){
        this.handler=handler;
        try{
            JSONObject file =(JSONObject) handler.phaseJson.get("assets");
            JSONArray animated =(JSONArray) file.get("animated");
            for(Object o:animated){
                JSONObject object = (JSONObject)o;
                String fileName = String.valueOf(object.get("name"));
                File f = FileManager.loadFile(handler.assetsFolder + fileName);
                addAnimatedObject(f);
            }
        }catch (Exception ignored){}
    }

    public void addAnimatedObject(File file){
        AnimatedObject o = new AnimatedObject(new Animation(new AnimationFileHook(file)));
        animatedObjects.add(new AnimatedFile(o,file));
    }

    public JSONObject getAssetsJson(){
        JSONObject exit = new JSONObject();
        JSONArray animated =new JSONArray();

        for(AnimatedFile a:animatedObjects){
            JSONObject o = new JSONObject();
            JSONArray offsets = new JSONArray();
            offsets.add(a.object.x);offsets.add(a.object.y);

            o.put("name",a.file.getName());

            animated.add(o);
        }

        exit.put("animated",animated);
        return exit;
    }
    public class AnimatedFile{
        public final AnimatedObject object;
        public final File file;
        private AnimatedFile(AnimatedObject object, File file){
            this.object = object;

            this.file = file;
        }

        @Override
        public String toString() {
            return file.getName();
        }
    }
}
