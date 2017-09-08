package vn.com.vng.elementview.element;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by HungNQ on 31/08/2017.
 */

public class LinearElementGroup extends ElementGroup {

    static class LinearAttachInfo extends AttachInfo {
        public int mWeight;
    }

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;

    //stuff
    private int mContentWidth;
    private int mContentHeight;

    //properties
    private int mOrientation;


    public LinearElementGroup(Context context) {
        this(context, WRAP_CONTENT, WRAP_CONTENT);
    }

    public LinearElementGroup(Context context, int widthDimension, int heightDimension) {
        super(context, widthDimension, heightDimension);
        init();
    }

    private void init() {
        mOrientation = ORIENTATION_VERTICAL;
    }


    //----------properties--------

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public int addElement(@NonNull Element element, int weigh) {
        int result = super.addElement(element);
        if (weigh >= 0 && element.getAttachInfo() instanceof LinearAttachInfo)
            ((LinearAttachInfo) element.getAttachInfo()).mWeight = weigh;
        return result;
    }

    public int addElement(@NonNull Element element, int widthDimension, int heightDimension, int weigh) {
        int result = super.addElement(element, widthDimension, heightDimension);
        if (weigh >= 0 && element.getAttachInfo() instanceof LinearAttachInfo)
            ((LinearAttachInfo) element.getAttachInfo()).mWeight = weigh;
        return result;

    }
    //endregion

    @Override
    protected AttachInfo buildAttachInfo(Element element) {
        return new LinearAttachInfo();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mOrientation == ORIENTATION_VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }


    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int layoutWidthSpec = resolveDimensionSpec(mWidthDimension, widthMeasureSpec);
        int layoutHeightSpec = resolveDimensionSpec(mHeightDimension, heightMeasureSpec);

        int heightUsed = 0;
        int maxWidth = 0;

        int weightSum = 0;
        //measure all items but weight items
        for (Element child : mChildren) {
            int weight = getWeight(child);
            if (weight <= 0) {
                measureChildWithMargins(child, layoutWidthSpec, 0, layoutHeightSpec, heightUsed);
                int w = getMeasureSpecSize(child.getMeasureWidth());
                int h = getMeasureSpecSize(child.getMeasureHeight());
                saveDimensionAttachInfo(child, w, h);

                int widthWithMargins = w + child.mMarginLeft + child.mMarginRight;
                int heightWithMargins = h + child.mMarginTop + child.mMarginBottom;

                heightUsed += heightWithMargins;
                if (widthWithMargins > maxWidth)
                    maxWidth = widthWithMargins;
            } else
                weightSum += weight;
        }

        //measure  weight items
        if (weightSum > 0) {
            int heightRemain = getMeasureSpecSize(layoutHeightSpec) - mPaddingTop - mPaddingBottom - heightUsed;
            if (heightRemain > 0) {
                for (Element child : mChildren) {
                    int weight = getWeight(child);
                    if (weight > 0) {
                        int heightWeight = (int) (heightRemain / (float) weightSum * weight);
                        int heightAssign = Math.max(heightWeight - child.mMarginTop - child.mMarginBottom, 0);
                        int heightMeasure = makeMeasureSpec(heightAssign, MEASURE_EXACTLY);
                        int widthMeasure = makeChildMeasureSpec(layoutWidthSpec, mPaddingLeft + mPaddingRight + child.mMarginLeft + child.mMarginRight, child.mWidthDimension);

                        child.measure(widthMeasure, heightMeasure);

                        int w = getMeasureSpecSize(child.getMeasureWidth());
                        saveDimensionAttachInfo(child, w, heightAssign);

                        int widthWithMargins = w + child.mMarginLeft + child.mMarginRight;

                        heightUsed += heightWeight;
                        if (widthWithMargins > maxWidth)
                            maxWidth = widthWithMargins;
                    }
                }
            }
        }


        int desireWidth = maxWidth + mPaddingLeft + mPaddingRight;
        int desireHeight = heightUsed + mPaddingTop + mPaddingBottom;
        int widthMeasure = reconcileSize(desireWidth, layoutWidthSpec);
        int heightMeasure = reconcileSize(desireHeight, layoutHeightSpec);

        setMeasuredDimension(makeMeasureSpec(widthMeasure, MEASURE_EXACTLY), makeMeasureSpec(heightMeasure, MEASURE_EXACTLY));

        mContentWidth = desireWidth;
        mContentHeight = desireHeight;
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int layoutWidthSpec = resolveDimensionSpec(mWidthDimension, widthMeasureSpec);
        int layoutHeightSpec = resolveDimensionSpec(mHeightDimension, heightMeasureSpec);

        int widthUsed = 0;
        int maxHeight = 0;

