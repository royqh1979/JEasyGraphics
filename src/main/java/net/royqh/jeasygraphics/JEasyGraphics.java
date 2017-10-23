package net.royqh.jeasygraphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.AttributedString;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.royqh.jeasygraphics.RenderMode.RENDER_AUTO;
import static net.royqh.jeasygraphics.RenderMode.RENDER_MANUAL;

public class JEasyGraphics {
    public final static int PAGE_SIZE = 2;
    private static JEasyGraphics instance = null;
    private int width;
    private int height;
    private volatile KeyEvent keyEvent = null;
    private volatile int keyCode = -1;
    private volatile MouseMsg mouseMsg = null;
    private long lastKeyTime = 0;
    private long lastMouseTime = 0;
    private MainFrame mainScreen;
    private RenderMode renderMode = RENDER_AUTO;
    private ImageBuffer targetPage;
    private ImageBuffer[] imagePages;

    private JEasyGraphics(int width, int height) {
        this.width = width;
        this.height = height;
        mainScreen = new MainFrame(width, height);
        initImages();
    }

    private void initImages() {
        imagePages = new ImageBuffer[PAGE_SIZE];
        for (int i = 0; i < imagePages.length; i++) {
            imagePages[i] = createImage();
        }
        targetPage = imagePages[0];
    }

    /**
     * 创建一个和当前窗口大小相同的图片
     *
     * @return
     */
    public ImageBuffer createImage() {
        ImageBuffer imageBuffer = new ImageBuffer(width, height);
        return imageBuffer;
    }

    /**
     * 创建一个指定大小的图片
     *
     * @param width
     * @param height
     * @return
     */
    public ImageBuffer createImage(int width, int height) {
        return new ImageBuffer(width, height);
    }

