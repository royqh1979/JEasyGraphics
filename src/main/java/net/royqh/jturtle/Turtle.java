package net.royqh.jturtle;

import net.royqh.jeasygraphics.ImageBuffer;
import net.royqh.jeasygraphics.JEasyGraphics;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.lang.Math.round;
import static net.royqh.jeasygraphics.RenderMode.RENDER_MANUAL;

public class Turtle {
    public static final int BASE_STEP=1;

    private JEasyGraphics eg;
    /* properties for world */
    private double scale;
    private int width;
    private int height;
    private ImageBuffer worldPage;
    private ImageBuffer screenBufferPage;
    private ImageBuffer iconPage;
    private Color backColor= WHITE;
    private boolean rewind=false;
    private boolean immediate=false;
    private int originX;
    private int originY;
    private int frameCount;
    private int iconWidth;
    private int iconHeight;

    private TurtleState myTurtle=new TurtleState();

    /* properties for turtle */

    

    private Turtle(JEasyGraphics easyGraphics, int width, int height, double scale) {
        this.eg=easyGraphics;
        this.width=width;
        this.height=height;
        this.scale=scale;

        backColor=WHITE;
        rewind=true;
        immediate=false;
        frameCount=0;
        originX=width/2;
        originY=height/2;

        myTurtle.x=originX;
        myTurtle.y=originY;
        myTurtle.orient=-90;
        myTurtle.penDown=true;
        myTurtle.penColor=BLACK;
        myTurtle.penSize=1;
        myTurtle.penSpeed=100;
        myTurtle.showTurtle=true;

        screenBufferPage=eg.createImage(width,height);
        worldPage=eg.createImage(width,height);
        worldPage.setColor(myTurtle.penColor);
        worldPage.setBackgroundColor(backColor);
        worldPage.setFillColor(myTurtle.penColor);
        worldPage.setLineWidth(myTurtle.penSize);
        worldPage.clear();
        try {
            prepareTurtleOriginIcon();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        eg.setRenderMode(RENDER_MANUAL);
        //prepareTurtleIcon();

        refreshWorld();
    }

    /**
     * init the turtle graphics
     * 初始化海龟世界
     * @param width width of the window 窗口宽度
     * @param height height of the window 窗口高度
     * @param scale zoom factor 缩放比例
     * @return the turtle instance
     */
    public static Turtle createWorld(int width, int height, double scale){
        JEasyGraphics eg=JEasyGraphics.init((int)(width*scale)+1,(int)(height*scale)+1);
        return new Turtle(eg,width,height,scale);
    }

    /**
     * init the turtle graphics
     * 初始化海龟世界
     * @param width width of the window 窗口宽度
     * @param height height of the window 窗口高度
     * @return
     */
    public static Turtle createWorld(int width,int height) {
        return createWorld(width,height,1);
    }

    /**
     * convert degree to radian
     * 将角度转换为弧度
     * @param degree
     * @return
     */
    private double d2a(double degree) {
        return degree*Math.PI/180.0;
    }

    /**
     *  really draw the world
     */
    private void displayWorld(){
        screenBufferPage.putImage(worldPage,0,0);
        if (myTurtle.showTurtle) {
            screenBufferPage.putImageRotate(iconPage,
                    (int)myTurtle.x,
                    (int)myTurtle.y,
                    0,0,
                    iconWidth,iconHeight,
                    iconWidth/2,iconHeight/2,
                    myTurtle.orient/180*Math.PI);

            //System.out.println("("+myTurtle.x+","+myTurtle.y+")");
        }
        eg.putImage(screenBufferPage,0,0,eg.getWidth(),eg.getHeight(),0,0,width,height);
    }

    /**
     * refresh world
     */
    private void refreshWorld()
    {
        frameCount++;
        frameCount%=myTurtle.penSpeed;
        if (myTurtle.penSpeed>100)
        {
            int s=myTurtle.penSpeed/100;
            if (frameCount%s!=1)
            {
                return;
            }
            eg.delay(10);
        }
        else
        {
           eg.delayFps(myTurtle.penSpeed);
        }
        displayWorld();
    }

    private void prepareTurtleOriginIcon() throws IOException {
        iconPage=eg.createImage(this.getClass().getResource("/turtle.png").getFile());
        iconPage.makeTransparent(Color.WHITE);
        iconWidth=iconPage.getWidth();
        iconHeight=iconPage.getHeight();
    }

    /**
     * go forward
     * 海龟前进
     * @param step
     */
    public void fd(double step)
    {
        forward(step);
    }

    /**
     * go forward
     * 海龟前进
     *
     * @param step
     */
    public void forward(double step)
    {
        double deltaX,deltaY;
        double x,y,oldX,oldY;
        deltaX=BASE_STEP*Math.cos(d2a(myTurtle.orient));
        deltaY=BASE_STEP*Math.sin(d2a(myTurtle.orient));
        if (step<0)
        {
            deltaX=-deltaX;
            deltaY=-deltaY;
            step=-step;
        }
        oldX=x=myTurtle.x;
        oldY=y=myTurtle.y;

        for (int i=0; i<step; i++)
        {
            if ((i+1)<step)
            {
                x+=deltaX;
                y+=deltaY;
            }
            else
            {
                x+=(step-i)*deltaX;
                y+=(step-i)*deltaY;
            }

            if (rewind)
            {
                if (x<0)
                {
                    x+=width;
                    oldX+=width;
                }
                else if (x>width)
                {
                    x-=width;
                    oldX-=width;
                }
                if (y<0)
                {
                    y+=height;
                    oldY+=height;
                }
                else if (y>height)
                {
                    y-=height;
                    oldY-=height;
                }
            }
            if (myTurtle.penDown)
            {
                worldPage.line((int)oldX, (int)oldY,(int)x,(int)y);
            }
            oldX=x;
            oldY=y;
            myTurtle.x=x;
            myTurtle.y=y;
            refreshWorld();

        }

    }

    /**
     * go backward
     * 海龟后退
     * @param step
     */
    public void bk(double step)
    {
        backward(step);
    }

    /**
     * go backward
     * 海龟后退
     * @param step
     */
    public void backward(double step)
    {
        forward(-step);
    }

    /**
     * turn left
     * 海龟左转
     * @param degree
     */
    public void lt(double degree)
    {
        leftTurn(degree);
    }
    /**
     * turn left
     * 海龟左转
     * @param degree
     */
    public void turnLeft(double degree) {
        leftTurn(degree);
    }

    /**
     * turn left
     * 海龟左转
     * @param degree
     */
    public void leftTurn(double degree)
    {
        double originAngle=myTurtle.orient;
        if (!immediate)
        {
            if (degree>0)
            {
                for (int i=0; i<degree; i+=2)
                {
                    myTurtle.orient=originAngle-i;
                    //prepareTurtleIcon();
                    refreshWorld();
                }
            }
            else
            {
                for (int i=0; i<-degree; i+=2)
                {
                    myTurtle.orient=originAngle+i;
                    //prepareTurtleIcon();
                    refreshWorld();
                }
            }
        }
        degree=degree-(int)degree/360*360;
        myTurtle.orient=originAngle-degree;
        if (myTurtle.orient>360)
        {
            myTurtle.orient-=360;
        }
        if (myTurtle.orient<0)
        {
            myTurtle.orient+=360;
        }
        //prepareTurtleIcon();
        refreshWorld();
    }

    /**
     * turn right
     * 海龟右转
     * @param degree
     */
    public void rt(double degree)
    {
        rightTurn(degree);
    }
    /**
     * turn right
     * 海龟右转
     * @param degree
     */
    public void turnRight(double degree)
    {
        rightTurn(degree);
    }
    /**
     * turn right
     * 海龟右转
     * @param degree
     */
    public void rightTurn(double degree)
    {
        lt(-degree);
    }

    /**
     * pen down
     * 落笔
     */
    public void pd()
    {
        penDown();
    }

    /**
     * pen down
     * 落笔
     */
    public void penDown()
    {
        myTurtle.penDown=true;
    }

    /**
     * pen up
     * 抬笔
     * 
     */
    public void penUp()
    {
        myTurtle.penDown=false;
    }
    /**
     * pen up
     * 抬笔
     *
     */
    public void pu()
    {
        penUp();
    }

    /**
     * clear screen
     * 清屏
     *
     */
    public void clearScreen()
    {
        worldPage.clear();

        myTurtle.x=originX;
        myTurtle.y=originY;
        myTurtle.orient=-90;
        //prepareTurtleIcon();
        refreshWorld();
    }
    /**
     * clear screen
     * 清屏
     *
     */
    public void cs()
    {
        clearScreen();
    }
    /**
     * clear screen
     * 清屏
     *
     */
    public void clear()
    {
        clearScreen();
    }

    /**
     * go home (return to (0,0))
     * 海龟回到原点(0,0)
     */
    public void home()
    {
        int toX,toY;

        toX=originX;
        toY=originY;
        if (myTurtle.penDown)
        {
            worldPage.line((int)myTurtle.x,(int)myTurtle.y,toX,toY);
        }
        myTurtle.x=toX;
        myTurtle.y=toY;
        myTurtle.orient=-90;
        //prepareTurtleIcon();
        refreshWorld();
    }

    /**
     * set pen size
     * 设置画笔宽度
     *
     * @param size
     */
    public void setPenSize(int size)
    {
        myTurtle.penSize=size;
        worldPage.setLineWidth(size);
    };

    /**
     * set pen color
     * 设置画笔颜色
     *
     * @param color
     */
    public void setPenColor(Color color)
    {
        myTurtle.penColor=color;
        worldPage.setColor(color);
    }

    /**
     * set turtle speed
     * 设置海龟移动速度
     *
     * @param speed
     */
    public void setSpeed(int speed)
    {
        if (speed>=1)
        {
            myTurtle.penSpeed=speed;
        }
        else
        {
            myTurtle.penSpeed=1;
        }
    }

    /**
     * set if turtle will rewind if out of screen
     * 设置当海龟移动到屏幕外时,是否自动回绕到屏幕另一侧
     * @param isRewind
     */
    public void setRewind(boolean isRewind)
    {
        rewind=isRewind;
    }

    /**
     * set if turtle move immediatelly (no animation)
     * 设置海龟是否瞬移(取消动画)
     * @param isImmediate
     */
    public void setImmediate(boolean isImmediate)
    {
        immediate=isImmediate;
    }

    /**
     * pause the program
     * 暂停程序
     */
    public void pause()
    {
        frameCount=0;
        //prepareTurtleIcon();
        displayWorld();
        eg.pause();
    }

    /**
     * show the turtle
     * 显示海龟
     */
    public void show()
    {
        myTurtle.showTurtle=true;
        refreshWorld();
    }

    /**
     * hide the turtle
     * 隐藏海龟
     */
    public void hide()
    {
        myTurtle.showTurtle=false;
        refreshWorld();
    }

    /**
     * put turtle to point(x,y)
     * 将海龟放到点(x,y)
     *
     * @param x
     * @param y
     */
    public void setXY(double x, double y)
    {
        myTurtle.x=originX+x;
        myTurtle.y=originY-y;
        refreshWorld();
    }

    /**
     * get x position of turtle
     * 获取海龟x坐标
     * @return
     */
    public double getX()
    {
        int cent_x;
        cent_x=round(width/2);
        return myTurtle.x-cent_x;
    }

    /**
     * get y position of turtle
     * 获取海龟y坐标
     * @return
     */
    public double getY()
    {
        int cent_y;
        cent_y=round(height/2);
        return cent_y-myTurtle.y;
    }

    /**
     * set orientation of the turtle
     * 设置海龟朝向
     * @param angle
     */
    public void setAngle(double angle)
    {
        myTurtle.orient=-angle;
        //prepareTurtleIcon();
        refreshWorld();
    }

    /**
     * get the orientation of the turtle
     * 获取海龟朝向
     * 
     * @return
     */
    public double getAngle()
    {
        return -myTurtle.orient;
    }

    /**
     * get status info of the turtle                                          h
     * 获取海龟当前状态(位置和朝向)
     * @see TurtleInfo
     * @return
     */
    public TurtleInfo getState()
    {
        return new TurtleInfo(getX(),getY(),getAngle());
    }

    /**
     * set status of the turtle
     * 设置海龟当前状态
     *
     * @param state
     */
    public void setState(TurtleInfo state)
    {
        boolean pd=myTurtle.penDown;
        myTurtle.penDown=false;
        setXY(state.getX(),state.getY());
        setAngle(state.getAngle());
        myTurtle.penDown=pd;
    }

    /**
     * let turtle face to point(x,y)
     * 让海龟朝向点(x,y)
     * @param x
     * @param y
     */
    public void faceXY(double x,double y)
    {
        x=originX+x;
        y=originY-y;
        double delta_x=x-myTurtle.x;
        double delta_y=-(y-myTurtle.y);
        double angle=Math.atan2(delta_y,delta_x)/Math.PI*180;
        turnTo(angle);
    }

    /**
     * change turtle's orientation
     * 设置海龟的朝向
     * @param angle
     */
    public void turnTo(double angle)
    {
        double turnAngle;
        turnAngle=-angle - myTurtle.orient ;
        while (turnAngle>180)
        {
            turnAngle=turnAngle-360;
        }
        while (turnAngle<-180)
        {
            turnAngle=360+turnAngle;
        }
        rt(turnAngle);
    }

    /**
     * move turtle to point(x,y)
     * 让海龟移动到点(x,y)
     * @param x
     * @param y
     */
    public void gotoXY(double x, double y)
    {
        faceXY(x,y);

        x=originX+x;
        y=originY-y;
        double delta_x=x-myTurtle.x;
        double delta_y=-(y-myTurtle.y);
        double step=Math.sqrt(delta_x*delta_x+delta_y*delta_y);
        fd(step);
        myTurtle.x=x;
        myTurtle.y=y;
        //prepareTurtleIcon();
        refreshWorld();
    }

    /**
     * set origin to point(x,y)
     * 将视图原点设置到点(x,y)
     *
     * @param x
     * @param y
     */
    public void setOrigin(int x, int y)
    {
        originX=originX+x;
        originY=originY-y;
        home();
        clearScreen();
    }

    /**
     * close the window
     * 关闭窗口
     * 
     */
    public void close() {
        eg.close();
    }

    /**
     * get the underlying easyGraphics
     * @return
     */
    public JEasyGraphics getEasyGraphics() {
        return eg;
    }

    /**
     * get the zoom scale
     * 获取缩放比例
     *
     * @return
     */
    public double getScale() {
        return scale;
    }

    /**
     * get width of the window
     * 获取视窗宽度
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * get height of the window
     * 获取视窗高度
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * get the background image(drawing image)
     * 获取背景图像
     *
     * @return
     */
    public ImageBuffer getWorldPage() {
        return worldPage;
    }

    /**
     * get the turtle icon image
     * 获取海龟图标
     *
     * @return
     */
    public ImageBuffer getIconPage() {
        return iconPage;
    }

    /**
     * get if the turtle will rewind when out of screen
     * 获取海龟是否回绕
     *
     * @return
     */
    public boolean isRewind() {
        return rewind;
    }

    /**
     * get if the turtle will act immediatelly (no animation)
     * 获取海龟是否瞬移(无动画)
     * @return
     */
    public boolean isImmediate() {
        return immediate;
    }
}
