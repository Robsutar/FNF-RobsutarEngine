package com.robsutar.Fnf.PhaseInit.PhaseBuilder;

import com.robsutar.Engine.Helpers.EngineVisuals;

import java.awt.*;

public interface PBColors {
    Color background = new Color(52, 73, 94);
    Color backgroundDarker = background.darker();
    Color backgroundMoreDarker = backgroundDarker.darker();

    Color color = new Color(142, 68, 173);
    Color colorDarker = color.darker();
    Color colorMoreDarker = colorDarker.darker();

    Color transparentSelect = EngineVisuals.changeOpacity(Color.WHITE,0.5f);
}
