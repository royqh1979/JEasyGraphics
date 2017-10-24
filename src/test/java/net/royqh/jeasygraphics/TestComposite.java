package net.royqh.jeasygraphics;

import java.awt.*;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class TestComposite {

    public static void main(String[] args){
        JEasyGraphics eg=JEasyGraphics.init(800,600);

        eg.clear();
        eg.setFillColor(Color.RED);
        eg.setColor(BLACK);
        eg.setBackgroundColor(WHITE);

        eg.outTextXY(0,0,"Clear");
        drawSrc(eg,10,20);
        eg.setComposite(AlphaComposite.Clear);
        drawDst(eg,40,50);
        eg.setComposite();

        eg.outTextXY(160,0,"Dst");
        drawSrc(eg,160,20);
        eg.setComposite(AlphaComposite.Dst);
        drawDst(eg,190,50);
        eg.setComposite();

        eg.outTextXY(310,0,"DstAtop");
        drawSrc(eg,310,20);
        eg.setComposite(AlphaComposite.DstAtop);
        drawDst(eg,340,50);
        eg.setComposite();

        eg.outTextXY(460,0,"DstIn");
        drawSrc(eg,460,20);
        eg.setComposite(AlphaComposite.DstIn);
        drawDst(eg,490,50);
        eg.setComposite();

        eg.outTextXY(610,0,"DstOut");
        drawSrc(eg,610,20);
        eg.setComposite(AlphaComposite.DstOut);
        drawDst(eg,640,50);
        eg.setComposite();

        eg.outTextXY(0,150,"DstOver");
        drawSrc(eg,10,170);
        eg.setComposite(AlphaComposite.DstOver);
        drawDst(eg,40,200);
        eg.setComposite();

        eg.outTextXY(160,150,"Src");
        drawSrc(eg,160,170);
        eg.setComposite(AlphaComposite.Src);
        drawDst(eg,190,200);
        eg.setComposite();

        eg.outTextXY(310,150,"SrcAtop");
        drawSrc(eg,310,170);
        eg.setComposite(AlphaComposite.SrcAtop);
        drawDst(eg,340,200);
        eg.setComposite();

        eg.outTextXY(460,150,"SrcIn");
        drawSrc(eg,460,170);
        eg.setComposite(AlphaComposite.SrcIn);
        drawDst(eg,490,200);
        eg.setComposite();

        eg.outTextXY(610,150,"SrcOut");
        drawSrc(eg,610,170);
        eg.setComposite(AlphaComposite.SrcOut);
        drawDst(eg,640,200);
        eg.setComposite();

        eg.outTextXY(0,300,"SrcOver");
        drawSrc(eg,10,320);
        eg.setComposite(AlphaComposite.SrcOver);
        drawDst(eg,40,350);
        eg.setComposite();

        eg.outTextXY(160,300,"Xor");
        drawSrc(eg,160,320);
        eg.setComposite(AlphaComposite.Xor);
        drawDst(eg,190,350);
        eg.setComposite();

        /*
        eg.outTextXY(310,0,"DstAtop");
        drawSrc(eg,310,20);
        eg.setComposite(AlphaComposite.DstAtop);
        drawDst(eg,340,50);
        eg.setComposite();

        eg.outTextXY(460,0,"DstIn");
        drawSrc(eg,460,20);
        eg.setComposite(AlphaComposite.DstIn);
        drawDst(eg,490,50);
        eg.setComposite();

        eg.outTextXY(610,0,"DstOut");
        drawSrc(eg,610,20);
        eg.setComposite(AlphaComposite.DstOut);
        drawDst(eg,640,50);
        eg.setComposite();

        eg.outTextXY(0,150,"DstOver");
        drawSrc(eg,10,170);
        eg.setComposite(AlphaComposite.DstOver);
        drawDst(eg,40,200);
        eg.setComposite();
        */
        eg.pause();
        eg.close();
    }

    private static void drawDst(JEasyGraphics eg, int x, int y) {

        eg.setFillColor(WHITE);
        eg.fillRectangle(x,y,x+60,y+60);
        eg.setFillColor(BLACK);
        eg.fillRectangle(x+10,y+10,x+50,y+50);
    }

    private static void drawSrc(JEasyGraphics eg,int x, int y) {
        eg.setFillColor(WHITE);
        eg.fillRectangle(x,y,x+60,y+60);
        eg.setFillColor(BLACK);
        eg.fillCircle(x+30,y+30,30);
    }
}
