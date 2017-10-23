package net.royqh.jeasygraphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class LineTexturePaint implements Paint {
    private Paint paint;
    private final static int WIDTH=12;
    private final static int HEIGHT=12;
    private final static Rectangle RECT =new Rectangle(WIDTH,HEIGHT);

    public LineTexturePaint(Color color,Color backColor,FillPattern pattern) {
                generateTexturePaint(color,backColor,pattern);
    }

    private void generateTexturePaint(Color color, Color backColor, FillPattern pattern) {
        BufferedImage image=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=image.createGraphics();
        g.setColor(backColor);
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(color);
        switch(pattern) {
            case VERTICAL_FILL:
                g.drawLine(0,HEIGHT/2,WIDTH-1,HEIGHT/2);
                break;
            case SLASH_FILL:
                g.drawLine(0,HEIGHT-1,WIDTH-1,0);
                break;
            case BKSLASH_FILL:
                g.drawLine(0,0,WIDTH-1,HEIGHT-1);
                break;
            case HATCH_FILL:
                g.drawLine(0,HEIGHT/2,WIDTH-1,HEIGHT/2);
                g.drawLine(WIDTH/2,0,WIDTH/2,HEIGHT-1);
                break;
            case XHATCH_FILL:
                g.drawLine(0,HEIGHT-1,WIDTH-1,0);
                g.drawLine(0,0,WIDTH-1,HEIGHT-1);
                break;
            case HORIZONTAL_FILL:
                g.drawLine(WIDTH/2,0,WIDTH/2,HEIGHT-1);
                break;
        }
        g.dispose();
        paint=new TexturePaint(image, RECT);
    }

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        return paint.createContext(cm,deviceBounds,userBounds,xform,hints);
    }

    @Override
    public int getTransparency() {
        return paint.getTransparency();
    }

}
