package net.royqh.jeasygraphics.fillpattern;

import java.awt.*;

public class HatchFillPaint extends AbstractFillPatternPaint {
    public HatchFillPaint(Color color, Color backColor) {
        super(color, backColor);
    }

    @Override
    protected void drawTexture(Graphics2D g) {
        g.drawLine(0, SIZE /2, SIZE -1, SIZE /2);
        g.drawLine(SIZE /2,0, SIZE /2, SIZE -1);
    }
}
