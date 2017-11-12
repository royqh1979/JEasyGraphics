package net.royqh.jeasygraphics;

import net.royqh.jeasygraphics.fillpattern.*;

import java.awt.*;

/**
 * pattern of fills
 */
public enum FillPattern {
    /**
     * fill with background color
     */
    EMPTY_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return backColor;
        }
    },
    /**
     * fill with fill color
     */
    SOLID_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return fillColor;
        }
    },
    /**
     * fill with vertical line  |||
     */
    VERTICAL_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new VerticalFillPaint(color,backColor);
        }
    },
    /**
     * fill with slash line   ///
     */
    SLASH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new SlashFillPaint(color,backColor);
        }
    },
    /**
     * fill with backslash line \\\
     */
    BKSLASH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new BackSlashFillPaint(color,backColor);
        }
    },
    /**
     * fill with cross line +++
     */
    HATCH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new HatchFillPaint(color,backColor);
        }
    },
    /**
     * fill with x-cross line xxx
     */
    XHATCH_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new XHatchFillPaint(color,backColor);
        }
    },
    /**
     * fill with horizontal line ---
     */
    HORIZONTAL_FILL {
        @Override
        public Paint createPattern(Color color, Color fillColor, Color backColor) {
            return new HorizontalFillPaint(color,backColor);
        }
    };
    public abstract Paint createPattern(Color color, Color fillColor, Color backColor) ;
}
