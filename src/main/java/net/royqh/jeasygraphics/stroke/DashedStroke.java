package net.royqh.jeasygraphics.stroke;

import java.awt.*;

/**
 * 线形为：- ● - ● - ● - ●
 */
public class DashedStroke extends BasicStroke {
    public final static float LINE_SIZE =5;
    public final static float EMPTY_SIZE =2;

    public DashedStroke(float width) {
        super(width,CAP_ROUND,JOIN_ROUND,0,new float[]{LINE_SIZE *width, EMPTY_SIZE * width, 1, EMPTY_SIZE*width},0);
    }
}
