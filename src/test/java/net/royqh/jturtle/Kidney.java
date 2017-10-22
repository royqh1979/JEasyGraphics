package net.royqh.jturtle;

/*思路
  定位圆: 绘制的各圆圆心均位于该定位圆上
    圆心为x,y,半径为R
  以该定位圆上各点为圆心, 点到过定位圆圆心的垂直直线的距离为半径作圆
  得到的就是该图案
*/
public class Kidney {
    public static void main(String[] args) {
        double a;
        double x,y,r,s;
        double R;
        Turtle t=Turtle.createWorld(800,600);
        t.setSpeed(10000);
        //setPenColor(RED);
        R=100;
        //绘制定位圆
        for (a=0;a<=2*Math.PI;a+=Math.PI/30) {
            x=400+R*Math.cos(a);
            y=R*Math.sin(a);
            r=Math.abs(400-x);
            s=2*Math.PI*r/360;
            t.pu();
            t.fd(y);
            t.pd();
            for (int i=1;i<=360;i++) {
                t.fd(s);
                if (400>x){
                    t.lt(1);
                } else {
                    t.rt(1);
                }
            }
            t.pu();
            t.bk(y);
            t.pd();
        }
        t.pause();
        t.close();
    }
}
