package net.royqh.jeasygraphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class LineTexturePaint implements Paint {
    private Paint paint;
    private final static int SIZE = 12;
    private final static Rectangle RECT =new Rectangle(SIZE, SIZE);

    public LineTexturePaint(Color color,Color backColor,FillPattern pattern) {
                generateTexturePaint(color,backColor,pattern);
    }

    private void generateTexturePaint(Color color, Color backColor, FillPattern pattern) {
        BufferedImage image=new BufferedImage(SIZE, SIZE,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=image.createGraphics();
        g.setColor(backColor);
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(0,0, SIZE, SIZE);
        g.setColor(color);
        switch(pattern) {
            case VERTICAL_FILL:
                g.drawLine(0, SIZE /2, SIZE -1, SIZE /2);
                break;
            case SLASH_FILL:
                g.drawLine(0,SIZE-1,SIZE-1,0);
                break;
            case BKSLASH_FILL:
                g.drawLine(0,0, SIZE -1, SIZE -1);
                break;
            case HATCH_FILL:
                g.drawLine(0, SIZE /2, SIZE -1, SIZE /2);
                g.drawLine(SIZE /2,0, SIZE /2, SIZE -1);
                break;
            case XHATCH_FILL:
                g.drawLine(0,SIZE-1,SIZE-1,0);
                g.drawLine(0,0, SIZE -1, SIZE -1);
                break;
            case HORIZONTAL_FILL:
                g.drawLine(SIZE /2,0, SIZE /2, SIZE -1);
                break;
        }
        g.dispose();
        paint=new TexturePaint(image, RECT);
    }

    private void drawBackSlashLine(BufferedImage image, Color color) {
        for (int i=0;i<SIZE;i++){
            image.setRGB(i,i,color.getRGB());
        }
    }

    private void drawSlashLine(BufferedImage image, Color color) {
        for (int i=0;i<SIZE;i++) {
            image.setRGB(i,SIZE-i-1,color.getRGB());
        }
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
