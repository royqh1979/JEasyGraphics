package net.royqh.jeasygraphics.fillpattern;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public abstract class AbstractFillPatternPaint implements Paint {
    protected Paint paint;
    protected final static int SIZE = 12;
    protected final static Rectangle RECT =new Rectangle(SIZE, SIZE);

    public AbstractFillPatternPaint(Color color, Color backColor) {
                generateTexturePaint(color,backColor);
    }

    protected abstract void drawTexture(Graphics2D g);

    private void generateTexturePaint(Color color, Color backColor) {
        BufferedImage image=new BufferedImage(SIZE, SIZE,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=image.createGraphics();
        g.setColor(backColor);
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(0,0, SIZE, SIZE);
        g.setColor(color);
        drawTexture(g);
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
