package vn.com.vng.elementview.element;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by HungNQ on 31/08/2017.
 */

public class ElementGroup extends Element {

    //Contain child element's attach info
    static class AttachInfo {
        //the width parent assign
        public int mWidthAssign;
        //the height parent assign
        public int mHeightAssign;
    }


    public ElementGroup(Context context) {
        super(context);
    }

    public ElementGroup(Context context, int widthDimension, int heightDimension) {
        super(context, widthDimension, heightDimension);
        init();
    }


    //properties
    protected List<Element> mChildren;
    protected int mGravity;


    private void init() {
        mChildren = new LinkedList<>();
    }

    //getter & setter
    public List<Element> getChildren() {
        return mChildren;
    }

    /**
     *
     * @param element
     * @return index of element
     */
    public int addElement(@NonNull Element element) {
        mChildren.add(element);
        element.setAttachInfo(buildAttachInfo(element));
        return mChildren.size() - 1;
    }

    /**
     *
     * @param element
     * @param widthDimension
     * @param heightDimension
     * @return index of element
     */
    public int addElement(@NonNull Element element, int widthDimension, int heightDimension) {
        mChildren.add(element);
        element.setDimension(widthDimension, heightDimension);
        element.setAttachInfo(buildAttachInfo(element));
        return mChildren.size() - 1;
    }

    //build AttachInfo object for child element
    protected AttachInfo buildAttachInfo(Element element) {
        return new AttachInfo();
    }

    public int getChildCount() {
        return mChildren.size();
    }

    public Element getElementAt(int position) {
        return mChildren.get(position);
    }

    public Element removeElement(int position) {
        return mChildren.remove(position);
    }

