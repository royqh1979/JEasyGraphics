package net.royqh.jeasygraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.royqh.jeasygraphics.RenderMode.AUTO;
import static net.royqh.jeasygraphics.RenderMode.MANUAL;

public class JEasyGraphics implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    public final static int PAGE_SIZE=2;
    private static JEasyGraphics instance=null;
    private int width;
    private int height;
    private volatile int keyCode=-1;
    private MainFrame mainScreen;
    private RenderMode renderMode=AUTO;
    private ImageBuffer targetPage;
    private ImageBuffer[] imagePages;
    private JEasyGraphics(int width,int height){
        this.width=width;
        this.height=height;
        mainScreen=new MainFrame(width,height,this);
        initImages();
    }

    private void initImages() {
        imagePages=new ImageBuffer[PAGE_SIZE];
        for (int i=0;i<imagePages.length;i++) {
            imagePages[i]=createImage();
        }
        targetPage =imagePages[0];
    }

    public ImageBuffer createImage() {
        ImageBuffer imageBuffer=new ImageBuffer(width,height);
        return imageBuffer;
    }

    public ImageBuffer getTarget() {
        return targetPage;
    }

    /**
     * 设置绘制目标
     * @param imageBuffer
     */
    public void setTarget(ImageBuffer imageBuffer) {
        checkNotNull(imageBuffer);
        targetPage=imageBuffer;
    }

    public void setTarget() {
        targetPage=imagePages[0];
    }

    public RenderMode getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    /**
     * 设置绘制目标
     * @param i
     */
    public void setTarget(int i) {
        targetPage=imagePages[i];
    }

    /**
     * 在屏幕上画一条从(x1,y1)到(x2,y2)的线
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void line(int x1,int y1,int x2,int y2){
        targetPage.line(x1,y1,x2,y2);
        updateScreen();
    }


    /**
     *  绘制圆弧
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param radius
     */
    public void arc(int x,int y,int startAngle, int endAngle, int radius){
        arc(x,y,startAngle,endAngle,radius);
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
    public void arc(int x,int y,int startAngle, int endAngle, int xRadius,int yRadius) {
        targetPage.arc(x,y,startAngle,endAngle,xRadius,yRadius);
        updateScreen();
    }



    /**
     * 绘制圆形
     * @param x
     * @param y
     * @param radius
     */
    public void circle(int x,int y,int radius){
        targetPage.circle(x,y,radius);
        updateScreen();
    }


    /**
     * 绘制矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void rectangle(int left, int top, int right, int bottom){
        targetPage.rectangle(left,top,right,bottom);
        updateScreen();
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
        targetPage.roundRect(left,top,right,bottom,xRadius,yRadius);
        updateScreen();
    }



    /**
     * 画椭圆
     * @param x
     * @param y
     * @param xRadius
     * @param yRadius
     */
    public void ellipse(int x,int y,int xRadius,int yRadius) {
        targetPage.ellipse(x,y,xRadius,yRadius);
        updateScreen();
    }

    public void floodFill(int x, int y, Color color){
        targetPage.floodFill(x,y,color);
        updateScreen();
    }

    public Color getPixel(int x,int y) {
        return targetPage.getPixel(x,y);
    }

    public void putPixel(int x,int y, Color color) {
        targetPage.putPixel(x,y,color);
        updateScreen();
    }

    /**
     * 绘制无边框填充矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void bar(int left, int top, int right, int bottom ) {
        targetPage.bar(left,top,right,bottom);
        updateScreen();
    }

    /**
     * 绘制无边框填充矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void fillRectangle(int left, int top, int right, int bottom )  {
        targetPage.fillRectangle(left,top,right,bottom);
        updateScreen();
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
        targetPage.fillArc(x,y,startAngle,endAngle,radius);
        updateScreen();
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
        targetPage.fillArc(x,y,startAngle,endAngle,xRadius,yRadius);
        updateScreen();
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
        targetPage.sector(x, y, startAngle, endAngle, xRadius, yRadius);
        updateScreen();
    }

    public void pie( int left, int top,int right,int bottom, int stangle,int endangle) {
        targetPage.pie(left,top,right,bottom,stangle,endangle);
        updateScreen();
    }

    /**
     * 绘制无边框填充圆形
     *
     * @param x
     * @param y
     * @param radius
     */
    public void fillCircle(int x,int y,int radius) {
        targetPage.fillCircle(x,y,radius);
        updateScreen();
    }

    /**
     * 绘制无边框填充椭圆
     * @param x
     * @param y
     * @param xRadius
     * @param yRadius
     */
    public void fillEllipse(int x, int y, int xRadius, int yRadius) {
        targetPage.fillEllipse(x,y,xRadius,yRadius);
        updateScreen();
    }

    /**
     * 画无边框填充多边形
     * @param xPoints  每个点的X坐标
     * @param yPoints  每个点的Y坐标
     * @param numPoints 多边形点的个数
     */
    public void fillPoly(int[] xPoints, int[] yPoints, int numPoints) {
        targetPage.fillPoly(xPoints,yPoints,numPoints);
        updateScreen();
    }

    /**
     * 画无边框填充多边形
     * @param numPoints 多边形点的个数
     * @param polyPoints 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void fillPoly(int numPoints, int[] polyPoints) {
        targetPage.fillPoly(numPoints,polyPoints);
        updateScreen();
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
        targetPage.fillRoundRect(left,top,right,bottom,radius);
        updateScreen();
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
        targetPage.fillRoundRect(left,top,right,bottom,xRadius,yRadius);
        updateScreen();
    }

    /**
     * 画多边形
     * @param xPoints  每个点的X坐标
     * @param yPoints  每个点的Y坐标
     * @param numPoints 多边形点的个数
     */
    public void drawPoly(int[] xPoints, int[] yPoints, int numPoints) {
        targetPage.drawPoly(xPoints,yPoints,numPoints);
        updateScreen();
    }

    /**
     * 画多边形
     * @param numPoints 多边形点的个数
     * @param polyPoints 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void drawPoly(int numPoints, int[] polyPoints) {
        targetPage.drawPoly(numPoints,polyPoints);
        updateScreen();
    }

    /**
     * 清除当前屏幕
     */
    public void clear(){
        targetPage.clear();
        updateScreen();
    }

    private Runnable updateTargetRunnable=new Runnable() {
        @Override
        public void run() {
            onPaint(mainScreen.getContentPane().getGraphics());
        }
    };


    private void onPaint(Graphics g) {
        g.drawImage(targetPage.getImage(), 0, 0, null);
    }
    void redrawScreen() {
        EventQueue.invokeLater(updateTargetRunnable);
    }

    /**
     * 更新目标图像
     */
    private void updateScreen() {
        if (imagePages[0]==targetPage) {
            if (renderMode==AUTO) {
                redrawScreen();
            }
        }
    }

    public void pause() {
        keyboardSemaphore.drainPermits();
        getChar();
    }

    /**
     * 初始化图形系统
     * @param width 图形宽度
     * @param height 图形高度
     * @return JEasyGraphics对象实例
     */
    public static JEasyGraphics init(int width,int height){
        synchronized (JEasyGraphics.class){
            if (instance!=null) {
                throw new RuntimeException("JEasyGraphics can only init once!");
            }
            instance=new JEasyGraphics(width,height);
        }
        return instance;
    }

    public void setTitle(String title) {
        mainScreen.setTitle(title);
    }

    public MainFrame getMainScreen() {
        return mainScreen;
    }


    public void setFillColor(Color fillColor){
        setFillColor(fillColor,targetPage);
    }
    public void setFillColor(Color fillColor, ImageBuffer targetImage) {
        targetImage.setFillColor(fillColor);
    }

    public void setColor(Color color) {
        setColor(color,targetPage);
    }

    public void setColor(Color color,ImageBuffer targetImage) {
        targetImage.setColor(color);
    }

    public void setBackgroundColor(Color backgroundColor) {
        setBackgroundColor(backgroundColor,targetPage);
    }

    public void setBackgroundColor(Color backgroundColor,ImageBuffer targetImage) {
        targetImage.setBackgroundColor(backgroundColor);
    }

    public void setViewPort(int left,int top,int right,
                            int bottom,boolean clip) {
        targetPage.setViewPort(left,top,right,bottom,clip);
    }

    public void setLineWidth(float width) {
        targetPage.setLineWidth(width);
    }

    private Semaphore keyboardSemaphore =new Semaphore(0);
    public int getChar(){
        synchronized (this){
            if (renderMode==MANUAL) {
                redrawScreen();
            }
            try {
                keyboardSemaphore.acquire();
                int k=keyCode;
                return k;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    /**
     * 至少延迟以毫秒为单位的时间
     * @param milliseconds 要延迟的时间，以毫秒为单位
     */
    public void delay(long milliseconds) {
        redrawScreen();
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delay_ms(long milliseconds)  {
        delay(milliseconds);
    }

    private long delayFpsLast=0;

    public void delayFps(long fps) {
        double delay_time = 1000.0 / fps;
        double avg_max_time = delay_time * 10.0; // 误差时间在这个数值以内做平衡
        int nloop = 0;

        if (delayFpsLast == 0) {
            delayFpsLast = System.nanoTime() ;
        }
        redrawScreen();
        long dw = System.nanoTime()  ;
        if (delay_time>(dw-delayFpsLast)/1000000) {
            try {
                Thread.sleep((long)(delay_time-(dw-delayFpsLast)/1000000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

        }
        delayFpsLast=System.nanoTime();
    }

    public boolean kbhit() {
        return  keyboardSemaphore.availablePermits()>0;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyCode=e.getKeyCode();
        keyboardSemaphore.release(1);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        
    }

    public class MainFrame extends JFrame {
        private JEasyGraphics jEasyGraphics;

        public MainFrame(int width, int height, JEasyGraphics easyGraphics) {
            super("JEasyGraphics");
            this.jEasyGraphics=easyGraphics;
            init(width,height);
        }

        private void init(int width,int height) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setBounds(100,100,width,height);
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setVisible(true);
                    addKeyListener(jEasyGraphics);
                    addMouseListener(jEasyGraphics);
                    addMouseMotionListener(jEasyGraphics);
                    addMouseWheelListener(jEasyGraphics);
                }
            });
        }
        @Override
        public void paint(Graphics g) {
            jEasyGraphics.onPaint(g);
        }
    }
}
