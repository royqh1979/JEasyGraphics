package net.royqh.jeasygraphics;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;

public class TestFill {

    public static void main(String[] args){
        JEasyGraphics eg=JEasyGraphics.init(800,600);
        eg.setBackgroundColor(BLUE);
        eg.clear();
        eg.setFillColor(Color.RED);
        eg.setColor(Color.BLACK);
        eg.setBackgroundColor(WHITE);
        eg.circle(40,40,40);

        eg.outTextXY(0,0,"EMPTY_FILL");
        eg.setFillPattern(FillPattern.EMPTY_FILL);
        eg.fillRectangle(10,20,190,190);

        eg.outTextXY(200,0,"SOLID_FILL");
        eg.setFillPattern(FillPattern.SOLID_FILL);
        eg.fillRectangle(210,20,390,190);

        eg.outTextXY(400,0,"SLASH_FILL");
        eg.setFillPattern(FillPattern.SLASH_FILL);
        eg.fillRectangle(410,20,590,190);

        eg.outTextXY(0,200,"BKSLASH_FILL");
        eg.setFillPattern(FillPattern.BKSLASH_FILL);
        eg.fillRectangle(10,220,190,390);

        eg.outTextXY(200,200,"HATCH_FILL");
        eg.setFillPattern(FillPattern.HATCH_FILL);
        eg.fillRectangle(210,220,390,390);

        eg.outTextXY(400,200,"XHATCH_FILL");
        eg.setFillPattern(FillPattern.XHATCH_FILL);
        eg.fillRectangle(410,220,590,390);

        eg.outTextXY(0,400,"HORIZONTAL_FILL");
        eg.setFillPattern(FillPattern.HORIZONTAL_FILL);
        eg.fillRectangle(10,420,190,590);

        eg.outTextXY(200,400,"VERTICAL_FILL");
        eg.setFillPattern(FillPattern.VERTICAL_FILL);
        eg.fillRectangle(210,420,390,590);

        eg.pause();
        eg.close();
    }
}
