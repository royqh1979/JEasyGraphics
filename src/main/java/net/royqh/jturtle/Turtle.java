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

    public static Turtle createWorld(int width, int height, double scale){
        JEasyGraphics eg=JEasyGraphics.init((int)(width*scale)+1,(int)(height*scale)+1);
        return new Turtle(eg,width,height,scale);
    }

    public static Turtle createWorld(int width,int height) {
        return createWorld(width,height,1);
    }

    /**
     * 将角度转换为弧度
     * @param degree
     * @return
     */
    private double d2a(double degree) {
        return degree*Math.PI/180.0;
    }

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

            System.out.println("("+myTurtle.x+","+myTurtle.y+")");
        }
        eg.putImage(screenBufferPage,0,0,eg.getWidth(),eg.getHeight(),0,0,width,height);
    }

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

    public void fd(double step)
    {
        forward(step);
    }

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
    public void bk(double step)
    {
        backward(step);
    }
    public void backward(double step)
    {
        forward(-step);
    }
    public void lt(double degree)
    {
        leftTurn(degree);
    }
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
    public void rt(double degree)
    {
        rightTurn(degree);
    }
    public void rightTurn(double degree)
    {
        lt(-degree);
    }
    public void pd()
    {
        penDown();
    }
    public void penDown()
    {
        myTurtle.penDown=true;
    }
    public void penUp()
    {
        myTurtle.penDown=false;
    }
    public void pu()
    {
        penUp();
    }
    public void clearScreen()
    {
        worldPage.clear();

        myTurtle.x=originX;
        myTurtle.y=originY;
        myTurtle.orient=-90;
        //prepareTurtleIcon();
        refreshWorld();
    }

    public void cs()
    {
        clearScreen();
    }
    public void clear()
    {
        clearScreen();
    }

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
    public void setPenSize(int size)
    {
        myTurtle.penSize=size;
        worldPage.setLineWidth(size);
    };
    public void setPenColor(Color color)
    {
        myTurtle.penColor=color;
        worldPage.setColor(color);
    }
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
    public void setRewind(boolean isRewind)
    {
        rewind=isRewind;
    }
    public void setImmediate(boolean isImmediate)
    {
        immediate=isImmediate;
    }
    public void pause()
    {
        frameCount=0;
        //prepareTurtleIcon();
        displayWorld();
        eg.pause();
    }
    public void show()
    {
        myTurtle.showTurtle=true;
        refreshWorld();
    }
    public void hide()
    {
        myTurtle.showTurtle=false;
        refreshWorld();
    }

    public void setXY(double x, double y)
    {
        myTurtle.x=originX+x;
        myTurtle.y=originY-y;
        refreshWorld();
    }

    public double getX()
    {
        int cent_x;
        cent_x=round(width/2);
        return myTurtle.x-cent_x;
    }
    public double getY()
    {
        int cent_y;
        cent_y=round(height/2);
        return cent_y-myTurtle.y;
    }
    public void setAngle(double angle)
    {
        myTurtle.orient=-angle;
        //prepareTurtleIcon();
        refreshWorld();
    }
    public double getAngle()
    {
        return -myTurtle.orient;
    }

    public TurtleInfo getState()
    {
        return new TurtleInfo(getX(),getY(),getAngle());
    }

    public void setState(TurtleInfo state)
    {
        boolean pd=myTurtle.penDown;
        myTurtle.penDown=false;
        setXY(state.getX(),state.getY());
        setAngle(state.getAngle());
        myTurtle.penDown=pd;
    }

    public void faceXY(double x,double y)
    {
        x=originX+x;
        y=originY-y;
        double delta_x=x-myTurtle.x;
        double delta_y=-(y-myTurtle.y);
        double angle=Math.atan2(delta_y,delta_x)/Math.PI*180;
        turnTo(angle);
    }

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


    public void setOrigin(int x, int y)
    {
        originX=originX+x;
        originY=originY-y;
        home();
        clearScreen();
    }


    public void close() {
        eg.close();
    }

    public JEasyGraphics getEasyGraphics() {
        return eg;
    }

    public double getScale() {
        return scale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageBuffer getWorldPage() {
        return worldPage;
    }

    public ImageBuffer getIconPage() {
        return iconPage;
    }

    public boolean isRewind() {
        return rewind;
    }

    public boolean isImmediate() {
        return immediate;
    }
}
