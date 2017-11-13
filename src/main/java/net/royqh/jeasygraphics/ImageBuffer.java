package net.royqh.jeasygraphics;

import net.royqh.jeasygraphics.stroke.SolidStroke;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class ImageBuffer {
    private BufferedImage image;
    private int width;
    private int height;
    private ViewPortInfo viewPortInfo;
    private LineStyle lineStyle=LineStyle.SOLID_LINE;
    private Stroke stroke=new SolidStroke(1);
    private Color color=Color.BLACK;
    private Color fillColor=Color.WHITE;
    private Color backgroundColor=Color.WHITE;
    private FillPattern fillPattern=FillPattern.SOLID_FILL;
    private Paint paint=Color.WHITE;
    private float lineWidth=1;
    private int lastLineX=0;
    private int lastLineY=0;
    private Font font=null;
    private AlphaComposite composite=AlphaComposite.SrcAtop;


    public ImageBuffer(int width,int height) {
        this.width=width;
        this.height=height;
        this.viewPortInfo=new ViewPortInfo(0,0,width,height,false);
        this.image = new BufferedImage(width,height,TYPE_INT_ARGB);
        clear();
    }

    public ImageBuffer(BufferedImage image,int width, int height) {
        this(width,height);
        //init();
        Graphics2D g=(Graphics2D)(this.image.getGraphics());
        g.drawImage(image,0,0,width,height,0,0,image.getWidth(),image.getHeight(),null);
        g.dispose();
    }

    public ImageBuffer(BufferedImage image) {
        this(image,image.getWidth(),image.getHeight());
    }

    private void init() {
        clear();
    }

    public Graphics2D getGraphics2D() {
        Graphics2D g=(Graphics2D)image.getGraphics();
        g.setBackground(backgroundColor);
        g.setColor(color);
        g.setStroke(stroke);
        g.setFont(getFont());
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (viewPortInfo.clipOn) {
            g.setClip(viewPortInfo.left, viewPortInfo.top, viewPortInfo.right - viewPortInfo.left,
                    viewPortInfo.bottom - viewPortInfo.top);
        }

        g.setComposite(composite);
        g.translate(viewPortInfo.left,viewPortInfo.top);
        return g;
    }

    public Graphics getGraphics() {
        return getGraphics2D();
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * get the foreground color
     * 获取前景色
     * @return the foreground color
     */
    public Color getColor() {
        return color;
    }

    /**
     * set the foreground color
     * 设置前景色
     * @param color the foreground color
     */
    public void setColor(Color color) {
        this.color = color;
        updatePaint();
    }

    public AlphaComposite getComposite() {
        return composite;
    }

    public void setComposite(AlphaComposite composite) {
        this.composite = composite;
    }

    public void setComposite() {
        this.composite = AlphaComposite.SrcAtop;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        updatePaint();
    }

    /**
     * get background color
     * 获取背景色
     * @return background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * set background color
     * 设置背景色
     * @param backgroundColor background color
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        Map<TextAttribute,Object> attrs=new HashMap<>();
        attrs.put(TextAttribute.BACKGROUND,backgroundColor);
        font=getFont().deriveFont(attrs);
        updatePaint();
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        updateStroke();
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
        checkNotNull(lineStyle);
        this.lineStyle = lineStyle;
        updateStroke();
    }

    private void updateStroke() {
        this.stroke=lineStyle.createStroke(lineWidth);
    }

    /**
     * clear the window(screen)
     * 清除当前屏幕
     */
    public void clear(){
        Graphics2D g=(Graphics2D)image.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0,0,width,height);
        g.dispose();
    }

    public ViewPortInfo getViewPort(){
        return viewPortInfo;
    }

    /**
     * clear view port setting
     * 清除视口设置
     */
    public void clearViewPort() {
        viewPortInfo.left=0;
        viewPortInfo.top=0;
        viewPortInfo.right=width;
        viewPortInfo.bottom=height;
        viewPortInfo.clipOn=false;
    }

    public void putImage(ImageBuffer imageBuffer,int x,int y) {
        Graphics2D g=getGraphics2D();
        g.drawImage(imageBuffer.getImage(),x,y,null);
        g.dispose();
    }

    public void putImage(ImageBuffer imageBuffer,int x,int y, int width, int height,int srcX,int srcY) {
        Graphics2D g=getGraphics2D();
        g.drawImage(imageBuffer.getImage(),x,y,x+width,y+height,srcX,srcY,srcX+width,srcY+height,null);
        g.dispose();
    }

    public void putImage(ImageBuffer imageBuffer,int x,int y, int width, int height, int srcX, int srcY, int srcWidth,int srcHeight) {
        Graphics2D g=getGraphics2D();
        g.drawImage(imageBuffer.getImage(),x,y,x+width,y+height,srcX,srcY,srcX+srcWidth,srcY+srcHeight,null);
        g.dispose();
    }

    public void putImageTransparent(ImageBuffer imageBuffer,int x,int y, int width, int height, int srcX, int srcY, Color transColor) {
        /** TODO **/
        BufferedImage srcImage=imageBuffer.getImage();
        x=x+viewPortInfo.left;
        y=y+viewPortInfo.top;
        for (int i=0;i<width;i++) {
            if (x+i>=image.getWidth() || (viewPortInfo.clipOn && x+i>viewPortInfo.right) || srcX+i >=srcImage.getWidth()) {
                break;
            }
            for (int j=0;j<height;j++) {
                if (y+j>=image.getHeight() || (viewPortInfo.clipOn && y+j>viewPortInfo.bottom) || srcY+j >=srcImage.getHeight()) {
                    break;
                }
                int rgb=srcImage.getRGB(srcX+i,srcY+j);
                if (rgb!=transColor.getRGB()) {
                    image.setRGB(x+i,y+j,rgb);
                }
            }
        }
    }

    /**
     * make all point with the specified color the transparent
     * 将指定颜色的所有点变为透明
     * @param color color to change
     */
    public void makeTransparent(Color color) {
        for (int i=0;i<getWidth();i++) {
            for (int j=0;j<getHeight();j++) {
                if (image.getRGB(i,j) ==color.getRGB()) {
                    image.setRGB(i, j, 0x00FFFFFF);
                }
            }
        }
    }

    public void putImageRotate(
            ImageBuffer imageBuffer,
            int xCenterDest,/* 旋转中心在目的图像的x坐标 */
            int yCenterDest,/* 旋转中心在目的图像的y坐标 */
            int xOriginSrc, /* 源图像复制区域左上角x坐标 */
            int yOriginSrc, /* 源图像复制区域左上角y坐标 */
            int widthSrc, /* 源图像复制区域宽度 */
            int heightSrc, /* 源图像复制区域高度 */
            int xCenterSrc, /* 旋转中心在源图像的x坐标 */
            int yCenterSrc,/* 旋转中心在源图像的y坐标 */
            double radian /* 逆时针旋转角度(弧度) */
    )  {
        Graphics2D g=getGraphics2D();
        g.rotate(radian,xCenterDest,yCenterDest);
        g.translate(xCenterDest,yCenterDest);
        g.drawImage(imageBuffer.getImage(),xOriginSrc-xCenterSrc,yOriginSrc-yCenterSrc,widthSrc,heightSrc,null);
        g.dispose();
    }

    public double textHeight(String text){
        return textBound(text).getHeight();
    }

    public double textWidth(String text) {
        return textBound(text).getWidth();
    }

    public Rectangle2D textBound(String text) {
        Graphics2D g=getGraphics2D();
        return g.getFontMetrics().getStringBounds(text,g);
    }

    /**
     * print a text on the specified window position
     * 在屏幕指定位置显示文字
     *
     * @param x the x cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param y the y cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param text the text to be outputted
     */
    public void outTextXY(int x,int y,String text) {
        Graphics2D g=getGraphics2D();
        g.drawString(text, x, y+g.getFont().getSize());
        g.dispose();
    }

    /**
     * print a text like the printf function of The C Language on the specified window position
     * 在屏幕指定文字显示格式化后的文字
     * @see String#format
     * @param x the x cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param y the y cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param fmt the format string
     * @param args the objects to be outputted
     */
    public void xyPrintf(int x,int y,String fmt , Object... args) {
        outTextXY(x,y,String.format(fmt,args));
    }

    public Font getFont() {
        if (font==null) {
            Graphics2D g=image.createGraphics();
            setFont(g.getFont());
            g.dispose();
        }
        return font;
    }

    public void setFont(Font font) {
        Map<TextAttribute,Object> attributes=new HashMap<>();
        attributes.put(TextAttribute.BACKGROUND,backgroundColor);
        this.font = font.deriveFont(attributes);
    }

    public void setFont(int height,String fontName) {
        setFont(height,fontName,false,false,false,false);
    }

    public void setFont(int height,String fontName,boolean isBold, boolean isItalic ,boolean isStrikeThrough,boolean isUnderLine) {
        Map<TextAttribute,Object> attributes=new HashMap<>();
        attributes.put(TextAttribute.FAMILY,fontName);
        attributes.put(TextAttribute.SIZE,height);
        if (isBold) {
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        }
        if (isItalic) {
            attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE) ;
        }
        if (isStrikeThrough) {
            attributes.put(TextAttribute.STRIKETHROUGH,TextAttribute.STRIKETHROUGH_ON);
        }
        if (isUnderLine) {
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        }
        attributes.put(TextAttribute.BACKGROUND,backgroundColor);
        setFont(new Font(attributes));
    }



    /**
     * draw a straight line from point(x1,y1) to (x2,y2)
     * 在屏幕上画一条从(x1,y1)到(x2,y2)的线
     *
     * @param x1  x cordinate of point 1
     * @param y1  y cordinate of point 1
     * @param x2  x cordinate of point 2
     * @param y2  y cordinate of point 2
     */
    public void line(int x1,int y1,int x2,int y2) {
        Graphics2D g=getGraphics2D();
        g.drawLine(x1,y1,x2,y2);
        g.dispose();
    }

    /**
     * move current point to (x,y)
     * 移动当前点坐标到 (x,y)
     * @param x x cordinate of current point
     * @param y y cordinate of current point
     */
    public void moveTo(int x,int y) {
        lastLineX=x;
        lastLineY=y;
    }

    /**
     * draw a line from current point to (x,y), then current point move to (x,y)
     * 从当前点画线到(x,y)。画线后当前点坐标变为（x,y）
     * @param x x cordinate of new current point
     * @param y y cordinate of new current point
     */
    public void lineTo(int x,int y) {
        line(lastLineX,lastLineY,x,y);
        lastLineX=x;
        lastLineY=y;
    }

    /**
     * draw from current point(x,y) to (x+dx,y+dy)
     *  从当前点(x,y)画线到(x+dx,y+dy). 画线后当前点坐标变为（x+dx,y+dy）
     * @param dx delta x
     * @param dy delta y
     */
    public void lineRel(int dx,int dy){
        lineTo(lastLineX+dx,lastLineY+dy);
    }

    /**
     * draw an arc
     * 绘制圆弧
     *
     * @param x  the x cordinate of the arc's center 圆弧圆心x坐标
     * @param y  the y cordinate of the arc's center 圆弧圆心y坐标
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     * @param radius  radius of the arc
     */
    public void arc(int x,int y,int startAngle, int endAngle, int radius){
        arc(x,y,startAngle,endAngle,radius,radius);
    }

    /**
     * draw an eclipse arc
     * 画椭圆弧
     *
     * @param x the x cordinate of the arc's center
     * @param y the y cordinate of the arc's center
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     * @param xRadius radius of the arc on the x-axis
     * @param yRadius radius of the arc on the y-axis
     */
    public void arc(int x,int y,int startAngle, int endAngle, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.drawArc(x-xRadius,y-yRadius,xRadius*2,yRadius*2,startAngle,endAngle-startAngle);
        g.dispose();
    }

    /**
     * draw a circle
     * 绘制圆形
     *
     * @param x the x cordinate of the circle's center
     * @param y the y cordinate of the circle's center
     * @param radius  radius of the circle
     */
    public void circle(int x,int y,int radius) {
        ellipse(x,y,radius,radius);
    }

    /**
     * draw a rectangle
     * 绘制矩形
     *
     * @param left left of the rectangle
     * @param top  top of the rectangle
     * @param right right of the rectangle
     * @param bottom  bottom of the rectangle
     */
    public void rectangle(int left, int top, int right, int bottom) {
        Graphics2D g=getGraphics2D();
        g.drawRect(left,top,right-left,bottom-top);
        g.dispose();
    }

    /**
     * draw a rectangle with rounded corner
     * 绘制圆角矩形
     *
     * @param left left of the rectangle
     * @param top  top of the rectangle
     * @param right right of the rectangle
     * @param bottom  bottom of the rectangle
     * @param radius  radius of the round corner circle
     */
    public void roundRect(int left, int top, int right, int bottom, int radius){
        roundRect(left,top,right,bottom,radius,radius);
    }

    /**
     * draw a rectangle with rounded corner
     * 绘制圆角矩形
     *
     * @param left left of the rectangle
     * @param top  top of the rectangle
     * @param right right of the rectangle
     * @param bottom  bottom of the rectangle
     * @param xRadius radius on the x-axis of the round corner circle
     * @param yRadius radius on the y-axis of the round corner circle
     */
    public void roundRect(int left, int top, int right, int bottom, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.drawRoundRect(left,top,right-left,bottom-top,xRadius,yRadius);
        g.dispose();
    }

    /**
     * draw an ellipse
     * 画椭圆
     *
     * @param x x cordinate of ellipse's center
     * @param y y cordinate of ellipse's center
     * @param xRadius radius on x-axis
     * @param yRadius radius on y-axis
     */
    public void ellipse(int x,int y, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.drawOval(x-xRadius,y-yRadius,xRadius*2,yRadius*2);
        g.dispose();
    }

    /**
     * floodfill start from point(x,y)
     * the area to be fill must be enclosed by the borderColor, or whole window will be filled!
     * 从点(x,y)开始进行flood填充(使用fillColor),直到遇到borderColor为止
     * @param x x cordinate of the start point
     * @param y y cordinate of the start point
     * @param borderColor border color of the fill area
     */
    public void floodFill(int x, int y, Color borderColor){
        x+=viewPortInfo.left;
        y+=viewPortInfo.top;
        if (!isXValidForFloodFill(x)) {
            return;
        }
        if (!isYValidForFloodFill(y)){
            return ;
        }
        Queue<Point> queue=new LinkedList<>();
        boolean[][] processed=new boolean[width+1][height+1];

        queue.add(new Point(x,y));
        processed[x][y]=true;
        int i=0;
        while (!queue.isEmpty()) {
            Point p=queue.poll() ;
            if (image.getRGB(p.x,p.y)!=borderColor.getRGB()) {
                image.setRGB(p.x,p.y,fillColor.getRGB());
                floodfilladdPoint(queue,p.x+1,p.y,processed);
                floodfilladdPoint(queue,p.x-1,p.y,processed);
                floodfilladdPoint(queue,p.x,p.y-1,processed);
                floodfilladdPoint(queue,p.x,p.y+1,processed);
            }
        }
    }

    private boolean isYValidForFloodFill(int y) {
        return y>=0 && y<getHeight() && ( !viewPortInfo.clipOn || (y<viewPortInfo.bottom && y>=viewPortInfo.top ));
    }

    private boolean isXValidForFloodFill(int x) {
        return x>=0 && x<getWidth() && ( !viewPortInfo.clipOn ||(x<viewPortInfo.right && x>=viewPortInfo.left));
    }

    private void floodfilladdPoint(Queue<Point> queue, int x, int y, boolean[][] processed) {
        if (!isXValidForFloodFill(x)) {
            return;
        }
        if (!isYValidForFloodFill(y)){
            return ;
        }
        if(processed[x][y]) {
            return;
        }
        Point newPoint=new Point(x,y);
        queue.add(newPoint);
        processed[x][y]=true;
    }

    /**
     * draw a filled rectangle without border
     * 绘制无边框填充矩形
     *
     * @param left left of the rectangle
     * @param top top of the rectangle
     * @param right  right of the rectangle
     * @param bottom  bottom of the rectangle
     */
    public void bar(int left, int top, int right, int bottom ) {
        Graphics2D g=getGraphics2D();
        g.setPaint(paint);
        g.fillRect(left,top,right-left,bottom-top);
        g.dispose();
    }

    /**
     * draw a filled rectangle with border
     * 绘制带边框填充矩形
     *
     * @param left left of the rectangle
     * @param top top of the rectangle
     * @param right  right of the rectangle
     * @param bottom  bottom of the rectangle
     */
    public void fillRectangle(int left, int top, int right, int bottom )  {
        Graphics2D g=getGraphics2D();
        g.setPaint(paint);
        g.fillRect(left,top,right-left,bottom-top);
        g.setColor(color);
        g.drawRect(left,top,right-left,bottom-top);
        g.dispose();
    }

    /**
     * draw a filled rectangle with rounded corner
     * 绘制带边框填充圆角矩形
     *
     * @param left left of the rectangle
     * @param top  top of the rectangle
     * @param right right of the rectangle
     * @param bottom  bottom of the rectangle
     * @param radius radius of the round corner circle
     */
    public void fillRoundRect(int left, int top, int right, int bottom, int radius){
        fillRoundRect(left,top,right,bottom,radius,radius);
    }

    /**
     * draw a filled rectangle with rounded corner
     * 绘制带边框填充圆角矩形
     *
     * @param left left of the rectangle
     * @param top  top of the rectangle
     * @param right right of the rectangle
     * @param bottom  bottom of the rectangle
     * @param xRadius radius on the x-axis of the round corner circle
     * @param yRadius radius on the y-axis of the round corner circle
     */
    public void fillRoundRect(int left, int top, int right, int bottom, int xRadius,int yRadius)  {
        Graphics2D g=getGraphics2D();
        g.setPaint(paint);
        g.fillRoundRect(left,top,right-left,bottom-top,xRadius,yRadius);
        g.setColor(color);
        g.drawRoundRect(left,top,right-left,bottom-top,xRadius,yRadius);
        g.dispose();
    }

    /**
     * draw an filled sector with border
     * 绘制带边框填充扇形
     *
     * @param x  the x cordinate of the arc's center
     * @param y  the y cordinate of the arc's center
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     * @param radius  radius of the arc
     */
    public void fillArc(int x,int y,int startAngle, int endAngle, int radius){
        fillArc(x,y,startAngle,endAngle,radius,radius);
    }

    /**
     * draw an filled ellipse sector with border
     * 绘制带边框填充扇形
     *
     * @param x the x cordinate of the arc's center
     * @param y the y cordinate of the arc's center
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     * @param xRadius radius of the arc on the x-axis
     * @param yRadius radius of the arc on the y-axis
     */
    public void fillArc(int x,int y,int startAngle, int endAngle, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.setPaint(paint);
        g.fillArc(x,y,xRadius,yRadius,startAngle,endAngle-startAngle);
        g.setColor(color);
        g.drawArc(x-xRadius,y-yRadius,xRadius*2,yRadius*2,startAngle,endAngle-startAngle);
        System.out.println(endAngle);
        System.out.println(x+","+y+","+(int)(x+xRadius*Math.cos(startAngle*Math.PI/180))+","+(int)(y+yRadius*Math.sin(startAngle*Math.PI/180)));
        System.out.println(x+","+y+","+(int)(x+xRadius*Math.cos(endAngle*Math.PI/180))+","+(int)(y+yRadius*Math.sin(endAngle*Math.PI/180)));
        g.drawLine(x,y,(int)(x+xRadius*Math.cos(startAngle*Math.PI/180)),(int)(y-yRadius*Math.sin(startAngle*Math.PI/180)));
        g.drawLine(x,y,(int)(x+xRadius*Math.cos(endAngle*Math.PI/180)),(int)(y-yRadius*Math.sin(endAngle*Math.PI/180)));
        g.dispose();
    }

    /**
     * draw an filled ellipse sector with border
     * 绘制带边框填充扇形
     *
     * @param x the x cordinate of the arc's center
     * @param y the y cordinate of the arc's center
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     * @param xRadius radius of the arc on the x-axis
     * @param yRadius radius of the arc on the y-axis
     */
    public void sector(int x,int y,int startAngle, int endAngle, int xRadius,int yRadius){
        fillArc(x,y,startAngle,endAngle,xRadius,yRadius);
    }
    /**
     * draw an filled ellipse sector with border
     * 绘制带边框填充扇形
     * @param left left of the bounded rectangle of the ellipse
     * @param top top of the bounded rectangle of the ellipse
     * @param right right of the bounded rectangle of the ellipse
     * @param bottom bottom of the bounded rectangle of the ellipse
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     */
    public void pie( int left, int top,int right,int bottom, int startAngle,int endAngle) {
        sector((left+right)/2,(top+bottom)/2,startAngle,endAngle,(right-left)/2,(bottom-top)/2);
    }

    /**
     * draw a filled circle with bound
     * 绘制带边框填充圆形
     *
     * @param x the x cordinate of the circle's center
     * @param y the y cordinate of the circle's center
     * @param radius  radius of the circle
     */
    public void fillCircle(int x,int y,int radius) {
        fillEllipse(x,y,radius,radius);
    }

    /**
     * draw a filled ellipse
     * 绘制带边框填充椭圆
     *
     * @param x x cordinate of the ellipse's center
     * @param y x cordinate of the elipse's center
     * @param xRadius  radius on the x-axis of the elipse's center
     * @param yRadius  radius on the y-axis of the elipse's center
     */
    public void fillEllipse(int x, int y, int xRadius, int yRadius) {
        Graphics2D g=getGraphics2D();
        g.setPaint(paint);
        g.fillOval(x-xRadius,y-yRadius,xRadius*2,yRadius*2);
        g.setColor(color);
        g.drawOval(x-xRadius,y-yRadius,xRadius*2,yRadius*2);
        g.dispose();
    }

    /**
     * draw a filled polygon
     * 画带边框填充多边形
     *
     * @param xPoints   x cordinates of each vertex 每个顶点的X坐标
     * @param yPoints   x cordinates of each vertex 每个顶点的Y坐标
     * @param numPoints num of vertexes 多边形点的个数
     */
    public void fillPoly(int[] xPoints, int[] yPoints, int numPoints) {
        Graphics2D g=getGraphics2D();
        g.setPaint(paint);
        g.fillPolygon(xPoints,yPoints,numPoints);
        g.setColor(color);
        g.drawPolygon(xPoints,yPoints,numPoints);
        g.dispose();
    }

    /**
     * draw a filled polygon
     * 画带边框填充多边形
     *
     * @param numPoints  num of vertexes 多边形点的个数
     * @param vertexes cordinates of each vertexes 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void fillPoly(int numPoints, int[] vertexes) {
        int[] xPoints=new int[numPoints];
        int[] yPoints=new int[numPoints];
        for (int i=0;i<numPoints;i++) {
            xPoints[i]=vertexes[i*2];
            yPoints[i]=vertexes[i*2+1];
        }
        fillPoly(xPoints,yPoints,numPoints);
    }

    /**
     * set color of the specified point
     * 设置指定点的颜色
     * @param x x cordinate of the point
     * @param y y cordinate of the point
     * @param color color to be set
     */
    public void putPixel(int x,int y,Color color) {
        image.setRGB(x+viewPortInfo.left,y+viewPortInfo.top,color.getRGB());
    }

    /**
     * get color of the specified point
     * 获取指定点的颜色
     * @param x x cordinate of the point
     * @param y y cordinate of the point
     * @return the point's color
     */
    public Color getPixel(int x,int y) {
        return new Color(image.getRGB(x+viewPortInfo.left,y+viewPortInfo.top));
    }

    /**
     * draw a polygon
     * 画多边形
     *
     * @param xPoints   x cordinates of each vertex 每个顶点的X坐标
     * @param yPoints   x cordinates of each vertex 每个顶点的Y坐标
     * @param numPoints num of vertexes 多边形点的个数
     */
    public void drawPoly(int[] xPoints, int[] yPoints, int numPoints) {
        Graphics2D g=getGraphics2D();
        g.drawPolygon(xPoints,yPoints,numPoints);
        g.dispose();
    }

    /**
     * draw a polygon
     * 画多边形
     *
     * @param numPoints  num of vertexes 多边形点的个数
     * @param vertexes cordinates of each vertexes 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void drawPoly(int numPoints, int[] vertexes) {
        int[] xPoints=new int[numPoints];
        int[] yPoints=new int[numPoints];
        for (int i=0;i<numPoints;i++) {
            xPoints[i]=vertexes[i*2];
            yPoints[i]=vertexes[i*2+1];
        }
        drawPoly(xPoints,yPoints,numPoints);
    }

    /**
     * set the view port
     * 设置视口
     * @param left left of the view
     * @param top  top of the view                                  v
     * @param right right of the view
     * @param bottom  bottom of the view
     * @param clipOn  if the content out of the view will be clipped
     */
    public void setViewPort(int left,int top,int right,
                            int bottom,boolean clipOn) {
        lastLineX=lastLineX-viewPortInfo.left;
        lastLineY=lastLineY-viewPortInfo.top;
        viewPortInfo.left=left;
        viewPortInfo.top=top;
        viewPortInfo.right=right;
        viewPortInfo.bottom=bottom;
        viewPortInfo.clipOn =clipOn;
        lastLineX=lastLineX+viewPortInfo.left;
        lastLineY=lastLineY+viewPortInfo.top;
    }

    public void dispose() {
    }

    public void setOrigin(int x, int y) {
        setViewPort(x,y,x+width,y+height,false);
    }

    public void resetOrigin() {
        clearViewPort();
    }

    public FillPattern getFillPattern() {
        return fillPattern;
    }

    public void setFillPattern(FillPattern fillPattern) {
        checkNotNull(fillPattern);
        this.fillPattern = fillPattern;
        updatePaint();
    }

    private void updatePaint() {
        paint=fillPattern.createPattern(color, fillColor, backgroundColor);
    }

    public void setWriteMode(AlphaComposite composite) {
        setComposite(composite);
    }
}
