package net.royqh.jeasygraphics.stroke;

import java.awt.*;

/**
 * 线形为：－ － － － － －
 */
public class NullStroke extends BasicStroke {

    private static NullStroke instance=new NullStroke();
    private NullStroke() {
        super(0,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[]{0,1},0);
    }

    public static NullStroke getInstance() {
        return instance;
    }                       

}
