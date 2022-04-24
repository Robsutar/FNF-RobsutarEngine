package com.robsutar.Fnf.PhaseCreator.Tabs;

import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Helpers.JComponentHelpers;
import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.robsutar.Fnf.PhaseCreator.PCTab;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PCTapBpm extends PCTab {
    final CenterPanel center;
    final LeftPanel left;
    final RightPanel right;

    private long startNanoClick = 0;

    private List<Long> clickList = new ArrayList<>();

    private double bpm;

    public PCTapBpm(PCMainPanel h) {
        super(h);
        bpm = getMusic().getBpm();

        center = new CenterPanel();
        left = new LeftPanel();
        right = new RightPanel();

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        c.weighty = 1;
        c.weightx = 1;

        c.gridx = 0;
        panel.add(right,c);
        c.gridx = 2;
        panel.add(left,c);
        c.gridx = 1;
        panel.add(center,c);
    }

    @Override
    public void bpmTick(long age) {
        super.bpmTick(age);
        center.bpm(age);
    }

    @Override
    protected void tick() {
        super.tick();
        center.tick();
    }

    @Override
    protected void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode()==KeyEvent.VK_T){
            center.tapButton.doClick();
        }
    }

    private void setBpm(double f){
        this.bpm = f;
        getMusic().setBpm(bpm);
    }

    public class CenterPanel extends JPanel {

        JButton tapButton, resetButton;
        JTextField bpmTextField;
        Lights lights;

        private CenterPanel() {
            setLayout(new GridBagLayout());
            setBackground(handler.color2);
            bpm = getMusic().getBpm();

            lights = new Lights(new Dimension(300, 50));

            tapButton = new JButton("TAP (T)");

            tapButton.setPreferredSize(new Dimension(100, 100));
            tapButton.setBorderPainted(false);
            tapButton.setFocusable(false);

            resetButton = new JButton("Reset Taps");

            resetButton.setPreferredSize(new Dimension(150, 50));
            resetButton.setBorderPainted(false);
            resetButton.setFocusable(false);

            bpmTextField = new JTextField(String.valueOf(bpm));
            bpmTextField.setPreferredSize(new Dimension(150, 50));
            bpmTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            bpmTextField.setHorizontalAlignment(JTextField.HORIZONTAL);
            PlainDocument doc = (PlainDocument) bpmTextField.getDocument();
            doc.setDocumentFilter(new JComponentHelpers.ParseFloatFilter());

            tapButton.addActionListener(e -> {
                onTap();
            });

            resetButton.addActionListener(e -> {
                resetClickList();
            });

            bpmTextField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        bpmTextField.setFocusable(false);
                        bpmTextField.setFocusable(true);
                    } else if (e.getKeyCode()==KeyEvent.VK_ENTER){
                        if (bpmTextField.getText().length()>0) {
                            updateBpm(Float.parseFloat(bpmTextField.getText()));
                        }

                        bpmTextField.setFocusable(false);
                        bpmTextField.setFocusable(true);
                    }
                }
            });

            GridBagConstraints c = new GridBagConstraints();

            setColor(handler.color3);

            c.gridy = 0;
            c.gridx = 1;
            add(tapButton, c);
            c.gridx = 2;
            add(resetButton, c);
            c.gridx = 0;
            add(bpmTextField, c);
            c.gridx = 1;
            c.gridy = 2;
            add(lights,c);
        }

        private void addClickToList(long nanoTime) {
            long deltaNanoTime = nanoTime - startNanoClick;
            startNanoClick = System.nanoTime();
            if(deltaNanoTime < 3000000000L) {
                clickList.add(deltaNanoTime);
                while (clickList.toArray().length > 128) {
                    clickList.remove(0);
                }
            }
            calculateAverage();
        }

        private void calculateAverage() {
            if(!clickList.isEmpty()) {
                long media = 0;
                for (long l : clickList) {
                    media += l;
                }
                media /= clickList.toArray().length;
                float b = (float) (media * Math.pow(10, -9));
                b = 60 / b;
                updateBpm(b);
            }
        }

        private void updateBpm(float b) {
            setBpm((float) (Math.floor(b * 100) / 100));
            String f2 =String.valueOf(bpm);
            tapButton.setText(f2);
            resetButton.setText("Reset Taps: " + clickList.toArray().length);
            bpmTextField.setText(f2);
        }

        private void resetClickList() {
            clickList = new ArrayList<>();
            tapButton.setText("TAP (T)");
            resetButton.setText("Reset Taps");
            startNanoClick = 0;
        }

        private void setColor(Color c){
            tapButton.setBackground(c);
            resetButton.setBackground(c.darker());
            bpmTextField.setBackground(c.darker());
        }

        private void onTap() {
            addClickToList(System.nanoTime());
            Color c = EngineVisuals.randomColor();
            setColor(c);
        }

        public void bpm(long age) {
            lights.bpmTick(age);
        }

        public void tick(){lights.tick();}

        private class Lights extends JPanel{
            private List<SingularLight> lights = new ArrayList<>();
            int index = 0;
            private Lights(Dimension dim){
                setPreferredSize(dim);
                setOpaque(false);

                for (int i = 0; i < getMusic().compass;i++){
                    lights.add(new SingularLight());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform at = g2d.getTransform();
                int width = getWidth()/4;
                int height = getHeight();
                int x = getWidth()/2;
                int y = 0;
                int x1 = x - width*(lights.size()/2);
                for (SingularLight l : lights) {
                    l.render(g2d, x1, y, width, height);
                    x1 += width;
                    GraphicsManipulator.resetGraphics(g2d, at);
                }
            }

            long lastAge = 0;

            private void bpmTick(long age){
                /*
                if (age!=lastAge+1){
                    System.out.println(age+" <> "+lastAge);
                }
                lastAge = age;
                if (age%getMusic().bpmDivisor==0) {
                    lights.get(index).highlight();
                    index++;
                    if (index>=4){
                        index = 0;
                    }
                }

                 */
            }

            private void tick(){
                for (SingularLight l : lights) {
                    l.tick();
                }
            }

            private class SingularLight{
                private float opacity = 1f;
                private Color c = Color.CYAN;
                private SingularLight(){

                }
                private void tick(){
                    if (opacity>0) {
                        opacity -= 0.05f;
                    }
                }
                private void highlight(){
                    opacity = 1f;
                }
                private void render(Graphics2D g2d,int x, int y,int width, int height){
                    GraphicsManipulator.setOpacity(g2d,opacity);
                    g2d.setColor(c);
                    g2d.fillRect(x,y,width,height);
                }
            }
        }
    }

    private class LeftPanel extends JPanel{
        private LeftPanel (){
            setOpaque(false);
        }
    }
    private class RightPanel extends JPanel {
        private RightPanel () {
            {
                setOpaque(false);
            }

        }
    }
}

