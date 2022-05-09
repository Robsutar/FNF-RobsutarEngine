package com.robsutar.Fnf.PhaseCreator.Tabs;

import com.robsutar.Assets;
import com.robsutar.Engine.Audio.OnePlayAudio;
import com.robsutar.Engine.Helpers.*;
import com.robsutar.Engine.Ultilities.ActionStatic;
import com.robsutar.Fnf.Animated.AnimatedObject;
import com.robsutar.Fnf.PhaseCreator.Arrows;
import com.robsutar.Fnf.AnimationBuilder.Animation;
import com.robsutar.Fnf.AnimationBuilder.AnimationFileHook;
import com.robsutar.Fnf.KeyConfiguration;
import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.robsutar.Fnf.PhaseCreator.PCTab;
import com.robsutar.Fnf.PhaseCreator.PhaseDesignObjects.PCDOArrow;
import com.robsutar.Fnf.PhaseCreator.PhaseDesignObjects.PCDObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PCDesignTab extends PCTab {

    public final MiddlePanel middle;

    public final LeftPanel left;

    public final RightPanel right;

    int zoom = 1000;

    long visualPosition = 0;
    AnimatedObject a1;
    AnimatedObject a2;
    OnePlayAudio drumHit = new OnePlayAudio(FileManager.loadFile(FileManager.audioPath+"drum-hitnormal.wav"));

    public PCDesignTab(PCMainPanel h) {
        super(h);
        middle = new MiddlePanel();
        left = new LeftPanel();
        right = new RightPanel();

        AnimationFileHook afh1 = new AnimationFileHook(FileManager.loadFile(FileManager.mainAssetsPath+"animated\\BOYFRIEND.json"));
        Animation anim1 = new Animation(afh1);

        a1 = new AnimatedObject(anim1);
        a1.setLocation(500,0);

        AnimationFileHook afh2 = new AnimationFileHook(FileManager.loadFile(FileManager.mainAssetsPath+"animated\\AGOTI.json"));
        Animation anim2 = new Animation(afh2);

        a2 = new AnimatedObject(anim2);
        a2.setLocation(-500,0);
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;

        panel.add(left,c);

        panel.add(middle,c);

        panel.add(right,c);
    }

    @Override
    protected void tick() {
        super.tick();
        a1.bpmTick();
        a2.bpmTick();
        middle.p1.aRight.getAnimation().nextAnimationFrame();
        middle.p1.aLeft.getAnimation().nextAnimationFrame();
        middle.p1.aUp.getAnimation().nextAnimationFrame();
        middle.p1.aDown.getAnimation().nextAnimationFrame();
        middle.p2.aRight.getAnimation().nextAnimationFrame();
        middle.p2.aLeft.getAnimation().nextAnimationFrame();
        middle.p2.aUp.getAnimation().nextAnimationFrame();
        middle.p2.aDown.getAnimation().nextAnimationFrame();
        long musicPos = getMusic().getPosition()-100000;
        if (visualPosition!=musicPos){
            if (visualPosition<musicPos){
                visualPosition+=(musicPos-visualPosition)/5+10;
                if (visualPosition>musicPos){
                    visualPosition = musicPos;
                }
            } else {
                visualPosition-=(visualPosition-musicPos)/5+10;
                if (visualPosition<musicPos){
                    visualPosition = musicPos;
                }
            }
        }
    }

    @Override
    protected void onPaintBackground(Graphics2D g2d) {
        super.onPaintBackground(g2d);
        AffineTransform at = g2d.getTransform();
        a1.render(g2d);
        GraphicsManipulator.resetGraphics(g2d,at);
        a2.render(g2d);
        GraphicsManipulator.resetGraphics(g2d,at);
    }

    private long getPosition(){
        return getMusic().getPosition();
    }

    public JSONObject getPhaseJson(){
        JSONObject exit = new JSONObject();

        JSONArray partsJson = new JSONArray();
        JSONArray p1ObjectsJson = new JSONArray();
        JSONArray p2ObjectsJson = new JSONArray();

        for (MiddlePanel.Part p:middle.parts){
            JSONObject jsonPart = new JSONObject();
            JSONArray partPits = new JSONArray();

            for (long l:p.pits){
                partPits.add(l);
            }

            jsonPart.put("bpm",p.bpm);
            jsonPart.put("index",p.index);
            jsonPart.put("pits",partPits);

            partsJson.add(jsonPart);
        }

        for(MiddlePanel.ArrowsPanel.PhaseObject o:middle.p1.objects){
            JSONArray objectJson = new JSONArray();
            objectJson.add(o.panel);
            objectJson.add(o.index);
            p1ObjectsJson.add(objectJson);
        }

        for(MiddlePanel.ArrowsPanel.PhaseObject o:middle.p2.objects){
            JSONArray objectJson = new JSONArray();
            objectJson.add(o.panel);
            objectJson.add(o.index);
            p2ObjectsJson.add(objectJson);
        }

        exit.put("parts",partsJson);
        exit.put("p1Objects",p1ObjectsJson);
        exit.put("p2Objects",p2ObjectsJson);

        return exit;
    }

    @Override
    protected void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        long index = middle.getNearestPit(100000 / zoom);
        int panel = 0;
        if (e.getKeyCode()==KeyEvent.VK_TAB){
            middle.focusPanel=!middle.focusPanel;
        }
        else if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
            MiddlePanel.ArrowsPanel aPanel = getSelectedArrowsPanel();
            List<MiddlePanel.ArrowsPanel.PhaseObject> objects = new ArrayList<>(aPanel.objects);
            ActionStatic a = new ActionStatic(e1 -> {
                for(MiddlePanel.ArrowsPanel.PhaseObject o:objects){
                    if (o.selected){
                        aPanel.objects.remove(o);
                    }
                }
            },e1 -> {
                for(MiddlePanel.ArrowsPanel.PhaseObject o:objects){
                    if (o.selected){
                        aPanel.objects.add(o);
                    }
                }
            });
            a.doAction();
        }
        else if (e.getKeyCode()==KeyEvent.VK_A&&e.isControlDown()){
            MiddlePanel.ArrowsPanel aPanel = getSelectedArrowsPanel();
            aPanel.changeAllObjectsSelect(true);
        }
        else if(KeyConfiguration.keyEquals(e, KeyConfiguration.ARROW_LEFT)) {
            panel = 1;
        } else if(KeyConfiguration.keyEquals(e, KeyConfiguration.ARROW_DOWN)) {
            panel = 2;
        } else if(KeyConfiguration.keyEquals(e, KeyConfiguration.ARROW_UP)) {
            panel = 3;
        } else if(KeyConfiguration.keyEquals(e, KeyConfiguration.ARROW_RIGHT)) {
            panel = 4;
        }
        if(panel != 0) {
            MiddlePanel.ArrowsPanel aPanel = getSelectedArrowsPanel();
            aPanel.addObject(index, panel);
        }
    }

    private MiddlePanel.ArrowsPanel getSelectedArrowsPanel(){
        if (!middle.focusPanel){
            return middle.p1;
        } else {
            return middle.p2;
        }
    }

    public class MiddlePanel extends JPanel {

        ArrowsPanel p1,p2;
        public final List<Part> parts = new ArrayList<>();
        public final List<Event> events65 = new ArrayList<>();
        int filter = 1;
        int verticalPanelWidth = 100;
        boolean focusPanel = false;

        private MiddlePanel(){
            setOpaque(false);

            GridLayout layout = new GridLayout(1,2);
            layout.setHgap(10);
            setLayout(layout);

            p1=new ArrowsPanel();
            p2 = new ArrowsPanel();

            p2.inLeft=true;
            add(p2);
            add(p1);

            JSONObject file= null;
            try{
                file =(JSONObject) handler.phaseJson.get("mapping");
            }catch (Exception ignored){}
            if (file!=null) {
                {
                    JSONArray parts = (JSONArray) file.get("parts");
                    for (Object o : parts) {
                        JSONObject partJson = (JSONObject) o;
                        long index = Long.parseLong(partJson.get("index").toString());
                        double bpm = Double.parseDouble(partJson.get("bpm").toString());
                        Part p = new Part(index, bpm);
                        addPart(p);
                        JSONArray pits = (JSONArray) partJson.get("pits");
                        for (Object o1 : pits) {
                            long pit = Long.parseLong(o1.toString());
                            p.pits.add(pit);
                        }
                    }

                }//parts

                {
                    List<ArrowsPanel> p1andp2 = new ArrayList<>();
                    p1andp2.add(p1);
                    p1andp2.add(p2);
                    for (ArrowsPanel a : p1andp2) {
                        JSONArray pObjects;
                        if(a == p1) {
                            pObjects = (JSONArray) file.get("p1Objects");
                        } else {
                            pObjects = (JSONArray) file.get("p2Objects");
                        }
                        for (Object o : pObjects) {
                            JSONArray array = (JSONArray) o;
                            long index = Long.parseLong(array.get(1).toString());
                            int panel = Integer.parseInt(array.get(0).toString());
                            a.addObject(index, panel);
                        }
                    }
                }//objects
            }

            refactor();
        }

        private long getIndex(int mouseY){
            return (long) mouseY *zoom+getPosition();
        }

        private int getMouseY(long index){return ((int)((index-getPosition())/zoom));}

        private Long getNearestPit(int mouseY){
            return getNearestPit(getIndex(mouseY),middle.filter);
        }

        private Long getNearestPit(long index,int filter){
            filter = (int) Math.pow(2,filter);
            Part p = getMousePart(index);
            long mouseIndex = index-p.index;
            List<Long> pits = new ArrayList<>(p.pits);
            int indexOfPart = parts.indexOf(p);
            if (indexOfPart+1<parts.size()){
                pits.add(parts.get(indexOfPart+1).pits.get(0));
            }
            int indexOf = parts.indexOf(p);
            if (indexOf+1<parts.size()){
                pits.add(parts.get(indexOf+1).pits.get(0));
            }

            int fIndex = (int) (mouseIndex/(60d/p.bpm*Math.pow(10,6)/16d));

            if (fIndex<0||fIndex>=pits.size()){
                return 0L;
            }

            int pIndex = fIndex;
            int nIndex = fIndex;

            while (pIndex%filter!=0){
                pIndex++;
            }while (nIndex%filter!=0){
                nIndex--;
            }
            long positive;
            long negative;
            try {
                positive = pits.get(pIndex);
                negative = pits.get(nIndex);
            }catch (IndexOutOfBoundsException e){
                return pits.get(pits.size()-1);
            }

            if (Math.abs(index -positive)<Math.abs(index -negative)){
                return positive;
            }else {
                return negative;
            }
        }

        private Part getMousePart(long index){
            for (int i = parts.size(); i > 0;i--){
                Part p = parts.get(i-1);
                if (p.index<index){
                    return p;
                }
            }
            return parts.get(0);
        }

        private int getPanel(int mouseX){
            int panel = 1;
            for(int i =4; i > 0;i--){
                if (verticalPanelWidth*(i-1)<mouseX){
                    panel = i;
                    break;
                }
            }
            return panel;
        }

        public void addPart(long index){
            l1:
            {
                Part part = new Part(index,parts.get(parts.size()-1).bpm);
                for (Part p : parts) {
                    if(p.index == part.index) {
                        break l1;
                    }
                }
                addPart(part);
            }
        }

        public void addPart(Part p){
            parts.add(p);
            refactor();
        }

        public void removePart(Part p){
            parts.remove(p);
            refactor();
        }

        public void refactor(){
            if (parts.isEmpty()){
                parts.add(new Part(0,95));
            }
            parts.sort((d1, d2) -> (int) (d1.index - d2.index));
            for(int i=0;i<parts.size();i++){
                Part p = parts.get(i);
                p.pits=new ArrayList<>();
                long maxAge = getMusic().getMusicMaxAge();
                if (i<parts.size()-1){
                    Part p2 = parts.get(i+1);
                    maxAge = p2.index;
                }
                long distance = (long) (60d/p.bpm*Math.pow(10,6)/16d);
                //long count = 0;
                for(long h = p.index;h<maxAge;h+=distance){
                    p.pits.add(h);
                    /*
                    int filter = (int) Math.pow(2,2);
                    if (h%filter==0) {
                        count++;
                        if (count>64){
                            count=0;
                        }
                        if (count>32){
                            p1.addObject(h, new Random().nextInt(4) + 1);
                        } else {
                            p2.addObject(h, new Random().nextInt(4) + 1);
                        }
                    }
                     */
                }
            }
        }

        public static class Part{
            public long index;
            public double bpm;
            List<Long> pits;

            public Part(long index, double bpm) {
                this.index = index;
                this.bpm=bpm;
            }
        }

        public static class Event{
            public long index;
            private Event(long index){
                this.index=index;
            }
        }

        private class ArrowsPanel extends JPanel{

            private boolean inLeft = false;
            List<PhaseObject> objects = new ArrayList<>();

            Arrows.ArrowUp aUp = new Arrows.ArrowUp();
            Arrows.ArrowDown aDown = new Arrows.ArrowDown();
            Arrows.ArrowLeft aLeft = new Arrows.ArrowLeft();
            Arrows.ArrowRight aRight = new Arrows.ArrowRight();

            public ArrowsPanel(){
                setBackground(EngineVisuals.changeOpacity(Color.black,0.4f));
                setPreferredSize(new Dimension(verticalPanelWidth*4,5));
                setFocusable(true);

                setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

                addMouseWheelListener(this::mouseWheel);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        mouseDown(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                        mouseUp(e);

                    }
                });
            }

            private PhaseObject getNearestObject(long index,int panel){
                for(PhaseObject o:objects){
                    if (o.panel==panel) {
                        if(Math.abs(o.index - index) < (long) verticalPanelWidth * zoom) {
                            return o;
                        }
                    }
                }
                return null;
            }
            private void mouseDown(MouseEvent e) {
            }

            private void mouseUp(MouseEvent e){
                focusPanel=inLeft;
                if(e.getButton() == MouseEvent.BUTTON3) {
                    long index = getNearestPit(e.getY());
                    PhaseObject object = getNearestObject(index,getPanel(e.getX()));
                    if(object != null) {
                        removeObject(object);
                    }
                }else if (!addObject(getNearestPit(e.getY()),getPanel(e.getX()))){
                    if (e.getButton()==MouseEvent.BUTTON1){
                        if (!KeyManager.isKeyPressed(KeyEvent.VK_CONTROL)){
                            changeAllObjectsSelect(false);
                        }
                        PhaseObject o = getObject(getNearestPit(e.getY()),getPanel(e.getX()));
                        if (o!=null){
                            o.selected=!o.selected;
                        }
                    }
                }
            }

            private void addObject(PhaseObject object){
                ActionStatic a = new ActionStatic(e1 -> {
                    objects.add(object);
                },e2 ->{
                    objects.remove(object);
                } );
                a.doAction();
            }
            private void removeObject(PhaseObject object){
                ActionStatic a = new ActionStatic(e1 -> {
                    objects.remove(object);
                },e2 ->{
                    objects.add(object);
                } );
                a.doAction();
            }

            private boolean addObject(long index,int panel){
                PhaseObject o = getObject(index,panel);
                if (o==null) {
                    addObject(new PhaseObject(index, panel));
                    return true;
                }
                return false;
            }

            private PhaseObject getObject(long index,int panel){
                for(PhaseObject o:objects){
                    if (o.index==index&&o.panel==panel){
                        return o;
                    }
                }
                return null;
            }


            private void changeAllObjectsSelect(boolean selectValue){
                for(PhaseObject o:objects){
                    o.selected=selectValue;
                }
            }

            private void mouseWheel(MouseWheelEvent e){
                if (KeyManager.isKeyPressed(KeyEvent.VK_CONTROL)){
                    zoom+=e.getWheelRotation()*zoom/30;
                    zoom = Math.max(100,zoom);
                } else if (KeyManager.isKeyPressed(KeyEvent.VK_ALT)){
                    filter+=e.getWheelRotation();
                    filter=Math.max(0,filter);
                    filter=Math.min(4,filter);
                } else {
                    handler.changeMusicPos(handler.getMusicPosition()+(e.getWheelRotation()*50L*zoom));
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform at = g2d.getTransform();

                if (focusPanel==inLeft){
                    g2d.setColor(Color.ORANGE);
                    g2d.drawLine(0,0,getWidth(),0);
                }

                long pos = getPosition();

                int x = 0;
                double lastBpm = 0;
                int filter = (int) Math.pow(2,middle.filter);
                l1 : for (Part p: parts){
                    for(Long l: p.pits){
                        int y = (int) ((l-pos)/zoom);

                        if (y<0){continue;}
                        if (y>getHeight()){break l1;}

                        int indexOf = p.pits.indexOf(l);

                        if (indexOf==0){
                            if (lastBpm!=p.bpm){
                                g2d.setColor(EngineVisuals.changeOpacity(Color.RED,0.5f));
                            } else {
                                g2d.setColor(EngineVisuals.changeOpacity(Color.GREEN, 0.5f));
                            }
                            lastBpm = p.bpm;
                            g2d.drawLine(0,y,getWidth(),y);
                        }

                        if (p.pits.indexOf(l)%filter==0) {
                            int width = 15;

                            if(indexOf % 32 == 0) {
                                g2d.setColor(Color.white);
                                width += 25;
                            } else if(indexOf % 16 == 0) {
                                g2d.setColor(Color.white);
                                width += 15;
                            } else if(indexOf % 8 == 0) {
                                g2d.setColor(Color.white);
                                width += 15;
                            } else if(indexOf % 4 == 0) {
                                g2d.setColor(Color.red);
                                width += 10;
                            } else if(indexOf % 2 == 0) {
                                g2d.setColor(Color.cyan);
                                width += 5;
                            } else {
                                g2d.setColor(Color.yellow);
                            }
                            if(!inLeft) {
                                x = getWidth();
                                width = -width;
                            }
                            g2d.drawLine(x, y, x + width, y);
                        }
                        GraphicsManipulator.resetGraphics(g2d,at);
                    }
                }

                for (PhaseObject o: objects){
                    int y = (int) ((o.index-pos)/zoom);

                    long distance =o.index - 100000 - pos;
                    AnimatedObject anim;
                    if (inLeft){
                        anim = a2;
                    } else {
                        anim=a1;
                    }
                    if (distance< 10000&&distance>-80000){
                        if (!o.playing){
                            drumHit.play();
                            if (o.panel==1){
                                anim.getAnimation().playAnimation("moveLeft");
                                aLeft.playConfirm();
                            } else if (o.panel==2){
                                anim.getAnimation().playAnimation("moveDown");
                                aDown.playConfirm();
                            } else if (o.panel==3){
                                anim.getAnimation().playAnimation("moveUp");
                                aUp.playConfirm();
                            } else if (o.panel==4){
                                anim.getAnimation().playAnimation("moveRight");
                                aRight.playConfirm();
                            }
                        }
                        o.playing =true;
                    }else {
                        if (o.playing){
                            if (o.panel==1){
                                aLeft.playIdle();
                            } else if (o.panel==2){
                                aDown.playIdle();
                            } else if (o.panel==3){
                                aUp.playIdle();
                            } else if (o.panel==4){
                                aRight.playIdle();
                            }
                        }
                        o.playing =false;
                    }
                    o.render(g2d,y,0.6);
                    GraphicsManipulator.resetGraphics(g2d,at);
                }

                int yArrowsLine = 100000/zoom;
                g2d.setColor(handler.color2);
                g2d.drawLine(0,yArrowsLine,getWidth(),yArrowsLine);

                g2d.translate(verticalPanelWidth/2,100000/zoom);
                aLeft.render(g2d,0,0,0.6);
                g2d.translate(verticalPanelWidth,0);
                aDown.render(g2d, 0, 0, 0.6);
                g2d.translate(verticalPanelWidth,0);
                aUp.render(g2d, 0, 0, 0.6);
                g2d.translate(verticalPanelWidth,0);
                aRight.render(g2d, 0, 0, 0.6);
                GraphicsManipulator.resetGraphics(g2d,at);
            }

            PCDOArrow.ArrowLeft ARROW_LEFT = new PCDOArrow.ArrowLeft();
            PCDOArrow.ArrowDown ARROW_DOWN = new PCDOArrow.ArrowDown();
            PCDOArrow.ArrowUp ARROW_UP = new PCDOArrow.ArrowUp();
            PCDOArrow.ArrowRight ARROW_RIGHT = new PCDOArrow.ArrowRight();
            public PCDObject getObjectByPriority(int priority){
                return switch (priority) {
                    case 1 -> ARROW_LEFT;
                    case 2 -> ARROW_DOWN;
                    case 3 -> ARROW_UP;
                    case 4 -> ARROW_RIGHT;
                    default -> null;
                };
            }

            private class PhaseObject{
                long index;
                int panel;
                final PCDObject object;

                boolean playing = false;
                boolean selected = false;

                public PhaseObject(long index, int panel) {
                    this.index = index;
                    this.panel=panel;
                    this.object = getObjectByPriority(panel);
                }

                public void render(Graphics2D g2d,int y,double scale) {
                    g2d.translate(panel*verticalPanelWidth-verticalPanelWidth/2,y);
                    g2d.scale(scale,scale);
                    object.render(g2d,0,0);
                    if (selected){
                        renderSelected(g2d);
                    }
                }

                private void renderSelected(Graphics2D g2d){
                    BufferedImage image = object.getImage();
                    if (image!=null){
                        g2d.setColor(object.getSelectedColor());
                        g2d.fillRect(-image.getWidth()/2,-image.getHeight()/2,
                                image.getWidth(),image.getHeight());
                        g2d.setColor(object.getColor());
                        g2d.drawRect(-image.getWidth()/2,-image.getHeight()/2,
                                image.getWidth(),image.getHeight());
                    }
                }
            }
        }
    }

    private class LeftPanel extends JPanel{
        private LeftPanel(){
            setOpaque(false);
            setPreferredSize(new Dimension(40,40));
            setLayout(new GridBagLayout());

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
            buttonsPanel.setOpaque(false);

            List<JButton> buttons = new ArrayList<>();

            JButton arrowsState = new JButton();
            JButton selectState = new JButton();
            JButton newPart = new JButton();

            arrowsState.setIcon(Assets.CURSOR);
            selectState.setIcon(Assets.BAIXAR);
            newPart.setIcon(Assets.LOCATION);

            buttons.add(arrowsState);
            buttons.add(selectState);
            buttons.add(newPart);

            newPart.addActionListener(e -> handler.openPhaseConfiguration());
            //arrowsState.addActionListener(e -> state='a');
            //selectState.addActionListener(e -> state='s');
            arrowsState.addActionListener(e -> {
                FileManager.writeFile(getPhaseJson(),new File(handler.folder+"\\phase.json"));
            });

            for(JButton b:buttons){
                b.setFocusable(false);
                b.setBackground(handler.color2);
                b.setBorderPainted(false);
                buttonsPanel.add(b);
            }
            GridBagConstraints c = new GridBagConstraints();
            add(buttonsPanel,c);
        }
    }
    private class RightPanel extends JPanel{
        private RightPanel(){
            setOpaque(false);
            setPreferredSize(new Dimension(100,100));
        }
    }
}

