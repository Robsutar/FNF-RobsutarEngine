package com.robsutar.Fnf.PhaseCreator.Tabs;

import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.robsutar.Fnf.PhaseCreator.PCTab;

import javax.swing.*;
import java.awt.*;

public class PCAtlasManagerTab extends PCTab {

    private JPanel left= new JPanel(),right = new JPanel(),bottom = new JPanel();
    public PCAtlasManagerTab(PCMainPanel h) {
        super(h);
        bottom.setBackground(Color.red);
    }
}
