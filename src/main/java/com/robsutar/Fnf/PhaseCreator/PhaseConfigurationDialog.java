package com.robsutar.Fnf.PhaseCreator;


import com.robsutar.Assets;
import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Helpers.JComponentHelpers;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCDesignTab;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhaseConfigurationDialog extends JDialog {
    JTabbedPane tab;
    List<TabPanel> tabs;

    PartPanel partPanel;

    public final PCMainPanel handler;

    PhaseConfigurationDialog(PCMainPanel handler){
        this.handler=handler;
        {
            setResizable(true);
            setSize(new Dimension(500, 500));
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setModal(true);
            setTitle("Part Manager");
            setLocationRelativeTo(null);
            tab = new JTabbedPane();
            tabs = new ArrayList<>();

            partPanel = new PartPanel();
            tabs.add(partPanel);
            tabs.add(new ConfigsPanel());
            tabs.add(new FilesPanel());

            for(TabPanel p:tabs){
                tab.add(p.name,p);
            }

            tab.addChangeListener(e -> ((TabPanel)tab.getSelectedComponent()).update());

            add(tab);
        }

        {
            JPanel southPanel = new JPanel();
            southPanel.setLayout(new GridLayout(1, 2));

            JButton ok = new JButton("Ok");
            JButton cancel = new JButton("Cancel");
            southPanel.add(ok);
            southPanel.add(cancel);

            cancel.addActionListener(e -> close());
            ok.addActionListener(e -> save());
            add(southPanel,BorderLayout.PAGE_END);
        }
    }

    private abstract class TabPanel extends JPanel{
        public abstract void update();
        public final String name;
        TabPanel(String name){this.name=name;}
    }

    private class PartPanel extends TabPanel {
        JTable table;
        JTextField bpmTf;
        JTextField indexTf;
        int lastSelectedRow=0;
        PCDesignTab.MiddlePanel middle =  handler.designTab.middle;
        List<PCDesignTab.MiddlePanel.Part> parts =middle.parts;
        PartPanel() {
            super("Parts");

            {
                setLayout(new BorderLayout());
            }//panel configuration

            {

                String[] cols = {"Time", "Bpm", "Index"};
                String[][] rows = {{String.valueOf(0),
                        String.valueOf(parts.get(0).bpm),
                        String.valueOf(parts.get(0).index)}};

                DefaultTableModel model = new DefaultTableModel(rows, cols);
                table = new JTable(model);
                table.setDefaultEditor(Object.class, null);
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                        onSelectRow();
                        if (e.getClickCount()>1){
                            handler.getMusic().setPosition(getSelectedPart().index);
                        }
                    }
                });
                JScrollPane tableScroll = new JScrollPane(table);
                tableScroll.setPreferredSize(new Dimension(275,1));
                add(tableScroll,BorderLayout.WEST);

            }//table

            {
                JPanel southPanel = new JPanel();
                southPanel.setLayout(new GridLayout(1,2));

                JButton addPart = new JButton();
                addPart.setIcon(Assets.MAIS);
                addPart.setFocusable(false);
                JButton removePart = new JButton();
                removePart.setIcon(Assets.MENOS);
                removePart.setFocusable(false);

                addPart.addActionListener(e -> {
                    l1:
                    {
                        PCDesignTab.MiddlePanel.Part part = new PCDesignTab.MiddlePanel.Part(handler.getMusic().getPosition(),parts.get(parts.size()-1).bpm);
                        for (int i = 0; i < parts.size();i++) {
                            PCDesignTab.MiddlePanel.Part p = parts.get(i);
                            if(p.index == part.index) {
                                table.getSelectionModel().setSelectionInterval(i,i);
                                onSelectRow();
                                break l1;
                            }
                        }
                        middle.addPart(part);
                        update();
                        int indexOf = parts.indexOf(part);
                        table.getSelectionModel().setSelectionInterval(indexOf,indexOf);
                        onSelectRow();
                    }
                });

                removePart.addActionListener(e -> {
                    if (table.getSelectedRow()>0&&table.getSelectedRow()<parts.size())
                    middle.removePart(getSelectedPart());
                    update();
                });

                southPanel.add(addPart);
                southPanel.add(removePart);

                add(southPanel,BorderLayout.SOUTH);
            }//southPanel

            {
                JPanel partConfigsPanel = new JPanel();
                partConfigsPanel.setLayout(new GridBagLayout());

                JLabel bpm = new JLabel("Bpm: ");
                JLabel index = new JLabel("Index: ");

                bpmTf = new JTextField(String.valueOf(parts.get(0).bpm),9);
                indexTf = new JTextField(String.valueOf(parts.get(0).index),9);

                PlainDocument bpmDoc = (PlainDocument) bpmTf.getDocument();
                bpmDoc.setDocumentFilter(new JComponentHelpers.ParseFloatFilter());

                PlainDocument indexDoc = (PlainDocument) indexTf.getDocument();
                indexDoc.setDocumentFilter(new JComponentHelpers.ParseIntFilter());

                bpmTf.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                            bpmTf.setFocusable(false);
                            bpmTf.setFocusable(true);
                            double bpm = Double.parseDouble(bpmTf.getText());
                            bpmTf.setText(String.valueOf(Math.floor(bpm*100) / 100));
                            getSelectedPart().bpm = Double.parseDouble(bpmTf.getText());
                            middle.refactor();
                            update();
                        }
                    }
                });

                indexTf.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                            indexTf.setFocusable(false);
                            indexTf.setFocusable(true);
                            PCDesignTab.MiddlePanel.Part p = getSelectedPart();
                            if (p.index!=0) {
                                p.index = Integer.parseInt(indexTf.getText());
                            }
                            middle.refactor();
                            update();
                        }
                    }
                });

                JButton useCurrentIndex = new JButton("Use current time");
                useCurrentIndex.setFocusable(false);
                useCurrentIndex.addActionListener(e -> {
                    if (table.getSelectedRow()>0&&table.getSelectedRow()< parts.size()){
                        PCDesignTab.MiddlePanel.Part p = getSelectedPart();
                        p.index= handler.getMusic().getPosition();
                        middle.refactor();
                        update();
                        int indexOf = parts.indexOf(p);
                        table.getSelectionModel().setSelectionInterval(indexOf,indexOf);
                        onSelectRow();
                    }
                });

                {
                    GridBagConstraints c = new GridBagConstraints();

                    c.weightx = 1;

                    c.gridy = 0;
                    c.gridx = 0;
                    partConfigsPanel.add(bpm, c);
                    c.gridx = 1;
                    partConfigsPanel.add(bpmTf, c);

                    c.gridy = 1;
                    c.gridx = 0;
                    partConfigsPanel.add(index, c);
                    c.gridx = 1;
                    partConfigsPanel.add(indexTf, c);

                    c.gridy = 2;
                    partConfigsPanel.add(useCurrentIndex,c);
                }//GridBagConstraints
                add(partConfigsPanel,BorderLayout.CENTER);
            }//configsPanel

            update();
        }

        private PCDesignTab.MiddlePanel.Part getSelectedPart(){
            int row = table.getSelectedRow();
            if (row<0||row>=parts.size()){
                return parts.get(0);
            }
            return parts.get(table.getSelectedRow());
        }

        private void onSelectRow(){
            if (table.getSelectedRow()<0||table.getSelectedRow()>=parts.size()){
                table.getSelectionModel().setSelectionInterval(0,0);
            }
            int selectedRow = table.getSelectedRow();
            table.getSelectionModel().setSelectionInterval(selectedRow,selectedRow);
            bpmTf.setText(String.valueOf(parts.get(selectedRow).bpm));
            indexTf.setText(String.valueOf(parts.get(selectedRow).index));
            lastSelectedRow = selectedRow;
        }

        @Override
        public void update() {
            {
                DefaultTableModel model = ((DefaultTableModel) table.getModel());
                model.setRowCount(0);
                for (PCDesignTab.MiddlePanel.Part p : parts) {
                    Date time = new Date((p.index/1000));
                    DateFormat dateFormat = new SimpleDateFormat("mm:ss.SSS");
                    String[] row = {dateFormat.format(time), String.valueOf(p.bpm), String.valueOf(p.index)};
                    model.addRow(row);
                }
            }
            table.getSelectionModel().setSelectionInterval(lastSelectedRow,lastSelectedRow);
            onSelectRow();
        }
    }
    private class ConfigsPanel extends TabPanel {
        ConfigsPanel() {
            super("Configs");
        }

        @Override
        public void update() {

        }
    }
    private class FilesPanel extends TabPanel {
        FilesPanel() {
            super("Files");
        }

        @Override
        public void update() {

        }
    }

    void open(){
        setVisible(true);
    }
    void save(){
        close();
    }
    void close(){
        setVisible(false);
    }
}
