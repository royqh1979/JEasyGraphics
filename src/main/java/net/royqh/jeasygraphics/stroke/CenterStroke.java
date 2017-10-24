package net.royqh.jeasygraphics.stroke;

import java.awt.*;

/**
 * 线形为：－ － － － － －
 */
public class CenterStroke extends BasicStroke {
    public final static float LINE_SIZE =5;
    public final static float EMPTY_SIZE =5;

    public CenterStroke(float width) {
        super(width,CAP_ROUND,JOIN_ROUND,0,new float[]{LINE_SIZE *width, EMPTY_SIZE *width},0);
    }
}
