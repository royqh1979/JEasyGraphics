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

import static com.google.common.base.Preconditions.checkElementIndex;
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

    /**
     * @param width width of the program window
     * @param height width of the program window
     */
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
     * Create an image which has the same size with the current window
     * 创建一个和当前窗口大小相同的图片
     *
     * @return the created image
     */
    public ImageBuffer createImage() {
        ImageBuffer imageBuffer = new ImageBuffer(width, height);
        return imageBuffer;
    }

    /**
     * create an image with the provided size
     * 创建一个指定大小的图片
     *
     * @param width the width of the image
     * @param height the height of the image
     * @return the created image
     */
    public ImageBuffer createImage(int width, int height) {
        return new ImageBuffer(width, height);
    }

    /**
     * create image from an image file
     * 从图像文件创建图片
     *
     * @param imageFile the image File
     * @return the created image
     * @throws IOException
     */
    public ImageBuffer createImage(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        return new ImageBuffer(image);
    }

    /**
     * create image for an image file
     * 从图像文件创建图片
     *
     * @param path  path to the image file
     * @return the created image
     * @throws IOException
     */
    public ImageBuffer createImage(String path) throws IOException {
        File file = new File(path);
        return createImage(file);
    }

    /**
     * create image by capture current window
     * 从当前屏幕抓取图像
     *
     * @param x the x cordinate of the top-left point of the captured area
     * @param y the y cordinate of the top-left point of the captured area
     * @param width width of the captured area
     * @param height width of the captured area
     * @return new image with the captured content
     */
    public ImageBuffer getImage(int x, int y, int width, int height) {
        ImageBuffer buffer = createImage(width, height);
        buffer.putImage(targetPage, 0, 0, width, height, x, y);
        return buffer;
    }

    /**
     * copy an image to the window
     * @param imageBuffer the image to be copied
     * @param x the x cordinate on the window of the image's left-top corner
     * @param y the y cordinate on the window of the image's left-top corner
     */
    public void putImage(ImageBuffer imageBuffer, int x, int y) {
        targetPage.putImage(imageBuffer, x, y);
        updateScreen();
    }

    /**
     * copy an image to the window
     * @param imageBuffer  the image to be copied
     * @param x  the x cordinate of the left-top corner of the copied area on the window
     * @param y  the y cordinate of the left-top corner of the copied area on the window
     * @param width  the width of the copied area on the window and the source image
     * @param height  the height of the copied area on the window and the source image
     * @param srcX the x cordinate of the left-top corner of the copied area on the source image
     * @param srcY the y cordinate of the left-top corner of the copied area on the source image
     */
    public void putImage(ImageBuffer imageBuffer, int x, int y, int width, int height, int srcX, int srcY) {
        targetPage.putImage(imageBuffer, x, y, width, height, srcX, srcY);
        updateScreen();
    }

    /**
     * copy an image to the window
     * @param imageBuffer  the image to be copied
     * @param x  the x cordinate of the left-top corner of the copied area on the window
     * @param y  the y cordinate of the left-top corner of the copied area on the window
     * @param width  the width of the copied image on the window
     * @param height the height of the copied image on the window
     * @param srcX  the x cordinate of the left-top corner of the copied area on the source image
     * @param srcY  the y cordinate of the left-top corner of the copied area on the source image
     * @param srcWidth  the width of the copied image on the source image
     * @param srcHeight the height of the copied image on the source image
     */
    public void putImage(ImageBuffer imageBuffer, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
        targetPage.putImage(imageBuffer, x, y, width, height, srcX, srcY, srcWidth, srcHeight);
        updateScreen();
    }

    /**
     * copy an image to the window and transparent(ignore) the specified color
     * @param imageBuffer  the image to be copied
     * @param x the x cordinate of the left-top corner of the copied area on the window
     * @param y the y cordinate of the left-top corner of the copied area on the window
     * @param width the width of the copied area on the window and the source image
     * @param height the height of the copied area on the window and the source image
     * @param srcX the x cordinate of the left-top corner of the copied area on the source image
     * @param srcY  the y cordinate of the left-top corner of the copied area on the source image
     * @param transColor the color which will be made transparent
     */
    public void putImageTransparent(ImageBuffer imageBuffer, int x, int y, int width, int height, int srcX, int srcY, Color transColor) {
        targetPage.putImageTransparent(imageBuffer, x, y, width, height, srcX, srcY, transColor);
        updateScreen();
    }

    /**
     * copy an rotated image to the screen
     * @param imageBuffer  the image to be copied
     * @param xCenterDest the x cordinate of rotation center of the copied area on the window
     * @param yCenterDest the y cordinate of rotation center of the copied area on the window
     * @param xOriginSrc  the x cordinate of the left-top corner of the copied area on the source image before rotation
     * @param yOriginSrc  the y cordinate of the left-top corner of the copied area on the source image before rotation
     * @param widthSrc  the width of the copied area on the source image before rotation
     * @param heightSrc the height of the copied area on the source image before rotation
     * @param xCenterSrc the x cordinate of rotation center of the copied area on the source image before rotation
     * @param yCenterSrc the y cordinate of trotation center of the copied area on the source image before rotation
     * @param radian the radian of the rotation(counter-clock wise)
     */
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

    /**
     * Calcuate height of the outputted text with current font
     * @param text the text to be outputted on the screen
     * @return height of the outputted text with current font
     */
    public double textHeight(String text) {
        return targetPage.textHeight(text);
    }

    /**
     * Calcuate width of the outputted text with current font
     * @param text the text to be outputted on the screen
     * @return width of the outputted text with current font
     */
    public double textWidth(String text) {
        return targetPage.textWidth(text);
    }

    /**
     * Calcuate bound of the outputted text with current font
     * @param text the text to be outputted on the screen
     * @return a rectangle reflecting the bound of the outputted text with current font
     */
    public Rectangle2D textBound(String text) {
        return targetPage.textBound(text);
    }

    /**
     * get current font setting of the window
     * @return
     */
    public Font getFont() {
        return targetPage.getFont();
    }

    /**
     * set the font of the window
     * @param font the font to be used
     */
    public void setFont(Font font) {
        targetPage.setFont(font);
    }

    /**
     * set the font of the window
     * @param height  height of the font
     * @param fontName name of the font
     */
    public void setFont(int height, String fontName) {
        targetPage.setFont(height, fontName);
    }

    /**
     * set the font of the window
     * @param height height of the font
     * @param fontName name of the font
     * @param isBold if the font is bold
     * @param isItalic  if the font is italid
     * @param isStrikeThrough if the font has strike through
     * @param isUnderLine if the font has underline
     */
    public void setFont(int height, String fontName, boolean isBold, boolean isItalic, boolean isStrikeThrough, boolean isUnderLine) {
        targetPage.setFont(height, fontName, isBold, isItalic, isStrikeThrough, isUnderLine);
    }

    /**
     * print a text on the specified window position
     * @param x the x cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param y the y cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param text the text to be outputted
     */
    public void outTextXY(int x, int y, String text) {
        targetPage.outTextXY(x, y, text);
        updateScreen();
    }

    /**
     * print a text like the printf function of The C Language on the specified window position
     * @See String#format
     * @param x the x cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param y the y cordinate of the left-top corner of the bound rectangle of the outputted text
     * @param fmt the format string
     * @param args the objects to be outputted
     */
    public void xyPrintf(int x, int y, String fmt, Object... args) {
        targetPage.xyPrintf(x, y, fmt, args);
        updateScreen();
    }

    /**
     * get the image serving as the current window's buffer
     * @return the buffer image
     */
    public ImageBuffer getTarget() {
        return targetPage;
    }

    /**
     * set the view's origin(0,0) to (x,y)
     * 设置视图原点到(x,y)。以后绘图时，点(x1,y1)对应于实际的点(x+x1,y+y1)
     * @param x origin's x cordinate
     * @param y origin's y cordinate
     */
    public void setOrigin(int x, int y) {
        targetPage.setOrigin(x,y);
    }

    /**
     * reset the view's origin
     */
    public void resetOrigin() {
        targetPage.resetOrigin();
    }

    /**
     * set the image serving as the current window's buffer
     * 设置绘制目标
     *
     * @param imageBuffer the buffer image
     */
    public void setTarget(ImageBuffer imageBuffer) {
        checkNotNull(imageBuffer);
        targetPage = imageBuffer;
    }

    /**
     * reset the default image serving as the current window's buffer
     */
    public void setTarget() {
        targetPage = imagePages[0];
    }

    /**
     * get current renderMode for the window
     * @return the renderMode
     */
    public RenderMode getRenderMode() {
        return renderMode;
    }

    /**
     * set current renderMode
     * @param renderMode
     */
    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    /**
     * set the image serving as the current window's buffer
     * 设置绘制目标
     *
     * @param i the image buffer's index
     */
    public void setTarget(int i) {
        targetPage = imagePages[i];
    }

    /**
     * draw a straight line from point(x1,y1) to (x2,y2)
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
     * draw an arc
     * 绘制圆弧
     *
     * @param x  the x cordinate of the arc's center 圆弧圆心x坐标
     * @param y  the y cordinate of the arc's center 圆弧圆心y坐标
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     * @param radius  radius of the arc
     */
    public void arc(int x, int y, int startAngle, int endAngle, int radius) {
        targetPage.arc(x, y, startAngle, endAngle, radius);
        updateScreen();
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
    public void arc(int x, int y, int startAngle, int endAngle, int xRadius, int yRadius) {
        targetPage.arc(x, y, startAngle, endAngle, xRadius, yRadius);
        updateScreen();
    }


    /**
     * draw a circle
     * 绘制圆形
     *
     * @param x the x cordinate of the circle's center
     * @param y the y cordinate of the circle's center
     * @param radius  radius of the circle
     */
    public void circle(int x, int y, int radius) {
        targetPage.circle(x, y, radius);
        updateScreen();
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
        targetPage.rectangle(left, top, right, bottom);
        updateScreen();
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
    public void roundRect(int left, int top, int right, int bottom, int radius) {
        roundRect(left, top, right, bottom, radius, radius);
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
    public void roundRect(int left, int top, int right, int bottom, int xRadius, int yRadius) {
        targetPage.roundRect(left, top, right, bottom, xRadius, yRadius);
        updateScreen();
    }


    /**
     * draw an ellipse
     * 画椭圆
     *
     * @param x x cordinate of the ellipse's center
     * @param y x cordinate of the elipse's center
     * @param xRadius  radius on the x-axis of the elipse's center
     * @param yRadius  radius on the y-axis of the elipse's center
     */
    public void ellipse(int x, int y, int xRadius, int yRadius) {
        targetPage.ellipse(x, y, xRadius, yRadius);
        updateScreen();
    }

    /**
     * floodfill
     * the area to be fill must be enclosed by the borderColor, or whole window will be filled!
     * @param x x cordinate of the start point
     * @param y y cordinate of the start point
     * @param borderColor border color of the fill area
     */
    public void floodFill(int x, int y, Color borderColor) {
        targetPage.floodFill(x, y, borderColor);
        updateScreen();
    }

    /**
     * get color of the specified point
     * @param x x cordinate of the point
     * @param y y cordinate of the point
     * @return the point's color
     */
    public Color getPixel(int x, int y) {
        return targetPage.getPixel(x, y);
    }

    /**
     * set color of the specified point
     * @param x x cordinate of the point
     * @param y y cordinate of the point
     * @param color color to be set
     */
    public void putPixel(int x, int y, Color color) {
        targetPage.putPixel(x, y, color);
        updateScreen();
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
    public void bar(int left, int top, int right, int bottom) {
        targetPage.bar(left, top, right, bottom);
        updateScreen();
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
    public void fillRectangle(int left, int top, int right, int bottom) {
        targetPage.fillRectangle(left, top, right, bottom);
        updateScreen();
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
    public void fillArc(int x, int y, int startAngle, int endAngle, int radius) {
        targetPage.fillArc(x, y, startAngle, endAngle, radius);
        updateScreen();
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
    public void fillArc(int x, int y, int startAngle, int endAngle, int xRadius, int yRadius) {
        targetPage.fillArc(x, y, startAngle, endAngle, xRadius, yRadius);
        updateScreen();
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
    public void sector(int x, int y, int startAngle, int endAngle, int xRadius, int yRadius) {
        targetPage.sector(x, y, startAngle, endAngle, xRadius, yRadius);
        updateScreen();
    }

    /**
     * draw an filled ellipse sector with border
     * @param left left of the bounded rectangle of the ellipse
     * @param top top of the bounded rectangle of the ellipse
     * @param right right of the bounded rectangle of the ellipse
     * @param bottom bottom of the bounded rectangle of the ellipse
     * @param startAngle start angle of the arc
     * @param endAngle  end angle of the arc
     */
    public void pie(int left, int top, int right, int bottom, int startAngle, int endAngle) {
        targetPage.pie(left, top, right, bottom, startAngle, endAngle);
        updateScreen();
    }

    /**
     * draw a filled circle with bound
     * 绘制带边框填充圆形
     *
     * @param x the x cordinate of the circle's center
     * @param y the y cordinate of the circle's center
     * @param radius  radius of the circle
     */
    public void fillCircle(int x, int y, int radius) {
        targetPage.fillCircle(x, y, radius);
        updateScreen();
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
        targetPage.fillEllipse(x, y, xRadius, yRadius);
        updateScreen();
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
        targetPage.fillPoly(xPoints, yPoints, numPoints);
        updateScreen();
    }

    /**
     * draw a filled polygon
     * 画带边框填充多边形
     *
     * @param numPoints  num of vertexes 多边形点的个数
     * @param vertexes cordinates of each vertexes 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void fillPoly(int numPoints, int[] vertexes) {
        targetPage.fillPoly(numPoints, vertexes);
        updateScreen();
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
    public void fillRoundRect(int left, int top, int right, int bottom, int radius) {
        targetPage.fillRoundRect(left, top, right, bottom, radius);
        updateScreen();
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
    public void fillRoundRect(int left, int top, int right, int bottom, int xRadius, int yRadius) {
        targetPage.fillRoundRect(left, top, right, bottom, xRadius, yRadius);
        updateScreen();
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
        targetPage.drawPoly(xPoints, yPoints, numPoints);
        updateScreen();
    }

    /**
     * draw a polygon
     * 画多边形
     *
     * @param numPoints  num of vertexes 多边形点的个数
     * @param vertexes cordinates of each vertexes 每个点的坐标（依次两个分别为x,y），数组元素个数为 numPoints * 2。
     */
    public void drawPoly(int numPoints, int[] vertexes) {
        targetPage.drawPoly(numPoints, vertexes);
        updateScreen();
    }

    /**
     * clear the window(screen)
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

    private void redrawScreen() {
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
     * pause the main program until key pressed
     * 暂停主程序运行,直到按下任意键
     */
    public void pause() {
        getCh();
    }

    /**
     * test if the program is running  (always true)
     * @return
     */
    public boolean isRun() {
        return true;
    }

    /**
     * Test if a key has been pressed
     * 这个函数用于判断某按键是否被按下。
     *
     * @param keyCode the code of the key {@link java.awt.event.KeyEvent}
     * @return true if the key has been pressed, false if not
     */
    public boolean keyState(int keyCode) {
        checkKeyEventTime();
        return (keyCode == this.keyCode);
    }

    /**
     * init the easy graphics
     * 初始化图形系统
     *
     * @param width  width of the window 屏幕宽度
     * @param height height of the window 屏幕高度
     * @return the JEasyGraphics instance JEasyGraphics对象实例
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

    /**
     * set title of the window
     * @param title  title text
     */
    public void setTitle(String title) {
        mainScreen.setTitle(title);
    }

    /**
     * get main frame of the window
     * @return  the hmain frame object
     */
    public JFrame getMainScreen() {
        return mainScreen;
    }

    /**
     *  set the fill color
     * @param fillColor
     */
    public void setFillColor(Color fillColor) {
        targetPage.setFillColor(fillColor);
    }

    /**
     * set the foreground color
     * @param color
     */
    public void setColor(Color color) {
        targetPage.setColor(color);
    }

    /**
     * get the foreground color
     * @return
     */
    public Color getColor() {
        return targetPage.getColor();
    }

    /**
     * get fill color
     * @return
     */
    public Color getFillColor() {
        return targetPage.getColor();
    }

    /**
     * get background color
     * @return
     */
    public Color getBackgroundColor() {
        return targetPage.getColor();
    }

    /**
     * set background color
     * @param backgroundColor
     */
    public void setBackgroundColor(Color backgroundColor) {
        targetPage.setBackgroundColor(backgroundColor);
    }

    /**
     * set the view port
     * @param left left of the view
     * @param top  top of the view                                  v
     * @param right right of the view
     * @param bottom  bottom of the view
     * @param clip  if the content out of the view will be clipped
     */
    public void setViewPort(int left, int top, int right,
                            int bottom, boolean clip) {
        targetPage.setViewPort(left, top, right, bottom, clip);
    }

    /**
     * get view port info
     * @return
     */
    public ViewPortInfo getViewPort() {
        return targetPage.getViewPort();
    }

    /**
     * clear view port setting
     */
    public void clearViewPort() {
        targetPage.clearViewPort();
    }

    /**
     * set line width
     * @param width
     */
    public void setLineWidth(float width) {
        targetPage.setLineWidth(width);
    }

    /**
     * get line width
     * @return
     */
    public float getLineWidth() {
        return targetPage.getLineWidth();
    }

    /**
     * set line style
     * @param lineStyle
     */
    public void setLineStyle(LineStyle lineStyle) {
        targetPage.setLineStyle(lineStyle);
    }

    /**
     * get line style
     * @return
     */
    public LineStyle getLineStyle() {
        return targetPage.getLineStyle();
    }


    private Semaphore keyCodeSemaphore = new Semaphore(0);
    private Semaphore keyMsgSemaphore = new Semaphore(0);
    private Semaphore mouseMsgSemaphore = new Semaphore(0);

    /**
     * check if key have been pressed
     * 这个函数用于检测当前是否有键盘消息
     *
     * @return
     */
    public boolean kbMsg() {
        checkKeyEventTime();
        return keyMsgSemaphore.availablePermits() > 0;
    }

    /**
     * get and wait for key press message
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

    /**
     * show an input dialog
     * @param notice the notice message
     * @return the text user inputted
     */
    public String inputbox(String notice) {
        return JOptionPane.showInputDialog(mainScreen, notice);
    }

    /**
     * show a confirm dialog
     * @param notice the notice message
     * @return the user's choise
     */
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
     * get and wait for a char inputted from keyboard
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
     * pause for specified time
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
     * pause for 1000/fps milliseconds
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
        if (renderMode == RENDER_MANUAL) {
            redrawScreen();
        }
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
     * test if some key pressed
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
     * close the main window
     * 关闭主窗口
     */
    public void close() {
        mainScreen.dispose();
    }

    private Point lastMousePos=new Point(0,0);
    /**
     * get current mouse position
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
     * wait and get a mouse message
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
     * test if there are mouse events
     * 这个函数用于检测当前是否有鼠标消息
     *
     * @return
     */
    public boolean mouseMsg() {
        checkMouseEventTime();
        return mouseMsgSemaphore.drainPermits() > 0;
    }

    /**
     * convert view cordinate to window cordinate
     * @param p
     * @return
     */
    public Point convertCordinate(Point p) {
        return new Point(p.x + targetPage.getViewPort().left,
                p.y + targetPage.getViewPort().top);
    }

    /**
     * get width of the window
     * @return
     */
    public int getWidth() {
        return targetPage.getWidth();
    }

    /**
     * get height of the window
     * @return
     */
    public int getHeight() {
        return targetPage.getHeight();
    }

    /**
     * set fill pattern
     * @see FillPattern
     * @param fillPattern
     */
    public void setFillPattern(FillPattern fillPattern) {
        targetPage.setFillPattern(fillPattern);
    }

    /**
     * set alpha composite
     * @see java.awt.AlphaComposite
     * @param composite
     */
    public void setComposite(AlphaComposite composite) {
        targetPage.setComposite(composite);
    }

    /**
     * reset alpha composite
     */
    public void setComposite() {
        targetPage.setComposite();
    }

    /**
     * set alpha composite
     * @see java.awt.AlphaComposite
     * @param composite
     */
    public void setWriteMode(AlphaComposite composite) {
        targetPage.setWriteMode(composite);
    }

    private class MainFrame extends JFrame {

        public MainFrame(int width, int height) {
            super("JEasyGraphics");
            init(width, height);
        }

        private void init(final int width, final int height) {
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
            if (targetPage!=null) {
                MouseMsg mouseMsg = new MouseMsg(targetPage.getViewPort().left,
                        targetPage.getViewPort().top,
                        e);
            }
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
