package vn.com.vng.elementview.element;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HungNQ on 30/08/2017.
 */

public class Element {

    //static values region
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    public static final int MEASURE_EXACTLY = View.MeasureSpec.EXACTLY;
    public static final int MEASURE_AT_MOST = View.MeasureSpec.AT_MOST;
    public static final int MEASURE_UNSPECIFIED = View.MeasureSpec.UNSPECIFIED;
//    private final Paint mPaint;

    //endregion


    //stuff region
    protected Context mContext;
    protected int mLeft, mTop, mRight, mBottom;
    protected int mWidth, mHeight;
    protected ElementGroup.AttachInfo mAttachInfo;
    //endregion

    //properties region
    protected int mWidthDimension, mHeightDimension;
    protected int mMeasureWidth, mMeasureHeight;

    protected int mMarginLeft, mMarginTop, mMarginRight, mMarginBottom;
    protected int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    protected Bitmap mBackgroundBitmap;
    protected Drawable mBackgroundDrawable;
    protected int mBackgroundColor;

    protected int mForegroundGravity;


    //endregion

    public Element(Context context) {
        this(context, WRAP_CONTENT, WRAP_CONTENT);
    }

    public Element(Context context, int widthDimension, int heightDimension) {
        mContext = context;
        mWidthDimension = widthDimension;
        mHeightDimension = heightDimension;

//        //Todo: debug
//        mPaint = new Paint();
//        mPaint.setStyle(Paint.Style.STROKE);
    }


    public Context getContext() {
        return mContext;
    }


    //close region
    protected ElementGroup.AttachInfo getAttachInfo() {
        return mAttachInfo;
    }

    protected void setAttachInfo(ElementGroup.AttachInfo attachInfo) {
        mAttachInfo = attachInfo;
    }


    //-------------------properties region-----------------------------


    public void setDimension(int widthDimension, int heightDimension) {
        mWidthDimension = widthDimension;
        mHeightDimension = heightDimension;
    }

    public int getWidthDimension() {
        return  mWidthDimension;
    }

    public int getHeightDimension() {
        return mHeightDimension;
    }

    protected void setMeasuredDimension(int widthMeasure, int heightMeasure) {
        mMeasureWidth = widthMeasure;
        mMeasureHeight = heightMeasure;
    }

    public int getMeasureWidth() {
        return mMeasureWidth;
    }

    public int getMeasureHeight() {
        return mMeasureHeight;
    }


    public int getForegroundGravity() {
        return mForegroundGravity;
    }

    public void setForegroundGravity(int foregroundGravity) {
        mForegroundGravity = foregroundGravity;
    }

    public void setForegroundVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & Gravity.VERTICAL_MASK;
        if ((mForegroundGravity & Gravity.VERTICAL_MASK) != gravity) {
            mForegroundGravity = (mForegroundGravity & ~Gravity.VERTICAL_MASK) | gravity;
        }
    }

    public void setForegroundHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & Gravity.HORIZONTAL_MASK;
        if ((mForegroundGravity & Gravity.HORIZONTAL_MASK) != gravity) {
            mForegroundGravity = (mForegroundGravity & ~Gravity.HORIZONTAL_MASK) | gravity;
        }
    }

    public void setMarginLeft(int marginLeft) {
        mMarginLeft = marginLeft;
    }

    public void setMarginTop(int marginTop) {
        mMarginTop = marginTop;
    }

    public void setMarginRight(int marginRight) {
        mMarginRight = marginRight;
    }

    public void setMarginBottom(int marginBottom) {
        mMarginBottom = marginBottom;
    }

    public void setMargin(int margin) {
        mMarginLeft = mMarginTop = mMarginRight = mMarginBottom = margin;
    }

    public void setPaddingLeft(int paddingLeft) {
        mPaddingLeft = paddingLeft;
    }

    public void setPaddingTop(int paddingTop) {
        mPaddingTop = paddingTop;
    }

    public void setPaddingRight(int paddingRight) {
        mPaddingRight = paddingRight;
    }

    public void setPaddingBottom(int paddingBottom) {
        mPaddingBottom = paddingBottom;
    }

    public void setPadding(int padding) {
        mPaddingLeft = mPaddingTop = mPaddingRight = mPaddingBottom = padding;
    }

    public Bitmap getBackgroundBitmap() {
        return mBackgroundBitmap;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        clearBackground();
        mBackgroundBitmap = backgroundBitmap;
    }

    public Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        clearBackground();
        mBackgroundDrawable = backgroundDrawable;
        if (mBackgroundDrawable != null)
            mBackgroundDrawable.setBounds(0, 0, mWidth, mHeight);
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        clearBackground();
        mBackgroundColor = backgroundColor;
    }

    public void clearBackground() {
        mBackgroundColor = 0;
        mBackgroundBitmap = null;
        mBackgroundDrawable = null;
    }

    //----------------------endregion----------------------------------------------


    void measure(int widthMeasureSpec, int heightMeasureSpec) {
        onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveDimensionSpec(mWidthDimension, widthMeasureSpec), resolveDimensionSpec(mHeightDimension, heightMeasureSpec));
    }


    void layout(int left, int top, int right, int bottom) {
        boolean changed = checkLayoutChanged(mLeft, mTop, mRight, mBottom, left, top, right, bottom);
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
        mWidth = mRight - mLeft;
        mHeight = mBottom - mTop;
        onLayout(changed, mLeft, mTop, mRight, mBottom);
        onPostLayout(changed, left, top, right, bottom);
    }

    protected void onPostLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    private boolean checkLayoutChanged(int oldLeft, int oldTop, int oldRight, int oldBottom, int newLeft, int newTop, int newRight, int newBottom) {
        return ((oldRight - oldLeft) != (newRight - newLeft)) || ((oldTop - oldBottom) != (newTop - newBottom));
    }


    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            if (mBackgroundDrawable != null)
                mBackgroundDrawable.setBounds(0, 0, mWidth, mHeight);
        }
    }

    void draw(Canvas canvas) {
        onDraw(canvas);
        dispatchDraw(canvas);
    }

    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);

