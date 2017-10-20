package net.royqh.jeasygraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class ImageBuffer {
    private BufferedImage image;
    private int width;
    private int height;
    private ViewPortInfo viewPortInfo;
    private float linewidth;
    private Color color=Color.BLACK;
    private Color fillColor=Color.WHITE;
    private Color backgroundColor=Color.WHITE;
    private float lineWidth=1;
    private int lastLineX=0;
    private int lastLineY=0;

    public ImageBuffer(int width,int height) {
        this.width=width;
        this.height=height;
        this.viewPortInfo=new ViewPortInfo(0,0,width,height,true);
        this.image = new BufferedImage(width,height,TYPE_INT_ARGB);
        init();
    }

    private void init() {
        Graphics2D g=(Graphics2D)image.getGraphics();
        clear();
    }

    public Graphics2D getGraphics2D() {
        Graphics2D g=(Graphics2D)image.getGraphics();
        g.setColor(color);
        g.setBackground(backgroundColor);
        g.setStroke(new BasicStroke(lineWidth));
        /*
        if (viewPortInfo.clipOn) {
            g.setClip(viewPortInfo.left,viewPortInfo.top,
                    viewPortInfo.right-viewPortInfo.left,
                    viewPortInfo.bottom-viewPortInfo.top);
        }
        */
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        Graphics2D g=(Graphics2D)image.getGraphics();
    }

    public void clear(){
        Graphics2D g=(Graphics2D)image.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0,0,width,height);
    }

    public ViewPortInfo getViewPortInfo(){
        return viewPortInfo;
    }

    /**
     *  在指定图像页上画一条从(x1,y1)到(x2,y2)的线
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void line(int x1,int y1,int x2,int y2) {
        Graphics2D graphics=getGraphics2D();
        graphics.drawLine(x1,y1,x2,y2);
    }

    /**
     * 移动当前点坐标到 (x,y)
     * @param x
     * @param y
     */
    public void moveTo(int x,int y) {
        lastLineX=x;
        lastLineY=y;
    }

    /**
     * 从当前点画线到(x,y)。画线后当前点坐标变为（x,y）
     * @param x
     * @param y
     */
    public void lineTo(int x,int y) {
        line(lastLineX,lastLineY,x,y);
        lastLineX=x;
        lastLineY=y;
    }

    /**
     *  从当前点(x,y)画线到(x+dx,y+dy). 画线后当前点坐标变为（x+dx,y+dy）
     * @param dx
     * @param dy
     */
    public void lineRel(int dx,int dy){
        lineTo(lastLineX+dx,lastLineY+dy);
    }

    /**
     * 绘制圆弧
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param radius
     */
    public void arc(int x,int y,int startAngle, int endAngle, int radius){
        arc(x,y,startAngle,endAngle,radius,radius);
    }

    /**
     * 画椭圆弧
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param xRadius
     * @param yRadius
     */
    public void arc(int x,int y,int startAngle, int endAngle, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.drawArc(x-xRadius,y-yRadius,xRadius*2,yRadius*2,startAngle,endAngle-startAngle);
    }

    /**
     * 绘制圆形
     * @param x
     * @param y
     * @param radius
     */
    public void circle(int x,int y,int radius) {
        ellipse(x,y,radius,radius);
    }

    /**
     * 绘制矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void rectangle(int left, int top, int right, int bottom) {
        Graphics2D g=getGraphics2D();
        g.drawRect(left,top,right-left,bottom-top);
    }

    /**
     * 绘制圆角矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param radius
     */
    public void roundRect(int left, int top, int right, int bottom, int radius){
        roundRect(left,top,right,bottom,radius,radius);
    }

    /**
     * 绘制圆角矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param xRadius
     * @param yRadius
     */
    public void roundRect(int left, int top, int right, int bottom, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.drawRoundRect(left,top,right-left,bottom-top,xRadius,yRadius);
    }

    /**
     * 画椭圆
     * @param x
     * @param y
     * @param xRadius
     * @param yRadius
     */
    public void ellipse(int x,int y, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.drawOval(x-xRadius,y-yRadius,xRadius*2,yRadius*2);
    }

    /**
     * 从点(x,y)开始进行flood填充(使用fillColor),直到遇到borderColor为止
     * @param x
     * @param y
     * @param borderColor
     */
    public void floodFill(int x, int y, Color borderColor){
        x+=viewPortInfo.left;
        y+=viewPortInfo.top;
        if (x<0 || x>getWidth()) {
            return;
        }
        if (y<0 || y>getHeight()){
            return ;
        }
        Graphics2D g=getGraphics2D();
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

    private void floodfilladdPoint(Queue<Point> queue, int x, int y, boolean[][] processed) {
        if (x<0 || x>getWidth()) {
            return;
        }
        if (y<0 || y>getHeight()){
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
     * 绘制无边框填充矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void bar(int left, int top, int right, int bottom ) {
        fillRectangle(left,top,right,bottom);
    }

    /**
     * 绘制无边框填充矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void fillRectangle(int left, int top, int right, int bottom )  {
        Graphics2D g=getGraphics2D();
        g.setColor(fillColor);
        g.fillRect(left,top,right-left,bottom-top);
    }

    /**
     *  绘制无边框填充圆角矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param radius
     */
    public void fillRoundRect(int left, int top, int right, int bottom, int radius){
        fillRoundRect(left,top,right,bottom,radius,radius);
    }

    /**
     * 绘制无边框填充椭圆角矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param xRadius
     * @param yRadius
     */
    public void fillRoundRect(int left, int top, int right, int bottom, int xRadius,int yRadius)  {
        Graphics2D g=getGraphics2D();
        g.setColor(fillColor);
        g.fillRoundRect(left,top,right-left,bottom-top,xRadius,yRadius);
    }

    /**
     * 绘制无边框填充扇形
     *
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param radius
     */
    public void fillArc(int x,int y,int startAngle, int endAngle, int radius){
        fillArc(x,y,startAngle,endAngle,radius,radius);
    }

    /**
     * 绘制无边框填充扇形
     * 
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param xRadius
     * @param yRadius
     */
    public void fillArc(int x,int y,int startAngle, int endAngle, int xRadius,int yRadius){
        Graphics2D g=getGraphics2D();
        g.setColor(fillColor);
        g.fillArc(x,y,xRadius,yRadius,startAngle,endAngle-startAngle);
    }

    /**
     * 绘制无边框填充扇形
     *
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param xRadius
     * @param yRadius
     */
    public void sector(int x,int y,int startAngle, int endAngle, int xRadius,int yRadius){
        fillArc(x,y,startAngle,endAngle,xRadius,yRadius);
    }

    public void pie( int left, int top,int right,int bottom, int stangle,int endangle) {
        sector((left+right)/2,(top+bottom)/2,stangle,endangle,(right-left)/2,(bottom-top)/2);
    }
    
    /**
     * 绘制无边框填充圆形
     *
     * @param x
     * @param y
     * @param radius
     */
    public void fillCircle(int x,int y,int radius) {
        fillEllipse(x,y,radius,radius);
    }

    /**
     * 绘制无边框填充椭圆
     * @param x
     * @param y
     * @param xRadius
     * @param yRadius
     */
    public void fillEllipse(int x, int y, int xRadius, int yRadius) {
        Graphics2D g=getGraphics2D();
        g.setColor(fillColor);
        g.fillOval(x-xRadius,y-yRadius,xRadius*2,yRadius*2);
    }

    /**
     * 画无边框填充多边形
     * @param xPoints  每个点的X坐标
     * @param yPoints  每个点的Y坐标
     * @param numPoints 多边形点的个数
     */
    public void fillPoly(int[] xPoints, int[] yPoints, int numPoints) {
        Graphics2D g=getGraphics2D();
        g.setColor(fillColor);
        g.fillPolygon(xPoints,yPoints,numPoints);
    }

    /**
     * 画无边框填充多边形
     * @param numPoints 多边形点的个数
     * @param polyPoints 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void fillPoly(int numPoints, int[] polyPoints) {
        int[] xPoints=new int[numPoints];
        int[] yPoints=new int[numPoints];
        for (int i=0;i<numPoints;i++) {
            xPoints[i]=polyPoints[i*2];
            yPoints[i]=polyPoints[i*2+1];
        }
        fillPoly(xPoints,yPoints,numPoints);
    }

    /**
     * 绘制点(x,y)
     * @param x
     * @param y
     * @param color
     */
    public void putPixel(int x,int y,Color color) {
        image.setRGB(x+viewPortInfo.left,y+viewPortInfo.top,color.getRGB());
    }

    /**
     * 获取点(x,y)处的颜色
     * @param x
     * @param y
     * @return
     */
    public Color getPixel(int x,int y) {
        return new Color(image.getRGB(x+viewPortInfo.left,y+viewPortInfo.top));
    }

    /**
     * 画多边形
     * @param xPoints  每个点的X坐标
     * @param yPoints  每个点的Y坐标
     * @param numPoints 多边形点的个数
     */
    public void drawPoly(int[] xPoints, int[] yPoints, int numPoints) {
        Graphics2D g=getGraphics2D();
        g.drawPolygon(xPoints,yPoints,numPoints);
    }

    /**
     * 画多边形
     * @param numPoints 多边形点的个数
     * @param polyPoints 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void drawPoly(int numPoints, int[] polyPoints) {
        int[] xPoints=new int[numPoints];
        int[] yPoints=new int[numPoints];
        for (int i=0;i<numPoints;i++) {
            xPoints[i]=polyPoints[i*2];
            yPoints[i]=polyPoints[i*2+1];
        }
        drawPoly(xPoints,yPoints,numPoints);
    }

    /**
     * 设置视图
     *
     * @param left 视图左上角横坐标
     * @param top 视图左上角纵坐标      *
     * @param right
     * @param bottom
     * @param clipOn
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

}
