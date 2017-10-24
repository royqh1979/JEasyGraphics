package net.royqh.jeasygraphics.fillpattern;

import java.awt.*;

public class BackSlashFillPaint extends AbstractFillPatternPaint {
    public BackSlashFillPaint(Color color, Color backColor) {
        super(color, backColor);
    }

    @Override
    protected void drawTexture(Graphics2D g) {
        g.drawLine(0,0, SIZE -1, SIZE -1);
    }
}
