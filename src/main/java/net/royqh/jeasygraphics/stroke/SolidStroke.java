package net.royqh.jeasygraphics.stroke;

import java.awt.*;

/**
 * 实线
 */
public class SolidStroke extends BasicStroke {
    public SolidStroke(float width) {
        super(width,CAP_ROUND,JOIN_ROUND);
    }
}
