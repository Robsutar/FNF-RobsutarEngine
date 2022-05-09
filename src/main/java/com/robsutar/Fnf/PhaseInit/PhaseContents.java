package com.robsutar.Fnf.PhaseInit;

import com.robsutar.Engine.Audio.Music;
import com.robsutar.Engine.Helpers.FileManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhaseContents {

    public final String folder;
    public final String difficultName;
    public final String musicName;
    public final Music music;
    public final List<PhasePlayerContents> phasePlayerContents = new ArrayList<>();
    public final List<Long>pits= new ArrayList<>();
    public final List<PhasePart>parts= new ArrayList<>();
    public final List<PhaseAnimatedFile> animatedFiles = new ArrayList<>();

    public PhaseContents(File file){
        JSONObject phaseFile = FileManager.loadJson(file);

        folder = file.getParent()+"\\";
        difficultName = file.getName();

        JSONArray pitsArray = (JSONArray) phaseFile.get("pits");
        JSONArray partsArray = (JSONArray) phaseFile.get("parts");
        JSONArray playersArray = (JSONArray) phaseFile.get("playerContents");

        JSONObject assetsJson = (JSONObject) phaseFile.get("assets");
        JSONArray animatedArray = (JSONArray) assetsJson.get("animated");

        for(Object o:pitsArray){
            pits.add(Long.parseLong(String.valueOf(o)));
        }
        for(Object o:partsArray){
            JSONObject object = (JSONObject)o;
            double bpm = Double.parseDouble(String.valueOf(object.get("bpm")));
            long index = Long.parseLong(String.valueOf(object.get("index")));
            parts.add(new PhasePart(index,bpm));
        }
        for(Object o:playersArray) {
            PhasePlayerContents players = new PhasePlayerContents();
            JSONObject object = (JSONObject) o;
            JSONArray objects =(JSONArray) object.get("objects");
            for(Object o1:objects){
                JSONArray indexAndIdentifier = (JSONArray) o1;
                int identifier = Integer.parseInt(String.valueOf(indexAndIdentifier.get(0)));
                long index = Long.parseLong(String.valueOf(indexAndIdentifier.get(1)));
                PhaseObject phaseObject = new PhaseObject(identifier,index);
                players.objects.add(phaseObject);
            }
            phasePlayerContents.add(players);
        }

        musicName = String.valueOf(assetsJson.get("music"));
        music = new Music(FileManager.loadWav(FileManager.loadFile(getAssetsFolder()+musicName)));
        for(Object o:animatedArray){
            animatedFiles.add(new PhaseAnimatedFile(FileManager.loadFile(getAssetsFolder()+o)));
        }
    }

    public JSONObject getPhaseFile(){
        JSONObject phaseFile = new JSONObject();

        JSONArray phasePits = new JSONArray();
        phasePits.addAll(pits);

        JSONArray phaseParts = new JSONArray();
        for(PhasePart p:parts){
            JSONObject array = new JSONObject();
            array.put("bpm",p.bpm);
            array.put("index",p.index);
            phaseParts.add(array);
        }

        JSONArray players = new JSONArray();
        for(PhasePlayerContents p: phasePlayerContents){
            JSONObject exit = new JSONObject();
            JSONArray objects = new JSONArray();
            for(PhaseObject o:p.objects){
                JSONArray ob = new JSONArray();
                ob.add(o.identification);
                ob.add(o.index);
                objects.add(ob);
            }
            exit.put("objects",objects);
            players.add(exit);
        }

        JSONObject assets = new JSONObject();
        JSONArray animated = new JSONArray();
        for(PhaseAnimatedFile a:animatedFiles){
            animated.add(a.getName());
        }

        assets.put("animated",animated);
        assets.put("music",musicName);

        phaseFile.put("pits",phasePits);
        phaseFile.put("parts",phaseParts);
        phaseFile.put("playerContents",players);
        phaseFile.put("assets",assets);
        return phaseFile;
    }

    public void saveContents(){
        refactorAll();
        FileManager.writeFile(getPhaseFile(),FileManager.loadFile(folder+difficultName));
    }

    public String getFolder(){return folder;}
    public String getAssetsFolder(){return getFolder()+"assets\\";}

    public void refactorAll(){
        for (PhasePlayerContents p:phasePlayerContents){
            p.refactor();
        }
    }
}
