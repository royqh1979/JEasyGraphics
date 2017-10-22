package net.royqh.jturtle;

public class TestTurtle {
    public static void main(String[] args) {
        Turtle t=Turtle.createWorld(80,60,10);
        t.setSpeed(1000);

        for(int i=0;i<4;i++) {
            t.fd(10);
            t.lt(90);
        }
        t.pause();
        t.hide();
        t.pause();
        t.close();
    }
}