    public void removeElement(Element element) {
        mChildren.remove(element);
    }


    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public void setForegroundVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & Gravity.VERTICAL_MASK;
        if ((mGravity & Gravity.VERTICAL_MASK) != gravity) {
            mGravity = (mGravity & ~Gravity.VERTICAL_MASK) | gravity;
        }
    }

    public void setForegroundHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & Gravity.HORIZONTAL_MASK;
        if ((mGravity & Gravity.HORIZONTAL_MASK) != gravity) {
            mGravity = (mGravity & ~Gravity.HORIZONTAL_MASK) | gravity;
        }
    }
    //endregion: getter & setter

    /**
     * Ask one of the children of this view to measure itself, taking into
     * account both the MeasureSpec requirements for this view and its padding
     * and margins. The child must have MarginLayoutParams The heavy lifting is
     * done in makeChildMeasureSpec.
     *
     * @param child                   The child to measure
     * @param parentWidthMeasureSpec  The width requirements for this view
     * @param widthUsed               Extra space that has been used up by the parent
     *                                horizontally (possibly by other children of the parent)
     * @param parentHeightMeasureSpec The height requirements for this view
     * @param heightUsed              Extra space that has been used up by the parent
     *                                vertically (possibly by other children of the parent)
     */
    protected void measureChildWithMargins(Element child,
                                           int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {

        final int childWidthMeasureSpec = makeChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft + mPaddingRight + child.mMarginLeft + child.mMarginRight + widthUsed, child.mWidthDimension);
        final int childHeightMeasureSpec = makeChildMeasureSpec(parentHeightMeasureSpec, mPaddingTop + mPaddingBottom + child.mMarginTop + child.mMarginBottom + heightUsed, child.mHeightDimension);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }


    /**
     * Ask one of the children of this view to measure itself, taking into
     * account both the MeasureSpec requirements for this view and its padding
     * and margins. The child must have MarginLayoutParams The heavy lifting is
     * done in makeChildMeasureSpec.
     *
     * @param child             The child to measure
     * @param widthMeasureSpec  The width requirements for this element
     * @param heightMeasureSpec The height requirements for this element
     */
    protected void measureChild(Element child,
                                int widthMeasureSpec,
                                int heightMeasureSpec) {

        final int childWidthMeasureSpec = makeChildMeasureSpec(widthMeasureSpec, child.mMarginLeft + child.mMarginRight, child.mWidthDimension);
        final int childHeightMeasureSpec = makeChildMeasureSpec(heightMeasureSpec, child.mMarginTop + child.mMarginBottom, child.mHeightDimension);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }


    protected void layoutChildWithMargins(Element child, int leftBound, int topBound, int widthLayout, int heightLayout, int gravity) {

        int widthCanUse = Math.max(widthLayout - child.mMarginLeft - child.mMarginRight, 0);
        int heightCanUse = Math.max(heightLayout - child.mMarginTop - child.mMarginBottom, 0);
        int childLayoutWidth = Math.min(child.getAttachInfo().mWidthAssign, widthCanUse);
        int childLayoutHeight = Math.min(child.getAttachInfo().mHeightAssign, heightCanUse);


        int left = leftBound + getGravityX(widthLayout, child.getAttachInfo().mWidthAssign, child.mMarginLeft, child.mMarginRight, gravity);
        int top = topBound + getGravityY(heightLayout, child.getAttachInfo().mHeightAssign, child.mMarginTop, child.mMarginBottom, gravity);
        int right = left + childLayoutWidth;
        int bottom = top + childLayoutHeight;

        child.layout(left, top, right, bottom);
    }



    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (Element child : mChildren) {
            drawChild(canvas, child);
        }
    }

    protected void drawChild(Canvas canvas, Element child) {
        if (child.mWidth > 0 && child.mHeight > 0) {
            int saveCount = canvas.save();
            if (child.mLeft > 0 || child.mTop > 0)
                canvas.translate(child.mLeft, child.mTop);
            canvas.clipRect(0, 0, child.mWidth, child.mHeight);

            child.draw(canvas);

            canvas.restoreToCount(saveCount);
        }
    }
    //----------static function region------------------------------

    /**
     * Does the hard part of measureChildren: figuring out the MeasureSpec to
     * pass to a particular child. This method figures out the right MeasureSpec
     * for one dimension (height or width) of one child view.
     * <p>
     * The goal is to combine information from our MeasureSpec with the
     * LayoutParams of the child to get the best possible results. For example,
     * if the this view knows its size (because its MeasureSpec has a mode of
     * EXACTLY), and the child has indicated in its LayoutParams that it wants
     * to be the same size as the parent, the parent should ask the child to
     * layout given an exact size.
     *
     * @param spec           The requirements for this view
     * @param padding        The padding of this view for the current dimension and
     *                       margins, if applicable
     * @param childDimension How big the child wants to be in the current
     *                       dimension
     * @return a MeasureSpec integer for the child
     */
    public static int makeChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = getMeasureSpecMode(spec);
        int specSize = getMeasureSpecSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MEASURE_EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MEASURE_EXACTLY;
                } else if (childDimension == MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MEASURE_EXACTLY;
                } else if (childDimension == WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MEASURE_AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MEASURE_AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MEASURE_EXACTLY;
                } else if (childDimension == MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MEASURE_AT_MOST;
                } else if (childDimension == WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MEASURE_AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MEASURE_UNSPECIFIED:
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MEASURE_EXACTLY;
                } else if (childDimension == MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = size;
                    resultMode = MEASURE_UNSPECIFIED;
                } else if (childDimension == WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = size;
                    resultMode = MEASURE_UNSPECIFIED;
                }
                break;
        }
        //noinspection ResourceType
        return makeMeasureSpec(resultSize, resultMode);
    }


    public static int getChildVerticalGravity(Element child, int defaultGravity) {
        int gravity;
        if (child != null && !Gravity.isVerticalNone(child.getForegroundGravity()))
            gravity = child.getForegroundGravity();
        else
            gravity = defaultGravity;
        return gravity & Gravity.VERTICAL_MASK;
    }

    public static int getChildHorizontalGravity(Element child, int defaultGravity) {
        int gravity;
        if (child != null && !Gravity.isHorizontalNone(child.getForegroundGravity()))
            gravity = child.getForegroundGravity();
        else
            gravity = defaultGravity;
        return gravity & Gravity.HORIZONTAL_MASK;
    }

    public static int getChildGravity(Element child, int parentGravity) {
        return getChildVerticalGravity(child, parentGravity) | getChildHorizontalGravity(child, parentGravity);
    }

    public static void saveDimensionAttachInfo(@NonNull Element element, int width, int height) {
        if (element.getAttachInfo() != null) {
            AttachInfo attachInfo = element.getAttachInfo();
            attachInfo.mWidthAssign = width;
            attachInfo.mHeightAssign = height;
        }
    }
    //---------------------endregion----------------------------------


}
