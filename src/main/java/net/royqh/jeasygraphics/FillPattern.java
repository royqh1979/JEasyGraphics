package net.royqh.jeasygraphics;

import net.royqh.jeasygraphics.fillpattern.*;

import java.awt.*;

public enum FillPattern {
    EMPTY_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return backColor;
        }
    },
    SOLID_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return fillColor;
        }
    },
    VERTICAL_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new VerticalFillPaint(color,backColor);
        }
    },
    SLASH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new SlashFillPaint(color,backColor);
        }
    },
    BKSLASH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new BackSlashFillPaint(color,backColor);
        }
    },
    HATCH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new HatchFillPaint(color,backColor);
        }
    },
    XHATCH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new XHatchFillPaint(color,backColor);
        }
    },
    HORIZONTAL_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new HorizontalFillPaint(color,backColor);
        }
    };
    public abstract Paint createPattern(Color color, Color fillColor, Color backColor) ;
}