        int weightSum = 0;
        //measure all items but weight item
        for (Element child : mChildren) {
            int weight = getWeight(child);
            if (weight <= 0) {
                measureChildWithMargins(child, layoutWidthSpec, widthUsed, layoutHeightSpec, 0);
                int w = getMeasureSpecSize(child.getMeasureWidth());
                int h = getMeasureSpecSize(child.getMeasureHeight());
                saveDimensionAttachInfo(child, w, h);

                int widthWithMargins = w + child.mMarginLeft + child.mMarginRight;
                int heightWithMargins = h + child.mMarginTop + child.mMarginBottom;

                widthUsed += widthWithMargins;
                if (heightWithMargins > maxHeight)
                    maxHeight = heightWithMargins;
            } else
                weightSum += weight;
        }

        //measure  weight item
        if (weightSum > 0) {
            int widthRemain = getMeasureSpecSize(layoutWidthSpec) - mPaddingLeft - mPaddingRight - widthUsed;
            if (widthRemain > 0) {
                for (Element child : mChildren) {
                    int weight = getWeight(child);
                    if (weight > 0) {
                        int widthWeight = (int) (widthRemain / (float) weightSum * weight);
                        int widthAssign = Math.max(widthWeight - child.mMarginLeft - child.mMarginRight, 0);
                        int widthMeasure = makeMeasureSpec(widthAssign, MEASURE_EXACTLY);
                        int heightMeasure = makeChildMeasureSpec(layoutHeightSpec, mPaddingTop + mPaddingBottom + child.mMarginLeft + child.mMarginRight, child.mHeightDimension);

                        child.measure(widthMeasure, heightMeasure);

                        int h = getMeasureSpecSize(child.getMeasureHeight());
                        saveDimensionAttachInfo(child, widthAssign, h);

                        int heightWithMargins = h + child.mMarginTop + child.mMarginBottom;

                        widthUsed += widthWeight;
                        if (heightWithMargins > maxHeight)
                            maxHeight = heightWithMargins;
                    }
                }
            }
        }

        int desireWidth = widthUsed + mPaddingLeft + mPaddingRight;
        int desireHeight = maxHeight + mPaddingTop + mPaddingBottom;
        int widthMeasure = reconcileSize(desireWidth, layoutWidthSpec);
        int heightMeasure = reconcileSize(desireHeight, layoutHeightSpec);

        setMeasuredDimension(makeMeasureSpec(widthMeasure, MEASURE_EXACTLY), makeMeasureSpec(heightMeasure, MEASURE_EXACTLY));


        mContentWidth = desireWidth;
        mContentHeight = desireHeight;
    }


    public int getWeight(Element child) {
        return (child != null && child.getAttachInfo() instanceof LinearAttachInfo) ? ((LinearAttachInfo) child.getAttachInfo()).mWeight : 0;
    }

    public void setChildWeight(Element child, int weight) {
        if (child != null && child.getAttachInfo() instanceof LinearAttachInfo)
            ((LinearAttachInfo) child.getAttachInfo()).mWeight = weight >= 0 ? weight : 0;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        int widthRemain = mWidth - mPaddingLeft - mPaddingRight;
        int heightRemain = mHeight - mPaddingTop - mPaddingBottom;

        //figure out start point for content layout
        int xPointer; //left beginning
        int yPointer;  //top beginning

        /*
         * if this layout in vertical orientation, so just take care vertical gravity now.
         * Otherwise, this layout in horizontal orientation, so just take care horizontal gravity.
         * The other direction gravity will be handle when layout child elements.
         */
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            xPointer = getGravityX(mWidth, mContentWidth, mPaddingLeft, mPaddingRight, mGravity);
            yPointer = mPaddingTop;
        } else {
            xPointer = mPaddingLeft;
            yPointer = getGravityY(mHeight, mContentHeight, mPaddingTop, mPaddingBottom, mGravity);
        }

        //assign for children
        if (mChildren != null && mChildren.size() > 0)
            if (mOrientation == ORIENTATION_HORIZONTAL) {
                for (Element child : mChildren) {
                    //just get vertical gravity, because horizontal gravity was handled before.
                    layoutChildWithMargins(child, xPointer, yPointer, widthRemain, heightRemain, getChildVerticalGravity(child, mGravity));

                    //move layout pointer
                    int widthUsed = child.mWidth + child.mMarginLeft + child.mMarginRight;
                    xPointer += widthUsed;
                    widthRemain -= widthUsed;
                }
            } else {
                for (Element child : mChildren) {
                    //just get horizontal gravity, because vertical gravity was handled before.
                    layoutChildWithMargins(child, xPointer, yPointer, widthRemain, heightRemain, getChildHorizontalGravity(child, mGravity));

                    //move layout pointer
                    int heightUsed = child.mHeight + child.mMarginTop + child.mMarginBottom;
                    yPointer += heightUsed;
                    heightRemain -= heightUsed;
                }
            }
    }

    private int assignSize(int childDesireSize, int size, boolean hardSize) {
        if (size <= 0)
            return 0;
        if (hardSize)
            return size;
        return childDesireSize < size ? childDesireSize : size;

    }

}
