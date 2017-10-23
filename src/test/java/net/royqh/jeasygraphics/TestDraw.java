package net.royqh.jeasygraphics;

public class TestDraw {
    public static void main(String[] args){
        JEasyGraphics eg=JEasyGraphics.init(800,600);
        eg.circle(40,40,40);

        eg.line(100,100,200,200);
        eg.pause();
        eg.close();
    }
}
