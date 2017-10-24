package net.royqh.jeasygraphics.fillpattern;

import java.awt.*;

public class VerticalFillPaint extends AbstractFillPatternPaint {
    public VerticalFillPaint(Color color, Color backColor) {
        super(color, backColor);
    }

    @Override
    protected void drawTexture(Graphics2D g) {
        g.drawLine(0, SIZE /2, SIZE -1, SIZE /2);
    }
}
