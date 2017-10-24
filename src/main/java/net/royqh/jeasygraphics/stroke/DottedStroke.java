package net.royqh.jeasygraphics.stroke;

import java.awt.*;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;

/**
 * 线形为：● ● ● ● ●
 */
public class DottedStroke extends BasicStroke {
    public DottedStroke(float width) {
        super(width,CAP_ROUND,JOIN_ROUND,0,new float[]{1,width*2},0);
    }
}
