package vn.com.vng.elementview.element;

/**
 * Created by HungNQ on 05/09/2017.
 */

public class Gravity {
    public static final int GRAVITY_NONE = 0;
    public static final int LEFT = 0x00000001;
    public static final int RIGHT = 0x00000002;
    public static final int HORIZONTAL_CENTER = 0x00000003; // = LEFT | RIGHT

    public static final int TOP = 0x00000100;
    public static final int BOTTOM = 0x00000200;
    public static final int VERTICAL_CENTER = 0x00000300; // = TOP | BOTTOM

    public static final int CENTER = VERTICAL_CENTER | HORIZONTAL_CENTER;

    public static final int HORIZONTAL_MASK = 0x000000ff;
    public static final int VERTICAL_MASK = 0x0000ff00;

    public static boolean isHorizontalCenter(int gravity){
        return (gravity & HORIZONTAL_MASK) == HORIZONTAL_CENTER;
    }
    public static boolean isHorizontalLeft(int gravity){
        return (gravity & HORIZONTAL_MASK) == LEFT;
    }
    public static boolean isHorizontalRight(int gravity){
        return (gravity & HORIZONTAL_MASK) == RIGHT;
    }
    public static boolean isVerticalCenter(int gravity){
        return (gravity & VERTICAL_MASK) == VERTICAL_CENTER;
    }
    public static boolean isVerticalTop(int gravity){
        return (gravity & VERTICAL_MASK) == TOP;
    }
    public static boolean isVerticalBottom(int gravity){
        return (gravity & VERTICAL_MASK) == BOTTOM;
    }

    public static boolean isHorizontalNone(int gravity) {
        return (gravity & HORIZONTAL_MASK) == GRAVITY_NONE;

    }

    public static boolean isVerticalNone(int gravity){
        return (gravity & VERTICAL_MASK) == GRAVITY_NONE;
    }
    public static boolean isNone(int gravity){
        return isVerticalNone(gravity) && isHorizontalNone(gravity);
    }
}
