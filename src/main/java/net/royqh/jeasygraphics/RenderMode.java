package net.royqh.jeasygraphics;

/**
 * render mode
 * 图像绘制方式
 */
public enum RenderMode {
    /**
     * render immediatelly
     * 绘制立即生效
     */
    RENDER_AUTO,
    /**
     * only render when delay()/getCh()/kbhit() etc, used for animation
     * 只有当调用delay()/getCh()/kbhit()等函数时,才进行绘制,用于动画处理
     *
     */
    RENDER_MANUAL
}
