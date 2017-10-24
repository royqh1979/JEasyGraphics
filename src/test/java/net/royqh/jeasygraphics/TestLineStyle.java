package net.royqh.jeasygraphics;

import java.awt.*;

import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;

public class TestLineStyle {

    public static void main(String[] args){
        JEasyGraphics eg=JEasyGraphics.init(800,600);
        eg.setFillColor(Color.YELLOW);
        eg.setColor(Color.BLACK);
        eg.setBackgroundColor(WHITE);
        eg.setLineWidth(5);

        eg.outTextXY(0,0,"SOLID_LINE");
        eg.setLineStyle(LineStyle.SOLID_LINE);
        eg.fillRectangle(10,20,190,190);

        eg.outTextXY(200,0,"CENTER_LINE");
        eg.setLineStyle(LineStyle.CENTER_LINE);
        eg.fillRectangle(210,20,390,190);

        eg.outTextXY(400,0,"DOTTED_LINE");
        eg.setLineStyle(LineStyle.DOTTED_LINE);
        eg.fillRectangle(410,20,590,190);


        eg.outTextXY(0,200,"DASHED_LINE");
        eg.setLineStyle(LineStyle.DASHED_LINE);
        eg.fillRectangle(10,220,190,390);

        eg.outTextXY(200,200,"NULL_LINE");
        eg.setLineStyle(LineStyle.NULL_LINE);
        eg.fillRectangle(210,220,390,390);

        /*
        eg.outTextXY(400,200,"XHATCH_FILL");
        eg.setFillPattern(FillPattern.XHATCH_FILL);
        eg.fillRectangle(410,220,590,390);

        eg.outTextXY(0,400,"HORIZONTAL_FILL");
        eg.setFillPattern(FillPattern.HORIZONTAL_FILL);
        eg.fillRectangle(10,420,190,590);

        eg.outTextXY(200,400,"VERTICAL_FILL");
        eg.setFillPattern(FillPattern.VERTICAL_FILL);
        eg.fillRectangle(210,420,390,590);
        */
        eg.pause();
        eg.close();
    }
}