//        //debug
//        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);

    }

    protected void dispatchDraw(Canvas canvas) {

    }

    protected void drawBackground(Canvas canvas) {
        if (canvas != null) {
            if (mBackgroundBitmap != null)
                drawBitmap(canvas, mBackgroundBitmap);
            else if (mBackgroundDrawable != null)
                drawDrawable(canvas, mBackgroundDrawable);
            else if (mBackgroundColor != 0)
                drawColor(canvas, mBackgroundColor);
        }
    }

    private void drawColor(Canvas canvas, int backgroundColor) {
        canvas.drawColor(backgroundColor);
    }

    private void drawDrawable(Canvas canvas, Drawable d) {
        d.draw(canvas);
    }

    private void drawBitmap(Canvas canvas, Bitmap b) {
        canvas.drawBitmap(b, 0f, 0f, null);
    }



    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }



    //-----------------------static functions region-----------------------------------------

    public static int makeMeasureSpec(int specSize, int specMode) {
        return View.MeasureSpec.makeMeasureSpec(specSize, specMode);
    }

    public static int getMeasureSpecSize(int measureSpec) {
        return View.MeasureSpec.getSize(measureSpec);
    }

    public static int getMeasureSpecMode(int measureMode) {
        return View.MeasureSpec.getMode(measureMode);
    }

    /**
     * Does the hard part of measure: figuring out the MeasureSpec to
     * pass to {setMeasuredDimension}. This method figures out the right MeasureSpec
     * for one dimension (height or width) of one element.
     * <p>
     * The goal is to combine information from the MeasureSpec with the
     * dimension of the child to get the best possible results.
     *
     * @param measureSpec The requirements for this view
     * @param dimension   How big the child wants to be in the current
     *                    dimension
     * @return a MeasureSpec integer for the child
     */
    public static int resolveDimensionSpec(int dimension, int measureSpec) {
        int specMode = getMeasureSpecMode(measureSpec);
        int specSize = getMeasureSpecSize(measureSpec);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MEASURE_EXACTLY: {
                resultSize = specSize;
                resultMode = MEASURE_EXACTLY;
                break;
            }
            // Parent has imposed a maximum size on us
            case MEASURE_AT_MOST:
                if (dimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = dimension > specSize ? specSize : dimension;
                    resultMode = MEASURE_EXACTLY;
                } else if (dimension == MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = specSize;
                    resultMode = MEASURE_AT_MOST;
                } else if (dimension == WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = specSize;
                    resultMode = MEASURE_AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MEASURE_UNSPECIFIED:
                if (dimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = dimension;
                    resultMode = MEASURE_EXACTLY;
                } else if (dimension == MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = specSize;
                    resultMode = MEASURE_UNSPECIFIED;
                } else if (dimension == WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = specSize;
                    resultMode = MEASURE_UNSPECIFIED;
                }
                break;
        }
        //noinspection ResourceType
        return makeMeasureSpec(resultSize, resultMode);
    }


    /**
     * Reconcile a desired size for the view contents with a {@link View.MeasureSpec}
     * constraint passed by the parent.
     * <p>
     * This is a simplified version of {@link View#resolveSize(int, int)}
     *
     * @param desireSize  Size of the view's contents.
     * @param measureSpec A {@link View.MeasureSpec} passed by the parent.
     * @return A size that best fits {@code contentSize} while respecting the parent's constraints.
     */
    public static int reconcileSize(int desireSize, int measureSpec) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == MEASURE_EXACTLY) {
            return specSize;
        } else if (specMode == MEASURE_AT_MOST) {
            return desireSize > specSize ? specSize : desireSize;
        } else {
            return desireSize;
        }
    }


    public static int getGravityX(int layoutWidth, int contentWidth, int paddingLeft, int paddingRight, int gravity) {
        int width = Math.max(layoutWidth - paddingLeft - paddingRight, 0);

        if(width == 0)
            return Math.min(layoutWidth, paddingLeft);
        if (width <= contentWidth)
            return paddingLeft;

        if (Gravity.isHorizontalLeft(gravity))
            return paddingLeft;
        if (Gravity.isHorizontalRight(gravity))
            return (width - contentWidth) + paddingLeft;
        if (Gravity.isHorizontalCenter(gravity))
            return ((width - contentWidth) / 2) + paddingLeft;

        return paddingLeft;
    }

    public static int getGravityY(int layoutHeight, int contentHeight, int paddingTop, int paddingBottom, int gravity) {
        int height = Math.max(layoutHeight - paddingTop - paddingBottom, 0);

        if(height == 0)
            return Math.max(contentHeight, paddingTop);
        if (height <= contentHeight)
            return paddingTop;

        if (Gravity.isVerticalTop(gravity))
            return paddingTop;
        if (Gravity.isVerticalBottom(gravity))
            return height - contentHeight + paddingTop;
        if (Gravity.isVerticalCenter(gravity))
            return (height - contentHeight) / 2 + paddingTop;

        return 0;
    }

    //----------------------endregion------------------------------------------------------

}
