package com.robsutar.Fnf.AnimationLoader;

import com.robsutar.Engine.Files.ImageFile;
import com.robsutar.Engine.Files.JsonFile;
import com.robsutar.Engine.Files.XmlFile;
import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Helpers.PrintColor;
import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Fnf.AnimationBuilder.AnimationFileHook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class AnimFileHook {
    public List<AnimationConfiguration> animationConfigurations;
    public final XmlFile xmlFile;
    public JsonFile jsonFile;
    public String imageName;

    public AnimFileHook(XmlFile xml, ImageFile image,JsonFile json){
        xmlFile = xml;
        imageName = image.getName();
        jsonFile=json;
        construct(xml.xml,image.image);
        setJson(json.json);
    }

    public AnimFileHook(File file){
        jsonFile = new JsonFile(file);
        String folder = file.getParent()+"\\";
        xmlFile = new XmlFile(FileManager.loadFile(folder+jsonFile.json.get("xml")));

        construct(xmlFile.xml,FileManager.loadImage(FileManager.loadFile(folder+jsonFile.json.get("image"))));
        setJson(jsonFile.json);
    }

    private void construct(Document xml, BufferedImage image) {
        animationConfigurations = new ArrayList<>();

        Function<String, Integer> getXmlInt = (s) -> {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return 0;
            }
        };

        NodeList list = xml.getElementsByTagName("SubTexture");

        List<AnimationConfiguration> configurations = new ArrayList<>();

        List<AtlasImage> atlas = new ArrayList<>();
        String name = null;

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String elementName = element.getAttribute("name");
                elementName = elementName.substring(0, elementName.length() - 4);

                if(name == null) {
                    name = elementName;
                }
                int x = getXmlInt.apply(element.getAttribute("x"));
                int y = getXmlInt.apply(element.getAttribute("y"));
                int width = Math.max(1,getXmlInt.apply(element.getAttribute("width")));
                int height = Math.max(1,getXmlInt.apply(element.getAttribute("height")));
                int frameX = getXmlInt.apply(element.getAttribute("frameX"));
                int frameY = getXmlInt.apply(element.getAttribute("frameY"));

                BufferedImage atlasImage= image.getSubimage(x, y, width, height);
                AtlasImage atlasV = new AtlasImage(frameX, frameY, atlasImage);

                if(!elementName.equals(name) || i == list.getLength() - 1) {
                    configurations.add(new AnimationConfiguration(name, atlas));
                    atlas = new ArrayList<>();
                }
                atlas.add(atlasV);
                name = elementName;
            }
        }

        this.animationConfigurations = configurations;
    }

    private void updateAnimations(){
        JSONArray animations = new JSONArray();
        for(AnimationConfiguration animC:animationConfigurations) {
            animations.add(getAnimationConfigs(animC));
        }
        jsonFile.json.replace("animations",animations);
    }

    public void setJson (JSONObject jsonObject){
        JSONArray json = (JSONArray)jsonObject.get("animations");
        List<JSONObject> jsonObjects = new ArrayList<>();
        for(Object o:json){
            jsonObjects.add((JSONObject) o);
        }
        l1:for(AnimationConfiguration animC:animationConfigurations){
            for(JSONObject object:jsonObjects){
                try {
                    String name = String.valueOf(object.get("name"));
                    if(Objects.equals(name, animC.name)){
                        String command = String.valueOf(object.get("command"));
                        JSONArray offsets = (JSONArray) object.get("offsets");
                        boolean loop = Boolean.parseBoolean(String.valueOf(object.get("loop")));
                        animC.command=command;
                        animC.x =Integer.parseInt(String.valueOf(offsets.get(0)));
                        animC.y =Integer.parseInt(String.valueOf(offsets.get(1)));
                        animC.shouldLoop=loop;
                        jsonObjects.remove(object);
                        continue l1;
                    }
                }catch (Exception e){
                    SystemPrinter.print(PrintColor.RED+"exception at index: "+PrintColor.RED_BOLD_BRIGHT+jsonObjects.indexOf(object));
                }
            }
            json.add(getAnimationConfigs(animC));

        }
        jsonObject.replace("animations",json);
        updateAnimations();
    }

    public static JSONObject getAnimationConfigs(AnimationConfiguration animC){
        JSONObject object = new JSONObject();
        JSONArray offsets = new JSONArray();
        offsets.add(animC.x);
        offsets.add(animC.y);
        object.put("name",animC.name);
        object.put("loop",animC.shouldLoop);
        object.put("offsets",offsets);
        object.put("command",animC.command);
        return object;
    }

    public JSONObject getJson(){return jsonFile.json;}

    public class AtlasImage{
        public final int frameX,frameY;
        public final BufferedImage image;

        public AtlasImage(int frameX, int frameY, BufferedImage image) {
            this.frameX = frameX;
            this.frameY = frameY;
            this.image = image;
        }

    }

    public class AnimationConfiguration{
        public int x,y;
        public final String name;
        public String command;
        public final List<AtlasImage> atlas;
        public boolean shouldLoop = false;

        private AnimationConfiguration(String name, List<AtlasImage> atlas) {
            this.name = name;
            this.atlas=atlas;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
