package com.robsutar.Fnf.PhaseInit.PhaseBuilder;

import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Helpers.KeyManager;
import com.robsutar.Engine.Helpers.Lists;
import com.robsutar.Engine.Ultilities.ActionStatic;
import com.robsutar.Fnf.Animated.AnimObject;
import com.robsutar.Fnf.AnimationLoader.Anim;
import com.robsutar.Fnf.KeyConfiguration;
import com.robsutar.Fnf.PhaseInit.PhaseObject;
import com.robsutar.Fnf.PhaseInit.PhasePart;
import com.robsutar.Fnf.PhaseInit.PhasePlayerContents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class PBDesign extends PBWindow{

    final PlayerContentPanel principalPlayer;
    final List<PlayerContentPanel> secondaryPlayers = new ArrayList<>();

    PlayerContentPanel focusContentPanel;

    int panelWidth = 100;
    long zoom = 1000;
    int filter = 1;
    long marginTop =100000;

    public PBDesign(PBMainPanel handler) {
        super(handler);
        setLayout(new GridBagLayout());

        List<PhasePlayerContents> phasePlayerContents = new ArrayList<>(getContents().phasePlayerContents);
        principalPlayer = new PlayerContentPanel(phasePlayerContents.get(0));
        focusContentPanel=principalPlayer;
        phasePlayerContents.remove(0);

        for(PhasePlayerContents ppc : phasePlayerContents){
            secondaryPlayers.add(new PlayerContentPanel(ppc));
        }

        revalidatePlayerContents();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        long index = getNearestPit();
        int panel = 0;
        if (e.getKeyCode()==KeyEvent.VK_TAB){
            changeFocusContentPanel(Lists.getRotationObject(getAllContentPlayers(),getAllContentPlayers().indexOf(focusContentPanel)+1));
        }
        else if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
            focusContentPanel.removeAllObjects();
        }
        else if (e.getKeyCode()==KeyEvent.VK_A&&e.isControlDown()){
            focusContentPanel.changeAllObjectsSelect(true);
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
            focusContentPanel.addObject(index, panel);
        }
    }

    @Override
    public void bpmTick() {
        super.bpmTick();
        long nearestPit =getNearestPitByFilter(1);
        long maxDistance = 50000;
        for(PlayerContentPanel p:getAllContentPlayers()){

            for(PhaseObject o:p.contents.objects){
                PBArrowTarget arrow = p.arrowSet.getArrowTarget(o.identification);
                if (Math.abs(nearestPit-o.index)<maxDistance){
                    arrow.playConfirm();
                    arrow.getAnimation().addEventOnFinish(e -> arrow.playIdle());

                    if (p.contents.anchorAnim!=null){
                        p.contents.anchorAnim.playAnimation(arrow.moveName);
                    }
                }
            }

            p.arrowSet.bpmTick();
        }
    }

    private PhasePart getPart(long index){
        for (int i = getContents().parts.size(); i > 0;i--){
            PhasePart p = getContents().parts.get(i-1);
            if (p.index<index){
                return p;
            }
        }
        return getContents().parts.get(0);
    }

    private void changeFocusContentPanel(PlayerContentPanel panel){
        if (focusContentPanel==panel){focusContentPanel.changeBackground(background);return;}
        focusContentPanel.changeBackground(backgroundDarker);
        focusContentPanel=panel;
        focusContentPanel.changeBackground(background);
        for(PlayerContentPanel p1:getAllContentPlayers()){
            p1.changeAllObjectsSelect(false);
        }
    }

    private Long getNearestPit(Long index){return getNearestPit(index,filter);}
    private Long getNearestPit(){return getNearestPit(handler.getMusicPosition(),filter);}
    private Long getNearestPit(int mouseY){return getNearestPit(mouseY*zoom+getVisualIndex());}
    private Long getNearestPit(long index,int filter){

        if (index<=0){return getContents().pits.get(0);}

        filter = (int) Math.pow(2,filter);
        PhasePart p = getPart(index);
        List<Long> pits = new ArrayList<>(getContents().pits);

        int fIndex = (int) (index /(60d/p.bpm*Math.pow(10,6)/16d));

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
    private Long getNearestPitByFilter(int filter){return getNearestPit(handler.getMusicPosition(),filter);}

    private long getVisualIndex(){return getVisualIndex(handler.getMusicPosition());}
    private long getVisualIndex(long index){return index- marginTop;}

    private int getMouseY(long index){ return (int) (index/zoom);}

    private int getPanel(int mouseX){
        int panel = 1;
        for(int i =4; i > 0;i--){
            if (panelWidth*(i-1)<mouseX){
                panel = i;
                break;
            }
        }
        return panel;
    }

    private class PlayerContentPanel extends JPanel{
        boolean open = true;
        public JButton closeOrOpen = new JButton("1");

        PBArrowSet arrowSet;

        final PhasePlayerContents contents;
        final List<PhaseObject> selectedObjects = new ArrayList<>();

        private PlayerContentPanel(PhasePlayerContents contents){
            this.contents=contents;

            arrowSet = new PBArrowSet();

            Dimension size = new Dimension(panelWidth*4,1);
            Dimension minimum = new Dimension(panelWidth/3,1);

            changeBackground(backgroundDarker);
            setMinimumSize(minimum);
            setPreferredSize(size);

            closeOrOpen.setBackground(getBackground());
            closeOrOpen.setForeground(Color.white);
            closeOrOpen.setBorderPainted(false);
            closeOrOpen.setFocusable(false);

            closeOrOpen.addActionListener(e -> {
                if (open){
                    setPreferredSize(minimum);
                }else {
                    setPreferredSize(size);
                }
                open=!open;
                PBDesign.this.invalidate();PBDesign.this.validate();
            });
            add(closeOrOpen);

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

        public void changeBackground(Color bg){
            setBackground(bg);
            closeOrOpen.setBackground(bg);
        }

        private void addObject(PhaseObject object){
            addObject(object,false);
        }
        private void removeObject(PhaseObject object){
            removeObject(object,false);
        }

        private void addObject(PhaseObject object,boolean ignoreActionStatic){
            boolean selected = selectedObjects.contains(object);
            if (ignoreActionStatic){
                contents.objects.add(object);
                if (selected){selectObject(object,true);}
                contents.refactor();
            }else {
                ActionStatic a = new ActionStatic(
                        e1 ->{addObject(object,true);},
                        e2 ->{removeObject(object,true);});
                a.doAction();
            }
        }

        private void removeObject(PhaseObject object,boolean ignoreActionStatic){
            boolean selected = selectedObjects.contains(object);
            if (ignoreActionStatic){
                contents.objects.remove(object);
                if (selected){selectObject(object,false);}
                contents.refactor();
            }else {
                ActionStatic a = new ActionStatic(
                    e1 ->{removeObject(object,true);},
                    e2 ->{addObject(object,true);});
                a.doAction();
            }
        }

        private boolean addObject(long index,int panel){
            PhaseObject o = getObject(index,panel);
            if (o==null) {
                addObject(new PhaseObject(panel,index));
                return true;
            }
            return false;
        }

        private PhaseObject getObject(long index, int panel){
            for(PhaseObject o:contents.objects){
                if (o.index==index&&o.identification==panel){
                    return o;
                }
            }
            return null;
        }

        private PhaseObject getNearestObject(long index, int panel){
            for(PhaseObject o:contents.objects){
                if (o.identification==panel) {
                    if(Math.abs(o.index - index) < (long) panelWidth * zoom) {
                        return o;
                    }
                }
            }
            return null;
        }

        private void selectObject(PhaseObject object, boolean value) {
            boolean contains = selectedObjects.contains(object);
            if (value) {
                if (!contains) {
                    selectedObjects.add(object);
                }
            }else if (contains) {
                selectedObjects.remove(object);
            }
        }
        private void changeObjectSelect(PhaseObject object){
            selectObject(object, !selectedObjects.contains(object));}
        private void changeAllObjectsSelect(boolean value){
            for (PhaseObject o:contents.objects){
                selectObject(o,value);
            }
        }

        private void removeAllObjects(){
            List<PhaseObject> objects = new ArrayList<>(selectedObjects);
            ActionStatic a = new ActionStatic(e -> {
                for(PhaseObject o:objects){
                    removeObject(o,true);
                }
            },e -> {
                for(PhaseObject o:objects){
                    addObject(o,true);
                    selectObject(o,true);
                }
            });
            a.doAction();
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

        private void mouseDown(MouseEvent e) {}

        private void mouseUp(MouseEvent e){
            changeFocusContentPanel(this);
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
                        changeObjectSelect(o);
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!open){return;}
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform at = g2d.getTransform();

            long pos = getVisualIndex();
            int filter = (int) Math.pow(2,PBDesign.this.filter);

            int firstPitIndex = getContents().pits.indexOf(getNearestPit(-marginTop));
            for (int i = firstPitIndex;i<getContents().pits.size();i++){
                long l = getContents().pits.get(i);
                int indexOf = getContents().pits.indexOf(l);
                if (indexOf%filter==0) {
                    int y = (int) ((l-pos)/zoom);
                    int width = 15;

                    {
                        if (indexOf % 32 == 0) {g2d.setColor(Color.white);width += 25;
                        } else if (indexOf % 16 == 0) {g2d.setColor(Color.white);width += 15;
                        } else if (indexOf % 8 == 0) {g2d.setColor(Color.white);width += 15;
                        } else if (indexOf % 4 == 0) {g2d.setColor(Color.red);width += 10;
                        } else if (indexOf % 2 == 0) {g2d.setColor(Color.cyan);width += 5;
                        } else {g2d.setColor(Color.yellow);}
                        int x = 0;
                        g2d.drawLine(x, y, x + width, y);
                        g2d.drawLine(getWidth(), y, getWidth()-width, y);
                        //g2d.drawString(String.valueOf(indexOf),10,y);
                    }//draw pit

                    if (y>getHeight())break;
                }
            }

            for(int i = 0;i<getContents().parts.size();i++){
                PhasePart p = getContents().parts.get(i);
                int y = getMouseY(p.index-pos);
                if (y<0){continue;}
                g2d.setColor(Color.red);
                g2d.drawLine(0,y,getWidth(),y);
                if (y>getHeight())break;
            }

            for(PhaseObject o:selectedObjects) {
                int arrowsY = getMouseY(o.index - pos);
                if (arrowsY < -panelWidth){continue;}
                g2d.setColor(transparentSelect);
                int size = panelWidth;
                g2d.fillRect(panelWidth*(o.identification)-panelWidth, arrowsY-panelWidth/2,size,size);
                if (arrowsY > getHeight()){break;}
            }

            for(PhaseObject o:contents.objects){
                int arrowsY = getMouseY(o.index-pos);
                if (arrowsY < -panelWidth){continue;}
                arrowSet.renderByPanel(g2d, o.identification, panelWidth*(o.identification)-panelWidth/2, arrowsY, panelWidth);
                if (arrowsY > getHeight()){break;}
            }

            for(int i = 1;i <=4;i++){
                arrowSet.renderTargetByPanel(g2d,i,panelWidth*(i)-panelWidth/2,-getMouseY(getVisualIndex(0)),panelWidth);
            }

            if (focusContentPanel==this){
                g2d.setColor(Color.orange);
                g2d.drawLine(0,5,getWidth(),5);
            }

            GraphicsManipulator.resetGraphics(g2d,at);
        }
    }

    private List<PlayerContentPanel> getAllContentPlayers(){
        List<PlayerContentPanel> list = new ArrayList<>();
        list.add(principalPlayer);
        list.addAll(secondaryPlayers);
        return list;
    }

    private void revalidatePlayerContents(){
        remove(principalPlayer);
        for(PlayerContentPanel o:secondaryPlayers){
            remove(o);
        }
        GridBagConstraints c = new GridBagConstraints();

        c.fill=GridBagConstraints.VERTICAL;
        c.insets= new Insets(0,5,0,5);
        c.weighty=1;

        c.gridx=secondaryPlayers.size()+1;
        add(principalPlayer,c);

        for(int i = 0;i<secondaryPlayers.size();i++){
            PlayerContentPanel o = secondaryPlayers.get(i);
            o.closeOrOpen.setText(String.valueOf(i+2));
            c.gridx--;
            add(o,c);
        }

        changeFocusContentPanel(principalPlayer);
    }
}
