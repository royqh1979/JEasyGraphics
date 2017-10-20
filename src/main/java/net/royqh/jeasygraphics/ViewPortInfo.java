package net.royqh.jeasygraphics;

public class ViewPortInfo {
    public int left;
    public int top;
    public int right;
    public int bottom;
    public boolean clipOn;

    public ViewPortInfo(int left, int top, int right, int bottom, boolean clipOn) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.clipOn = clipOn;
    }
}
