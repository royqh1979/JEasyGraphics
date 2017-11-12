# JEasyGraphics
An easy-to-use Java graphics library whose api is like Borland Turbo C++ Graphics.h lib, and turtle graphics.

It's good for introduction classes on Java or Computer Graphics.

## Examples

### Basic Drawing
<pre>
<code>
public static void main(String[] args){
    JEasyGraphics eg=JEasyGraphics.init(800,600);
    eg.circle(40,40,40);
    eg.line(100,100,200,200);
    eg.pause();
    eg.close();
}
</code>
</pre>

### Turtle Graphics

<pre>
<code>
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
</code>
</pre>

