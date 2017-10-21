package net.royqh.jeasygraphics;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static java.awt.event.MouseEvent.*;

public class MouseMsg {
    private int originX;
    private int originY;
    private MouseEvent e;

    public MouseMsg(int originX, int originY, MouseEvent e) {
        this.originX = originX;
        this.originY = originY;
        this.e = e;
    }

    public int getX() {
        return e.getXOnScreen()+originX;
    }
    public int getY() {
        return e.getYOnScreen()+originY;
    }
    public int getWheel() {
        if (e instanceof MouseWheelEvent)  {
            return ((MouseWheelEvent) e).getWheelRotation();
        } else {
            return 0;
        }
    }
    public boolean isLeft() {
        return e.getButton() == BUTTON1;
    }
    public boolean isRight() {
        return e.getButton()  == BUTTON2;
    }
    public boolean isMid() {
        return e.getButton() == BUTTON3;
    }
    public boolean isDown() {
        return e.getID()==MOUSE_PRESSED;
    }
    public boolean isUp() {
        return e.getID() == MOUSE_RELEASED;
    }
    public boolean isMove() {
        return e.getID() == MOUSE_MOVED;
    }

    public boolean isWheel() {
        return e.getID() == MOUSE_WHEEL;
    }
}
