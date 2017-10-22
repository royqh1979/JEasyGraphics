package net.royqh.jeasygraphics;

import java.awt.*;
import java.io.IOException;

public class TestImages {
    public static void main(String[] args) {
        JEasyGraphics eg=JEasyGraphics.init(800,600);

        testGetImageFromFile(eg);
        eg.pause();

        testPutImage(eg);
        eg.pause();

        testPutImageTransparent(eg);
        eg.pause();

        testGetImage(eg);
        eg.pause();

        eg.close();
    }

    private static void testGetImageFromFile(JEasyGraphics eg) {
        eg.clear();
        try {
            ImageBuffer imageBuffer=eg.createImage(TestImages.class.getResource("/Serendipity.jpg").getFile());

            eg.putImage(imageBuffer,100,100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testGetImage(JEasyGraphics eg) {
        eg.clear();
        eg.setFillColor(Color.RED);
        eg.fillEllipse(400,300,200,150);
        eg.ellipse(400,300,200,150);

        ImageBuffer imageBuffer=eg.getImage(200,150,400,300);

        eg.putImageTransparent(imageBuffer,200,200,400,300,0,0,Color.WHITE);
        imageBuffer.dispose();
    }

    private static void testPutImage(JEasyGraphics eg) {
        eg.clear();
        eg.setFillColor(Color.RED);
        eg.fillEllipse(400,300,200,150);
        eg.ellipse(400,300,200,150);

        ImageBuffer imageBuffer=eg.createImage(400,400);
        imageBuffer.setFillColor(Color.BLUE);
        imageBuffer.bar(100,100,300,300);

        eg.putImage(imageBuffer,200,200);
        imageBuffer.dispose();

    }

    private static void testPutImageTransparent(JEasyGraphics eg) {
        eg.clear();
        eg.setFillColor(Color.RED);
        eg.fillEllipse(400,300,200,150);
        eg.ellipse(400,300,200,150);

        ImageBuffer imageBuffer=eg.createImage(400,400);
        imageBuffer.setFillColor(Color.BLUE);
        imageBuffer.bar(100,100,300,300);

        eg.putImageTransparent(imageBuffer,200,200,400,400,0,0,Color.WHITE);

        imageBuffer.dispose();
    }
}