    /**
     * 从图像文件创建图片
     *
     * @param imageFile
     * @return
     * @throws IOException
     */
    public ImageBuffer createImage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        return new ImageBuffer(image);
    }

    /**
     * 从图像文件创建图片
     *
     * @param path
     * @return
     * @throws IOException
     */
    public ImageBuffer createImage(String path) throws IOException {
        File file = new File(path);
        return createImage(file);
    }

    /**
     * 从当前屏幕抓取图像
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public ImageBuffer getImage(int x, int y, int width, int height) {
        ImageBuffer buffer = createImage(width, height);
        buffer.putImage(targetPage, 0, 0, width, height, x, y);
        return buffer;
    }

    public void putImage(ImageBuffer imageBuffer, int x, int y) {
        targetPage.putImage(imageBuffer, x, y);
        updateScreen();
    }

    public void putImage(ImageBuffer imageBuffer, int x, int y, int width, int height, int srcX, int srcY) {
        targetPage.putImage(imageBuffer, x, y, width, height, srcX, srcY);
        updateScreen();
    }

    public void putImage(ImageBuffer imageBuffer, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
        targetPage.putImage(imageBuffer, x, y, width, height, srcX, srcY, srcWidth, srcHeight);
        updateScreen();
    }

    public void putImageTransparent(ImageBuffer imageBuffer, int x, int y, int width, int height, int srcX, int srcY, Color transColor) {
        targetPage.putImageTransparent(imageBuffer, x, y, width, height, srcX, srcY, transColor);
        updateScreen();
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
    ) {
        targetPage.putImageRotate(imageBuffer,xCenterDest,yCenterDest,
                xOriginSrc,yOriginSrc,widthSrc,heightSrc,xCenterSrc,yCenterSrc,radian);
    }

    public double textHeight(String text) {
        return targetPage.textHeight(text);
    }

    public double textWidth(String text) {
        return targetPage.textWidth(text);
    }

    public Rectangle2D textBound(String text) {
        return targetPage.textBound(text);
    }

    public Font getFont() {
        return targetPage.getFont();
    }

    public void setFont(Font font) {
        targetPage.setFont(font);
    }

    public void setFont(int height, String fontName) {
        targetPage.setFont(height, fontName);
    }

    public void setFont(int height, String fontName, boolean isBold, boolean isItalic, boolean isStrikeThrough, boolean isUnderLine) {
        targetPage.setFont(height, fontName, isBold, isItalic, isStrikeThrough, isUnderLine);
    }

    public void outTextXY(int x, int y, String text) {
        targetPage.outTextXY(x, y, text);
        updateScreen();
    }

    public void xyPrintf(int x, int y, String fmt, Object... args) {
        targetPage.xyPrintf(x, y, fmt, args);
        updateScreen();
    }


    public ImageBuffer getTarget() {
        return targetPage;
    }

    /**
     * 设置视图原点到(x,y)。以后绘图时，点(x1,y1)对应于实际的点(x+x1,y+y1)
     * @param x
     * @param y
     */
    public void setOrigin(int x, int y) {
        targetPage.setOrigin(x,y);
    }

    public void resetOrigin() {
        targetPage.resetOrigin();
    }

    /**
     * 设置绘制目标
     *
     * @param imageBuffer
     */
    public void setTarget(ImageBuffer imageBuffer) {
        checkNotNull(imageBuffer);
        targetPage = imageBuffer;
    }

    public void setTarget() {
        targetPage = imagePages[0];
    }

    public RenderMode getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    /**
     * 设置绘制目标
     *
     * @param i
     */
    public void setTarget(int i) {
        targetPage = imagePages[i];
    }

    /**
     * 在屏幕上画一条从(x1,y1)到(x2,y2)的线
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void line(int x1, int y1, int x2, int y2) {
        targetPage.line(x1, y1, x2, y2);
        updateScreen();
    }


    /**
     * 绘制圆弧
     *
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param radius
     */
    public void arc(int x, int y, int startAngle, int endAngle, int radius) {
        arc(x, y, startAngle, endAngle, radius);
    }

    /**
     * 画椭圆弧
     *
     * @param x
     * @param y
     * @param startAngle
     * @param endAngle
     * @param xRadius
     * @param yRadius
     */
    public void arc(int x, int y, int startAngle, int endAngle, int xRadius, int yRadius) {
        targetPage.arc(x, y, startAngle, endAngle, xRadius, yRadius);
        updateScreen();
    }


    /**
     * 绘制圆形
     *
     * @param x
     * @param y
     * @param radius
     */
    public void circle(int x, int y, int radius) {
        targetPage.circle(x, y, radius);
        updateScreen();
    }


    /**
     * 绘制矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void rectangle(int left, int top, int right, int bottom) {
        targetPage.rectangle(left, top, right, bottom);
        updateScreen();
    }

    /**
     * 绘制圆角矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param radius
     */
    public void roundRect(int left, int top, int right, int bottom, int radius) {
        roundRect(left, top, right, bottom, radius, radius);
    }

    /**
     * 绘制圆角矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param xRadius
     * @param yRadius
     */
    public void roundRect(int left, int top, int right, int bottom, int xRadius, int yRadius) {
        targetPage.roundRect(left, top, right, bottom, xRadius, yRadius);
        updateScreen();
    }


    /**
     * 画椭圆
     *
     * @param x
     * @param y
     * @param xRadius
     * @param yRadius
     */
    public void ellipse(int x, int y, int xRadius, int yRadius) {
        targetPage.ellipse(x, y, xRadius, yRadius);
        updateScreen();
    }

    public void floodFill(int x, int y, Color color) {
        targetPage.floodFill(x, y, color);
        updateScreen();
    }

    public Color getPixel(int x, int y) {
        return targetPage.getPixel(x, y);
    }

    public void putPixel(int x, int y, Color color) {
        targetPage.putPixel(x, y, color);
        updateScreen();
    }

    /**
     * 绘制无边框填充矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void bar(int left, int top, int right, int bottom) {
        targetPage.bar(left, top, right, bottom);
        updateScreen();
    }

    /**
     * 绘制无边框填充矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void fillRectangle(int left, int top, int right, int bottom) {
        targetPage.fillRectangle(left, top, right, bottom);
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
    public void fillArc(int x, int y, int startAngle, int endAngle, int radius) {
        targetPage.fillArc(x, y, startAngle, endAngle, radius);
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
    public void fillArc(int x, int y, int startAngle, int endAngle, int xRadius, int yRadius) {
        targetPage.fillArc(x, y, startAngle, endAngle, xRadius, yRadius);
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
    public void sector(int x, int y, int startAngle, int endAngle, int xRadius, int yRadius) {
        targetPage.sector(x, y, startAngle, endAngle, xRadius, yRadius);
        updateScreen();
    }

    public void pie(int left, int top, int right, int bottom, int stangle, int endangle) {
        targetPage.pie(left, top, right, bottom, stangle, endangle);
        updateScreen();
    }

    /**
     * 绘制无边框填充圆形
     *
     * @param x
     * @param y
     * @param radius
     */
    public void fillCircle(int x, int y, int radius) {
        targetPage.fillCircle(x, y, radius);
        updateScreen();
    }

    /**
     * 绘制无边框填充椭圆
     *
     * @param x
     * @param y
     * @param xRadius
     * @param yRadius
     */
    public void fillEllipse(int x, int y, int xRadius, int yRadius) {
        targetPage.fillEllipse(x, y, xRadius, yRadius);
        updateScreen();
    }

    /**
     * 画无边框填充多边形
     *
     * @param xPoints   每个点的X坐标
     * @param yPoints   每个点的Y坐标
     * @param numPoints 多边形点的个数
     */
    public void fillPoly(int[] xPoints, int[] yPoints, int numPoints) {
        targetPage.fillPoly(xPoints, yPoints, numPoints);
        updateScreen();
    }

    /**
     * 画无边框填充多边形
     *
     * @param numPoints  多边形点的个数
     * @param polyPoints 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void fillPoly(int numPoints, int[] polyPoints) {
        targetPage.fillPoly(numPoints, polyPoints);
        updateScreen();
    }

    /**
     * 绘制无边框填充圆角矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param radius
     */
    public void fillRoundRect(int left, int top, int right, int bottom, int radius) {
        targetPage.fillRoundRect(left, top, right, bottom, radius);
        updateScreen();
    }

    /**
     * 绘制无边框填充椭圆角矩形
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param xRadius
     * @param yRadius
     */
    public void fillRoundRect(int left, int top, int right, int bottom, int xRadius, int yRadius) {
        targetPage.fillRoundRect(left, top, right, bottom, xRadius, yRadius);
        updateScreen();
    }

    /**
     * 画多边形
     *
     * @param xPoints   每个点的X坐标
     * @param yPoints   每个点的Y坐标
     * @param numPoints 多边形点的个数
     */
    public void drawPoly(int[] xPoints, int[] yPoints, int numPoints) {
        targetPage.drawPoly(xPoints, yPoints, numPoints);
        updateScreen();
    }

    /**
     * 画多边形
     *
     * @param numPoints  多边形点的个数
     * @param polyPoints 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void drawPoly(int numPoints, int[] polyPoints) {
        targetPage.drawPoly(numPoints, polyPoints);
        updateScreen();
    }

    /**
     * 清除当前屏幕
     */
    public void clear() {
        targetPage.clear();
        updateScreen();
    }

    private Runnable updateTargetRunnable = new Runnable() {
        @Override
        public void run() {
            onPaint();
        }
    };


    private void onPaint() {
        if (targetPage == null) {
            return;
        }
        mainScreen.getContentPane().getGraphics().drawImage(targetPage.getImage(), 0, 0, null);
    }

    void redrawScreen() {
        EventQueue.invokeLater(updateTargetRunnable);
    }

    /**
     * 更新目标图像
     */
    private void updateScreen() {
        if (imagePages[0] == targetPage) {
            if (renderMode == RENDER_AUTO) {
                redrawScreen();
            }
        }
    }

    /**
     * 暂停主程序运行,直到按下任意键
     */
    public void pause() {
        getCh();
    }

    public boolean isRun() {
        return true;
    }

    /**
     * 这个函数用于判断某按键是否被按下。
     *
     * @param keyCode
     * @return
     */
    public boolean keyState(int keyCode) {
        checkKeyEventTime();
        return (keyCode == this.keyCode);
    }

    /**
     * 初始化图形系统
     *
     * @param width  图形宽度
     * @param height 图形高度
     * @return JEasyGraphics对象实例
     */
    public static JEasyGraphics init(int width, int height) {
        synchronized (JEasyGraphics.class) {
            if (instance != null) {
                throw new RuntimeException("JEasyGraphics can only init once!");
            }
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            instance = new JEasyGraphics(width, height);
        }
        return instance;
    }

    public void setTitle(String title) {
        mainScreen.setTitle(title);
    }

    public JFrame getMainScreen() {
        return mainScreen;
    }


    public void setFillColor(Color fillColor) {
        targetPage.setFillColor(fillColor);
    }

    public void setColor(Color color) {
        targetPage.setColor(color);
    }

    public Color getColor() {
        return targetPage.getColor();
    }

    public Color getFillColor() {
        return targetPage.getColor();
    }

    public Color getBackgroundColor() {
        return targetPage.getColor();
    }

    public void setBackgroundColor(Color backgroundColor) {
        targetPage.setBackgroundColor(backgroundColor);
    }

    public void setViewPort(int left, int top, int right,
                            int bottom, boolean clip) {
        targetPage.setViewPort(left, top, right, bottom, clip);
    }

    public ViewPortInfo getViewPort() {
        return targetPage.getViewPort();
    }

    public void clearViewPort() {
        targetPage.clearViewPort();
    }

    public void setLineWidth(float width) {
        targetPage.setLineWidth(width);
    }

    public float getLineWidth() {
        return targetPage.getLineWidth();
    }

    public void setLineStyle(LineStyle lineStyle) {
        targetPage.setLineStyle(lineStyle);
    }

    public LineStyle getLineStyle() {
        return targetPage.getLineStyle();
    }


    private Semaphore keyCodeSemaphore = new Semaphore(0);
    private Semaphore keyMsgSemaphore = new Semaphore(0);
    private Semaphore mouseMsgSemaphore = new Semaphore(0);

    /**
     * 这个函数用于检测当前是否有键盘消息
     *
     * @return
     */
    public boolean kbMsg() {
        checkKeyEventTime();
        return keyMsgSemaphore.availablePermits() > 0;
    }

    /**
     * 这个函数用于获取键盘消息，如果当前没有消息，则等待。
     *
     * @return
     */
    public KeyEvent getKey() {
        synchronized (this) {
            if (renderMode == RENDER_MANUAL) {
                redrawScreen();
            }
            try {
                keyMsgSemaphore.acquire();
                KeyEvent k = keyEvent;
                keyEvent = null;
                return k;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public String inputbox(String notice) {
        return JOptionPane.showInputDialog(mainScreen, notice);
    }

    public ConfirmOption confirm(String notice) {
        int op = JOptionPane.showConfirmDialog(mainScreen, notice);
        if (op == JOptionPane.YES_OPTION) {
            return ConfirmOption.YES;
        }
        if (op == JOptionPane.NO_OPTION) {
            return ConfirmOption.NO;
        }
        return ConfirmOption.CANCLE;
    }

    /**
     * 这个函数用于获取键盘字符输入，如果当前没有输入，则等待。
     *
     * @return
     */
    public int getCh() {
        synchronized (this) {
            if (renderMode == RENDER_MANUAL) {
                redrawScreen();
            }
            try {
                checkKeyEventTime();
                keyCodeSemaphore.acquire();
                int code = keyCode;
                keyCode = -1;
                return code;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }

    /**
     * 至少延迟以毫秒为单位的时间
     *
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

    private long delayFpsLast = 0;

    /**
     * 延迟1000/fps毫秒时间
     *
     * @param fps
     */
    public void delayFps(long fps) {
        double delay_time = 1000.0 / fps;
        int nloop = 0;

        if (delayFpsLast == 0) {
            delayFpsLast = System.nanoTime();
        }
        redrawScreen();
        long dw = System.nanoTime();
        if (delay_time > (dw - delayFpsLast) / 1000000) {
            try {
                Thread.sleep((long) (delay_time - (dw - delayFpsLast) / 1000000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

        }
        delayFpsLast = System.nanoTime();
    }

    /**
     * 这个函数用于检测当前是否有键盘字符输入
     *
     * @return
     */
    public boolean kbHit() {
        checkKeyEventTime();
        return keyCodeSemaphore.availablePermits() > 0;
    }

    private void checkKeyEventTime() {
        long now = System.nanoTime();
        if (now - lastKeyTime > 500000000) {
            keyCode = -1;
            keyEvent = null;
            keyCodeSemaphore.drainPermits();
            keyMsgSemaphore.drainPermits();
        }
    }

    private void checkMouseEventTime() {
        long now = System.nanoTime();
        if (now - lastKeyTime > 500000000) {
            mouseMsg = null;
            mouseMsgSemaphore.drainPermits();
        }
    }

    /**
     * 关闭主窗口
     */
    public void close() {
        mainScreen.dispose();
    }

    private Point lastMousePos=new Point(0,0);
    /**
     * 获取当前鼠标坐标
     *
     * @return
     */
    public Point mousePos() {
        Point p = mainScreen.getContentPane().getMousePosition();
        if (p==null) {
            p=lastMousePos;
        }
        lastMousePos=p;
        return convertCordinate(p);
    }

    /**
     * 这个函数用于获取一个鼠标消息。如果当前鼠标消息队列中没有，就一直等待。
     *
     * @return
     */
    public MouseMsg getMouse() {
        synchronized (this) {
            if (renderMode == RENDER_MANUAL) {
                redrawScreen();
            }
            try {
                checkMouseEventTime();
                mouseMsgSemaphore.acquire();
                MouseMsg message = mouseMsg;
                mouseMsg = null;
                return message;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 这个函数用于检测当前是否有鼠标消息
     *
     * @return
     */
    public boolean mouseMsg() {
        checkMouseEventTime();
        return mouseMsgSemaphore.drainPermits() > 0;
    }

    public Point convertCordinate(Point p) {
        return new Point(p.x + targetPage.getViewPort().left,
                p.y + targetPage.getViewPort().top);
    }

    public int getWidth() {
        return targetPage.getWidth();
    }

    public int getHeight() {
        return targetPage.getHeight();
    }

    public void setFillPattern(FillPattern fillPattern) {
        targetPage.setFillPattern(fillPattern);
    }

    public class MainFrame extends JFrame {

        public MainFrame(int width, int height) {
            super("JEasyGraphics");
            init(width, height);
        }

        private void init(int width, int height) {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        getContentPane().setPreferredSize(new Dimension(width, height));
                        pack();
                        setResizable(false);
                        setLocation(100, 100);
                        setVisible(true);
                        addKeyListener(eventHandler);
                        addMouseWheelListener(eventHandler);
                        addMouseListener(eventHandler);
                        addMouseMotionListener(eventHandler);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paint(Graphics g) {
            onPaint();
        }

    }

    public EventHandler eventHandler = new EventHandler();

    public class EventHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            lastKeyTime = System.nanoTime();
            keyEvent = e;
            keyCode = e.getKeyCode();
            keyCodeSemaphore.release(1);
            keyMsgSemaphore.release(1);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        private void onMouseEvent(MouseEvent e) {
            lastMouseTime = System.nanoTime();
            MouseMsg mouseMsg = new MouseMsg(targetPage.getViewPort().left,
                    targetPage.getViewPort().top,
                    e);
            mouseMsgSemaphore.release(1);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            onMouseEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            onMouseEvent(e);
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
            onMouseEvent(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            onMouseEvent(e);
        }

    }
}
