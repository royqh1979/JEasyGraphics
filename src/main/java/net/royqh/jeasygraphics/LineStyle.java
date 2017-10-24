package net.royqh.jeasygraphics;

import net.royqh.jeasygraphics.stroke.*;

import java.awt.*;

public enum LineStyle {
    SOLID_LINE{
        @Override
        public Stroke createStroke(float width) {
            return new SolidStroke(width);
        }
    },
    CENTER_LINE {
        @Override
        public Stroke createStroke(float width) {
            return new CenterStroke(width);
        }
    },
    DOTTED_LINE {
        @Override
        public Stroke createStroke(float width) {
            return new DottedStroke(width);
        }
    },
    DASHED_LINE {
        @Override
        public Stroke createStroke(float width) {
            return new DashedStroke(width);
        }
    },
    NULL_LINE {
        @Override
        public Stroke createStroke(float width) {
            return NullStroke.getInstance();
        }
    }
    /*USERBIT_LINE*/ ;
    public abstract Stroke createStroke(float width);
}
