package net.royqh.jeasygraphics;

import java.awt.*;

import static net.royqh.jeasygraphics.RenderMode.RENDER_MANUAL;

public class Star {

    static void paintstar(JEasyGraphics eg, double x, double y, double r, double a) {
        int[] pt = new int[10];
        for (int n = 0; n < 5; ++n) {
            pt[n * 2] = (int) (-Math.cos(Math.PI * 4 / 5 * n + a) * r + x);
            pt[n * 2 + 1] = (int) (Math.sin(Math.PI * 4 / 5 * n + a) * r + y);
        }
        eg.drawPoly(5,pt);
        eg.fillPoly(5, pt);
    }

    public static void main(String[] args) {
        JEasyGraphics eg = JEasyGraphics.init(640, 480);
        eg.setBackgroundColor(Color.BLACK);
        eg.setColor(Color.WHITE);
        eg.setFillColor(Color.RED);
        eg.setRenderMode(RENDER_MANUAL);
    /* 使用主控台输入/输出 */
        //initConsole();
    /* 将标准输入输出重定向到指定文件 */
        //setStdIO("f:\\aaa.txt","f:\\ttt.txt");
        //printf("hello world!\n");
        //scanf("%d",&n);
        //printf("n=%d\n",n);
        //hideConsole();

        double r = 0;
        for (; ; eg.delayFps(60)) {

            if (eg.kbHit()) {
                break;
            }
            r += 0.02;
            if (r > Math.PI * 2) r -= Math.PI * 2;

            eg.clear();
            paintstar(eg, 300, 200, 100, r);
        }

        eg.close();
    }


}
