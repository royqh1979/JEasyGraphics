package net.royqh.jeasygraphics;

import java.awt.*;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class Doraemon {
    public static void main(String[] args){
        JEasyGraphics eg=JEasyGraphics.init(800,600);
        System.out.println("lala");
        //setorigin(400, 300);
        eg.setViewPort(400,300,400,400,false);
        // 使用白色填充背景
        eg.setBackgroundColor(WHITE);
        eg.clear();

       // 画脸
        eg.setFillColor(new Color(7, 190, 234));						// 头
        eg.setColor(BLACK);
        eg.fillRoundRect(-135, -206, 135, 54, 248, 248);
        eg.roundRect(-135, -206, 135, 54, 248, 248);

        eg.setFillColor(WHITE);								// 脸
        eg.fillEllipse(0, -46, 115, 95);
        eg.ellipse(0,-46,115,95);

        eg.fillRoundRect(-63, -169, 0, -95, 56, 56);			// 右眼
        eg.roundRect(-63, -169, 0, -95, 56, 56);
        eg.fillRoundRect(0, -169,  63, -95, 56, 56);			// 左眼
        eg.roundRect(0, -169,  63, -95, 56, 56);

        eg.setFillColor(BLACK);
        eg.fillCircle(-16, -116, 6);							// 右眼球
        eg.circle(-16, -116, 6);
        eg.fillCircle( 16, -116, 6);							// 左眼球
        eg.circle(-16, -116, 6);

        eg.setFillColor(new Color(201, 62, 0));						// 鼻子
        eg.fillCircle(0, -92, 15);
        eg.circle(0, -92, 15);

        eg.line(0, -77, 0, -4);								// 人中
        eg.arc((-108+108)/2, (-220-4)/2,  180 * 5 / 4, 180 * 7 / 4,(108-(-108))/2,(-4-(-220))/2);	// 嘴

        eg.line(-42, -73, -90, -91);							// 胡子
        eg.line( 42, -73,  90, -91);
        eg.line(-41, -65, -92, -65);
        eg.line( 41, -65,  92, -65);
        eg.line(-42, -57, -90, -39);
        eg.line( 42, -57,  90, -39);


        // 画身体
        eg.line(-81, 32, -138, 72);					// 手臂(上)
        eg.line( 81, 32,  138, 72);
        eg.line(-96, 96, -116, 110);					// 手臂(下)
        eg.line( 96, 96,  116, 110);

        eg.line(-96, 85, -96, 178);					// 腿外侧
        eg.line( 96, 85,  96, 178);
        eg.arc((-15+15)/2, (168+190)/2, 0, 180,(15-(-15))/2,(190-168)/2);				// 腿内侧

        eg.setFillColor(WHITE);						// 手
        eg.fillCircle(-140, 99, 27);
        eg.circle(-140, 99, 27);
        eg.fillCircle( 140, 99, 27);
        eg.circle(140, 99, 27);
        eg.fillRoundRect(-112, 178, -2, 205, 24, 24);	// 脚
        eg.roundRect(-112, 178, -2, 205, 24, 24);
        eg.fillRoundRect( 2, 178,  112, 205, 24, 24);
        eg.roundRect(2, 178,  112, 205, 24, 24);

        eg.setFillColor(new Color(7, 190, 234));				// 身体填充蓝色
        eg.floodFill(0, 100, BLACK);

        eg.setFillColor(WHITE);						// 肚皮
        eg.fillCircle(0, 81, 75);
        eg.circle(0, 81, 75);
        eg.fillRectangle(-60, 4, 60, 24);				// 用白色矩形擦掉多余的肚皮

        eg.pie(-58, 23, 58, 139, 180, 0);				// 口袋


        // 画铃铛
        eg.setFillColor(new Color(169, 38, 0));				// 绳子
        eg.fillRoundRect(-100, 23, 100, 42, 12, 12);

        eg.setFillColor(new Color(245, 237, 38));			// 铃铛外形
        eg.fillCircle(0, 49, 19);

        eg.setFillColor(BLACK);						// 铃铛上的洞
        eg.fillEllipse(0,54, 4, 4);
        eg.setLineWidth(3);
        eg.line(0, 57, 0, 68);

        eg.setLineWidth(1);					// 铃铛上的纹路
        eg.line(-16, 40, 16, 40);
        eg.line(-18, 44, 18, 44);

        eg.pause();
        eg.close();
    }
}
