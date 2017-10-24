package net.royqh.jeasygraphics.fillpattern;

import java.awt.*;

public class XHatchFillPaint extends AbstractFillPatternPaint {
    public XHatchFillPaint(Color color, Color backColor) {
        super(color, backColor);
    }

    @Override
    protected void drawTexture(Graphics2D g) {
        g.drawLine(0,SIZE-1,SIZE-1,0);
        g.drawLine(0,0, SIZE -1, SIZE -1);
    }
}
