package net.royqh.jeasygraphics;

import java.awt.*;
import java.awt.geom.Point2D;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

public class MouseDemo {
    public static void main(String[] args){
        JEasyGraphics eg=JEasyGraphics.init(800,600);
        //eg.setFont(18, "宋体");

        int k = 0;

        // 这个循环，is_run判断窗口是否还在，delay_fps是延时
        // 后面讲动画的时候会详细讲解，现不要在此纠结
        for ( ; eg.isRun(); eg.delayFps(60))
        {
            //获取鼠标坐标，此函数不等待。若鼠标移出了窗口，那么坐标值不会更新
            //特殊情况是，你按着鼠标键不放，拖出窗口，这样坐标值会依然更新
            Point p=eg.mousePos();

            eg.xyPrintf(0,0,"%04d %04d",p.x,p.y);
            //格式化输出为字符串，用于后面输出
        }

        eg.close();
    }
}
