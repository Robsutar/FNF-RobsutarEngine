package com.robsutar.Fnf.PhaseCreator.Tabs;

import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Helpers.GraphicsManipulator;
import com.robsutar.Engine.Helpers.JComponentHelpers;
import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Fnf.Animated.AnimatedObject;
import com.robsutar.Fnf.AnimationBuilder.AnimationFileHook;
import com.robsutar.Fnf.PhaseCreator.PCAssets;
import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.robsutar.Fnf.PhaseCreator.PCTab;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class PCAnimatedTab extends PCTab {
    private int speed = 60;
    private int age = 0;
    final RightPanel rightPanel;
    final TopPanel topPanel;
    AnimatedObject lastAnimated;
    PCAssets.AnimatedFile lastAnimFile;
    AnimationFileHook.AnimationConfiguration lastAnimConfiguration;

    Point initialDragPoint = new Point(0,0);
    Point initialOffsets = new Point(0,0);
    Point originalOffsets = new Point(0,0);

    public PCAnimatedTab(PCMainPanel h) {
        super(h);
        panel.setLayout(new BorderLayout());
        topPanel = new TopPanel();
        rightPanel=new RightPanel();

        panel.add(topPanel,BorderLayout.NORTH);
        panel.add(rightPanel,BorderLayout.EAST);
        onObjectChanged();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (lastAnimConfiguration!=null) {
                    initialOffsets = new Point(lastAnimConfiguration.x, lastAnimConfiguration.y);
                    initialDragPoint = e.getPoint();
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastAnimConfiguration!=null) {
                    int x = e.getX() - initialDragPoint.x;
                    int y = e.getY() - initialDragPoint.y;
                    lastAnimConfiguration.x=x+initialOffsets.x;
                    lastAnimConfiguration.y=y+initialOffsets.y;
                    rightPanel.offsetsLabel.setText(lastAnimConfiguration.x+","+ lastAnimConfiguration.y);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    @Override
    protected void tick() {
        super.tick();
        age+=speed;
        if (age>=100){
            age=0;
            if (lastAnimated!=null){
                lastAnimated.bpmTick();
            }
        }
    }

    @Override
    protected void onPaintBackground(Graphics2D g2d) {
        super.onPaintBackground(g2d);
        AffineTransform at = g2d.getTransform();
        if (lastAnimated!=null){
            lastAnimated.render(g2d);
            GraphicsManipulator.resetGraphics(g2d,at);
        }
    }

    private void onObjectChanged(){
        Object o = topPanel.animatedObjects.getSelectedItem();
        if (o instanceof PCAssets.AnimatedFile){
            lastAnimFile = (PCAssets.AnimatedFile)o;
            lastAnimated=lastAnimFile.object;
        }
        rightPanel.animations.removeAllItems();
        if (lastAnimated!=null) {
            rightPanel.animations.addItem(lastAnimated.getAnimation().fileHook.animationConfigurations.get(0));
            onAnimationChanged();
        }
    }

    private void onAnimationChanged(){
        Object o = rightPanel.animations.getSelectedItem();
        if (lastAnimConfiguration!=null){
            lastAnimConfiguration.x=originalOffsets.x;
            lastAnimConfiguration.y= originalOffsets.y;
        }
        if (o instanceof AnimationFileHook.AnimationConfiguration){
            lastAnimConfiguration= (AnimationFileHook.AnimationConfiguration)o;
            lastAnimated.getAnimation().playAnimation(lastAnimConfiguration);
            rightPanel.commandField.setText(lastAnimConfiguration.command);
            rightPanel.offsetsLabel.setText(lastAnimConfiguration.x+","+ lastAnimConfiguration.y);
            rightPanel.shouldLoopBox.setSelected(lastAnimConfiguration.shouldLoop);
            originalOffsets = new Point(lastAnimConfiguration.x, lastAnimConfiguration.y);
        }
    }

    private class RightPanel extends JPanel{
        final JComboBox animations;
        final JTextField commandField = new JTextField(7);
        final JLabel offsetsLabel = new JLabel();
        final JCheckBox shouldLoopBox = new JCheckBox();
        private RightPanel(){
            setOpaque(false);
            setLayout(new GridBagLayout());

            JPanel animationConfigsPanel = new JPanel(new SpringLayout());
            animationConfigsPanel.setLayout(new GridBagLayout());
            animationConfigsPanel.setBorder(new EmptyBorder(10,10,10,10));

            animations = new JComboBox();

            animations.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    Object selected= animations.getSelectedItem();
                    animations.removeAllItems();
                    for(AnimationFileHook.AnimationConfiguration a:lastAnimFile.object.getAnimation().fileHook.animationConfigurations){
                        animations.addItem(a);
                    }
                    animations.setSelectedItem(selected);
                }

                @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    onAnimationChanged();
                }
                @Override public void popupMenuCanceled(PopupMenuEvent e) {}
            });

            JLabel command = new JLabel("Command: ");
            JLabel offsets = new JLabel("Offsets: ");
            JLabel shouldLoop = new JLabel("Should Loop: ");

            JPanel commandPanel = new JPanel();
            commandPanel.add(command);
            commandPanel.add(commandField);

            JPanel offsetsPanel = new JPanel();
            offsetsPanel.add(offsets);
            offsetsPanel.add(offsetsLabel);

            JPanel shouldLoopPanel = new JPanel();
            shouldLoopPanel.add(shouldLoop);
            shouldLoopPanel.add(shouldLoopBox);

            JButton ok = new JButton("OK");
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e ->{
                if (lastAnimConfiguration!=null){
                    lastAnimConfiguration.x=originalOffsets.x;
                    lastAnimConfiguration.y=originalOffsets.y;
                    onObjectChanged();
                }
            });
            ok.addActionListener(e -> {
                if (lastAnimConfiguration!=null){
                    lastAnimConfiguration.command=commandField.getText();
                    lastAnimConfiguration.shouldLoop=shouldLoopBox.isSelected();
                }
            });

            JPanel currentAnimationPanel = new JPanel();
            currentAnimationPanel.add(new JLabel("Current: "));
            currentAnimationPanel.add(animations);

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(ok);
            bottomPanel.add(cancel);

            GridBagConstraints c = new GridBagConstraints();
            c.gridy=0;
            animationConfigsPanel.add(currentAnimationPanel,c);
            c.gridy=1;
            animationConfigsPanel.add(commandPanel,c);
            c.gridy=2;
            animationConfigsPanel.add(offsetsPanel,c);
            c.gridy=3;
            animationConfigsPanel.add(shouldLoopPanel,c);
            c.gridy=4;
            animationConfigsPanel.add(bottomPanel,c);
            add(animationConfigsPanel);
        }
    }

    private class TopPanel extends JPanel{
        final JComboBox animatedObjects;
        private TopPanel(){
            setOpaque(false);
            JButton create = new JButton("CREATE");
            create.addActionListener(e -> {
                File xmlFile = FileManager.loadExplorerFile(new FileNameExtensionFilter("XML file","xml"));
                if (xmlFile!=null){
                    File imageFile = FileManager.loadExplorerFile(new FileNameExtensionFilter("IMAGE file","png"));
                    if (imageFile!=null){
                        JSONObject json = new JSONObject();
                        json.put("xml",xmlFile.getName());
                        json.put("image",imageFile.getName());
                        json.put("animations",new JSONArray());
                        File jsonFile =  FileManager.loadFile(handler.assetsFolder+xmlFile.getName().replace("xml","json"));
                        FileManager.writeFile(xmlFile,
                                FileManager.loadFile(handler.assetsFolder+xmlFile.getName()));
                        FileManager.writeFile(imageFile,
                                FileManager.loadFile(handler.assetsFolder+imageFile.getName()));
                        FileManager.writeFile(json,jsonFile);
                        handler.assets.addAnimatedObject(jsonFile);
                    }
                }
            });
            JButton load = new JButton("LOAD");
            load.addActionListener(e -> {
                File f = FileManager.loadExplorerFile(new FileNameExtensionFilter("JSON file","json"));
                if (f!=null){
                    handler.assets.addAnimatedObject(f);
                }
            });
            JButton save = new JButton("SAVE");
            save.addActionListener(e -> {
                if (lastAnimated!=null){
                    lastAnimated.getAnimation().fileHook.saveJsonFile();
                }
            });
            JTextField speedManager = new JTextField(String.valueOf(speed));
            PlainDocument doc = (PlainDocument) speedManager.getDocument();
            doc.setDocumentFilter(new JComponentHelpers.ParseIntFilter());
            speedManager.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if (e.getKeyCode()==KeyEvent.VK_ENTER||e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                            speed = Math.min(100, Integer.parseInt(speedManager.getText()));
                            speedManager.setFocusable(false);
                            speedManager.setFocusable(true);
                        }
                        speedManager.setText(String.valueOf(speed));
                    }
                }
            });

            animatedObjects = new JComboBox();
            if (!handler.assets.animatedObjects.isEmpty()){
                animatedObjects.addItem(handler.assets.animatedObjects.get(0));
            }
            animatedObjects.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    Object selected= animatedObjects.getSelectedItem();
                    animatedObjects.removeAllItems();
                    for(PCAssets.AnimatedFile a:handler.assets.animatedObjects){
                        animatedObjects.addItem(a);
                    }
                    animatedObjects.setSelectedItem(selected);
                }

                @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    onObjectChanged();
                }

                @Override public void popupMenuCanceled(PopupMenuEvent e) {}
            });
            add(load);
            add(create);
            add(save);
            add(speedManager);
            add(animatedObjects);
        }
    }
}
