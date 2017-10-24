package net.royqh.jeasygraphics.fillpattern;

import java.awt.*;

public class HorizontalFillPaint extends AbstractFillPatternPaint {
    public HorizontalFillPaint(Color color, Color backColor) {
        super(color, backColor);
    }

    @Override
    protected void drawTexture(Graphics2D g) {
        g.drawLine(SIZE /2,0, SIZE /2, SIZE -1);
    }
}
