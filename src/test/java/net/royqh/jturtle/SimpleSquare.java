package net.royqh.jturtle;

public class SimpleSquare {
    public static void main(String[] args){
        Turtle t=Turtle.createWorld(800,600);
        t.setPenSize(1);
        t.setSpeed(100);

        t.fd(100);
        t.rt(90);
        t.fd(100);
        t.rt(90);
        t.fd(100);
        t.rt(90);
        t.fd(100);
        t.rt(90);

        t.pause();

        t.close();
    }
}
