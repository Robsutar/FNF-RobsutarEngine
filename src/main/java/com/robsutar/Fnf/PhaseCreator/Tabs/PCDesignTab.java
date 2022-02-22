package com.robsutar.Fnf.PhaseCreator.Tabs;

import com.robsutar.Engine.Helpers.*;
import com.robsutar.Engine.Ultilities.ActionStatic;
import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.robsutar.Fnf.PhaseCreator.PCTab;
import com.robsutar.Fnf.PhaseCreator.PhaseDesignObjects.PCDObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class PCDesignTab extends PCTab {

    private final MiddlePanel middle;
    private final JPanel bottom= new JPanel(),right = new JPanel();

    private final Color spray2 = (EngineVisuals.changeOpacity(EngineVisuals.SPRAY,0.2f));
    private final Color spray7 = (EngineVisuals.changeOpacity(EngineVisuals.SPRAY,0.7f));

    public PCDesignTab(PCMainPanel h) {
        super(h);

        middle = new MiddlePanel();

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.VERTICAL;//fill north to south
        c.weighty = 1;   //request any extra vertical space
        c.anchor = GridBagConstraints.CENTER; //bottom of space

        panel.add(middle,c);
    }

    @Override
    protected void tick() {
        super.tick();
        middle.tick();
    }

    @Override
    protected void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        middle.keyPressed(e);
    }

    @Override
    protected void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    private class MiddlePanel extends JPanel{

        ArrowsPanel p1,p2;

        private final int panelWidth = 75,panelAmount = 4;

        private boolean dragging = false;
        private int startDragX,startDragY;

        private double position = 0,startPosition;
        private double thumbPos = 0;
        private double musicPos = 0;
        private int pointDistance = 15;

        private int divisor = 2,maxDivisor=4;

        private MiddlePanel(){
            setLayout(new BorderLayout(5,5));
            setOpaque(false);

            p1= new ArrowsPanel(1);
            p2= new ArrowsPanel(2);

            add(p1,BorderLayout.EAST);
            add(p2,BorderLayout.WEST);
        }

        private int getPowDivisor(){
            return (int) Math.pow(2,divisor);
        }

        private void setDivisor(int a){
            if (a>maxDivisor){divisor=maxDivisor;return;}
            if (a<0){divisor=0;return;}
            divisor=a;
        }

        private void tick(){
            if (!dragging) {
                if(position < 0) {
                    position -= position / 10.0 -0.1;
                    if(position > 0) {
                        position = 0;
                    }
                }
            }
            p1.tick();
            p2.tick();
        }

        public void keyPressed(KeyEvent e) {
            if (p1.focus){p1.keyDrop(e);}
            if (p2.focus){p2.keyDrop(e);}
        }

        public void setFocus(ArrowsPanel p){
            p1.focus=false;p2.focus=false;
            p.focus=true;
        }

        private class ArrowsPanel extends JPanel{

            private final int type;

            Rectangle dragRect = null;

            List<Pit> pits;

            private List<PCDObject> selectedObjects = new ArrayList<>();
            private List<Pit> selectedPits = new ArrayList<>();

            private boolean dragLeft=false,dragRight=false,focus=false,selectRectVisible;

            private PCDObject slideringObject = null;

            public ArrowsPanel(int t){
                type = t;
                setPreferredSize(new Dimension(panelWidth*panelAmount,1));
                setBackground(handler.color2);

                addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        mouseDrag(e);
                        mouseMove(e);
                    }

                    @Override
                    public void mouseMoved(MouseEvent e) {mouseMove(e);
                    }
                });

                addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mouseDown(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mouseUp(e);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });

                addMouseWheelListener(this::mouseWheel);

                addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {

                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        keyDrop(e);
                    }
                });

                pits = new ArrayList<>();

                for (int i = 0; i <  2000;i++){
                    pits.add(new Pit(i));
                }
            }

            private int getPanel(int mouseX){
                for (int i = 0; i <= panelAmount;i++){
                    if (i*panelWidth>=mouseX){
                        return i;
                    }
                }
                return 0;
            }

            private int getIndex(int mouseY){
                int index = getAbsoluteIndex(mouseY);
                index /= getPowDivisor() ;
                index *=getPowDivisor();
                return index;
            }

            private int getAbsoluteIndex(int mouseY){
                return (int) ((position*pointDistance+(mouseY+(pointDistance*getPowDivisor())/2))/pointDistance);
            }

            private Pit getPit(int mouseY){
                int index =getIndex(mouseY);
                return Lists.getSecureObject(pits,index);
            }

            private Pit getAbsolutePit(int mouseY){
                int index =getAbsoluteIndex(mouseY);
                return Lists.getSecureObject(pits,index);
            }

            private Pit getNearestFilledPit(int mouseX,int mouseY){
                for (int y = 0; y <panelWidth;y++){
                    int addi = y;
                    if (y%2==0){
                        addi = -y;
                    }
                    addi/=2;
                    int index = (int) ((position*pointDistance+(mouseY+addi))/pointDistance);
                    Pit p = Lists.getSecureObject(pits,index);
                    if (p!=null) {
                        PCDObject o = p.getObject(mouseX);
                        if(o != null) {
                            return p;
                        }
                    }
                }
                return null;
            }

            private void addSelectedObject(PCDObject o,Pit p){
                selectedPits.add(p);
                selectedObjects.add(o);
            }

            private void removeSelectedObject(PCDObject o,Pit p){
                selectedPits.remove(p);
                selectedObjects.remove(o);
            }

            private void resetSelected(){
                for (PCDObject o: selectedObjects){
                    o.setSelected(false);
                }
                selectedObjects= new ArrayList<>();
                selectedPits = new ArrayList<>();
            }

            private void mouseClick(MouseEvent e){
                if (e.getButton()==MouseEvent.BUTTON1){
                    Pit p = getPit(e.getY());
                    if (p!=null){
                        PCDObject o = p.getObject(e.getX());
                        if (o==null) {
                            p.addObject(e.getX());
                        } else {
                            p.selectObject(o,true);
                        }
                    }
                } else if (e.getButton()==MouseEvent.BUTTON3){
                    Pit p = getNearestFilledPit(e.getX(),e.getY());
                    if (p!=null){
                        PCDObject o = p.getObject(e.getX());
                        if (o!=null) {
                            p.removeObject(o);
                        }
                    }
                }
            }

            private void mouseDown(MouseEvent e){
                setFocus(this);
                startDragX = e.getX();
                startDragY = e.getY();

                if (!KeyManager.isKeyPressed(KeyEvent.VK_CONTROL)) {
                    resetSelected();
                }
                if (e.getButton()==MouseEvent.BUTTON1) {
                    Pit p = getPit(e.getY());
                    if (p!=null){
                        PCDObject o = p.getObject(e.getX());
                        if (o!=null) {
                            slideringObject = o;
                        } else {
                            selectRectVisible = true;
                        }
                    }
                    dragRect = new Rectangle(e.getX(), e.getY(), 0, 0);
                    dragRight=true;

                } else if (e.getButton()==MouseEvent.BUTTON3){
                    startPosition = position;
                    dragLeft=true;
                }
            }

            private void mouseUp(MouseEvent e) {
                if (dragRight&&dragRect!=null){

                    int wdt = (dragRect.width < 0 ? -dragRect.width : dragRect.width);
                    int hgt = (dragRect.height < 0 ? -dragRect.height : dragRect.height);

                    if (wdt<10&&hgt<10){
                        mouseClick(e);
                    } else {
                        Rectangle r = GraphicsManipulator.makeRectPositive(dragRect);
                        for (int y = 0;y<r.height/pointDistance;y++){
                            Pit p = getAbsolutePit(r.y+y*pointDistance);
                            if (p!=null){
                                for (int x = 0; x < (r.width+panelWidth)/panelWidth;x++){
                                    PCDObject o = p.getObject(r.x+x*panelWidth);
                                    if (o!=null){
                                        p.selectObject(o,true);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    mouseClick(e);
                }
                dragRect = null;
                dragRight=false;dragLeft=false;
                dragging = false;
                selectRectVisible =false;
                slideringObject = null;
            }

            private void mouseDrag(MouseEvent e){
                dragging = true;
                if (dragRight) {
                    if(dragRect != null) {
                        dragRect.width = e.getX() - startDragX;
                        dragRect.height = e.getY() - startDragY;
                    }
                } else if (dragLeft){
                    position = startPosition-(e.getY()-startDragY)/(double)pointDistance;
                }

                if (slideringObject!=null){
                    slideringObject.setLength((e.getY()-startDragY)/pointDistance);
                }
            }

            private void mouseMove(MouseEvent e){
            }

            private void mouseWheel(MouseWheelEvent e){
                if (KeyManager.isKeyPressed(KeyEvent.VK_CONTROL)) {
                    setDivisor(divisor + e.getWheelRotation());
                } else if (KeyManager.isKeyPressed(KeyEvent.VK_ALT)){
                    pointDistance-=e.getWheelRotation();
                } else {
                    position+=e.getWheelRotation()*5;
                }
            }

            private void keyDrop(KeyEvent e){
                if (e.getKeyCode()== KeyEvent.VK_BACK_SPACE) {
                    List<Pit> selectedPits = new ArrayList<>(this.selectedPits);
                    List<PCDObject> selectedObjects = new ArrayList<>(this.selectedObjects);

                    ActionStatic a = new ActionStatic(e1 -> {
                        for (int i = 0 ; i < selectedObjects.toArray().length;i++){
                            PCDObject o = selectedObjects.get(i);
                            selectedPits.get(i).objects.remove(o);
                        }
                        resetSelected();
                    },e1 -> {
                        for (int i = 0 ; i < selectedObjects.toArray().length;i++){
                            PCDObject o = selectedObjects.get(i);
                            o.setSelected(true);
                            selectedPits.get(i).objects.add(o);
                        }
                        this.selectedObjects = selectedObjects;
                        this.selectedPits = selectedPits;
                    });
                    a.doAction();
                }
                else if (e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_A){
                    for (Pit p:pits){
                        for(PCDObject o:p.objects){
                            p.selectObject(o,true);
                        }
                    }
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform at = g2d.getTransform();

                if (focus){
                    GraphicsManipulator.setOpacity(g2d,0.5f);
                    g2d.setColor(Color.white);
                    for (int i = 1; i < panelAmount;i++){
                        g2d.drawLine(i*panelWidth+1,0,i*panelWidth+1,getHeight());
                    }
                    GraphicsManipulator.setOpacity(g2d,1f);
                }

                if (selectRectVisible&&dragRect!=null){
                    Rectangle dr = GraphicsManipulator.makeRectPositive(dragRect);
                    g2d.setColor(spray2);
                    g2d.fill(dr);
                    g2d.setColor(spray7);
                    g2d.draw(dr);
                }

                for (Pit p : pits){
                    p.render(g2d);
                    GraphicsManipulator.resetGraphics(g2d,at);
                }
            }

            private void tick(){
                Point mouse = getMousePosition();
                if (mouse!=null){
                    Pit p = getNearestFilledPit(mouse.x,mouse.y);
                    if (p!=null){
                        PCDObject o = p.getObject(mouse.x);
                        if (o!=null){
                            o.setTemporarySelected(true);
                        }
                    }
                }
            }

            private class Pit {
                final int index;
                final Color color;
                final int additionalWidth;
                List<PCDObject> objects = new ArrayList<>();

                private Pit(int index){
                    this.index=index;
                    if (index%16==0){
                        color = Color.white;
                        additionalWidth = 20;
                    } else if (index%8==0){
                        color = Color.white;
                        additionalWidth = 14;
                    } else if (index%4==0){
                        color = Color.red;
                        additionalWidth = 9;
                    } else if (index%2==0){
                        color = new Color(9, 103, 194);
                        additionalWidth = 5;
                    } else{
                        color = Color.yellow;
                        additionalWidth = 0;
                    }
                }

                private void addObject(int mouseX){
                    PCDObject o = PCDObject.getByPriority(getPanel(mouseX));
                    if (o!=null){
                        addObject(o);
                    }
                }

                private PCDObject getObject(int mouseX){
                    int panel = getPanel(mouseX);
                    for (PCDObject o:objects){
                        if (o.getPriority()==panel){
                            return o;
                        }
                    }
                    return null;
                }

                private void addObject(PCDObject o) {
                    ActionStatic a = new ActionStatic(
                            e -> objects.add(o), e -> objects.remove(o));
                    a.doAction();
                }

                private void removeObject(PCDObject o){
                    ActionStatic a = new ActionStatic(
                            e -> objects.remove(o), e -> objects.add(o));
                    a.doAction();
                }

                private void selectObject(PCDObject o, boolean selected){

                    if (objects.contains(o)){
                        o.setSelected(selected);
                        if (o.isSelected()) {
                            if(!selectedObjects.contains(o)) {
                                selectedPits.add(this);
                                selectedObjects.add(o);
                            }
                        } else {
                            if (selectedObjects.contains(o)) {
                                selectedPits.remove(selectedObjects.indexOf(o));
                                selectedObjects.remove(o);
                            }
                        }
                    }
                }

                private void changeSelectObject(PCDObject o){
                    selectObject(o,!o.isSelected());
                }

                private boolean isVisible(){
                    return index%getPowDivisor()==0;
                }

                private void render(Graphics2D g2d){
                    int y = (int) ((index-position)*pointDistance);
                    if (isVisible()) {
                        g2d.setColor(color);
                        if (type==2) {
                            g2d.drawLine(0, y, additionalWidth + 10, y);
                        } else {
                            g2d.drawLine(getWidth(), y,getWidth() -(additionalWidth + 10), y);
                        }
                    }

                    AffineTransform at = g2d.getTransform();

                    for (PCDObject o:objects){
                        o.render(g2d,o.getPriority()*panelWidth-panelWidth/2,y,pointDistance);
                        GraphicsManipulator.resetGraphics(g2d,at);
                    }
                }
            }
        }
    }
}
