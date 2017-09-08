package vn.com.vng.elementview.element;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by HungNQ on 05/09/2017.
 */

public class ImageElement extends Element {

    /**
     * Options for scaling the bounds of an image to the bounds of this view.
     */
    public enum ScaleType {
        FIT_XY,
        FIT_CENTER,
        CENTER,
        CENTER_CROP,
        CENTER_INSIDE
    }

    public static final int ROUND_CIRCLE = -1;
    public static final int ROUND_NONE = 0;

    public ImageElement(Context context) {
        this(context, WRAP_CONTENT, WRAP_CONTENT);
    }

    public ImageElement(Context context, int widthDimension, int heightDimension) {
        super(context, widthDimension, heightDimension);
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        mScaleType = ScaleType.FIT_CENTER;

    }


    //stuff
    private Drawable mDrawable;
    private Matrix mDrawMatrix, mMatrix;
    private int mDrawableWidth, mDrawableHeight;
    private Bitmap mCachedBitmap;

    // Avoid allocations...
    private final RectF mTempSrc = new RectF();
    private final RectF mTempDst = new RectF();
    private final Path mClipPath = new Path();
    private final Paint mAntiAliasPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    //properties
    private ScaleType mScaleType;
    private float mRoundCorner;
    private boolean mShouldCacheBitmap;

    //-------------getter & setter----------------------



    public ScaleType getScaleType() {
        return mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        if(mScaleType != scaleType) {
            mScaleType = scaleType;
            configureImageBounds();
        }
    }

    public float getRoundCorner() {
        return mRoundCorner;
    }

    public void setRoundCorner(float roundCorner) {
        if(mRoundCorner != roundCorner){
            mRoundCorner = roundCorner;
            configureClipBounds();
        }
    }
    public void setBitmap(Bitmap bitmap) {
        setImageDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
    }

    public void setImageDrawable(Drawable drawable) {
        mDrawable = drawable;
        if (drawable != null) {
            mDrawableWidth = mDrawable.getIntrinsicWidth();
            mDrawableHeight = mDrawable.getIntrinsicHeight();
            configureImageBounds();
        } else {
            mDrawableWidth = -1;
            mDrawableHeight = -1;
        }


    }

    public boolean isShouldCacheBitmap() {
        return mShouldCacheBitmap;
    }

    //-------------endregion------------------


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int w;
//        int h;
//
//        if (mDrawable == null) {
//            // If no drawable, its intrinsic size is 0.
//            mDrawableWidth = -1;
//            mDrawableHeight = -1;
//            w = h = 0;
//        } else {
//            w = mDrawableWidth;
//            h = mDrawableHeight;
//            if (w <= 0) w = 1;
//            if (h <= 0) h = 1;
//        }
//
//        final int pleft = mPaddingLeft;
//        final int pright = mPaddingRight;
//        final int ptop = mPaddingTop;
//        final int pbottom = mPaddingBottom;
//
//        int widthSize;
//        int heightSize;
//
//
//            /* We are either don't want to preserve the drawables aspect ratio,
//               or we are not allowed to change view dimensions. Just measure in
//               the normal way.
//            */
//        w += pleft + pright;
//        h += ptop + pbottom;
//
////            w = Math.max(w, getSuggestedMinimumWidth());
////            h = Math.max(h, getSuggestedMinimumHeight());
//
//        widthSize = resolveDimensionSpec(w, widthMeasureSpec);
//        heightSize = resolveDimensionSpec(h, heightMeasureSpec);
//
//        setMeasuredDimension(widthSize, heightSize);
//    }


    @Override
    protected void onPostLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onPostLayout(changed, left, top, right, bottom);
        if (changed) {
            configureImageBounds();
            configureClipBounds();
        }
    }

    //copy a part of ImageView.configureBounds(()
    private void configureImageBounds() {
        if (mDrawable == null) {
            return;
        }

        final int dwidth = mDrawableWidth;
        final int dheight = mDrawableHeight;

        final int vwidth = mWidth - mPaddingLeft - mPaddingRight;
        final int vheight = mHeight - mPaddingTop - mPaddingBottom;

        final boolean fits = (dwidth < 0 || vwidth == dwidth)
                && (dheight < 0 || vheight == dheight);

        if (dwidth <= 0 || dheight <= 0 || ScaleType.FIT_XY == mScaleType) {
            /* If the drawable has no intrinsic size, or we're told to
                scaletofit, then we just fill our entire view.
            */
            mDrawable.setBounds(0, 0, vwidth, vheight);
            mDrawMatrix = null;
        } else {
            // We need to do the scaling ourself, so have the drawable
            // use its native size.
            mDrawable.setBounds(0, 0, dwidth, dheight);

            if (fits) {
                // The bitmap fits exactly, no transform needed.
                mDrawMatrix = null;
            } else if (mScaleType == ScaleType.CENTER) {
                // Center bitmap in view, no scaling.
                mDrawMatrix = mMatrix;
                mDrawMatrix.setTranslate(Math.round((vwidth - dwidth) * 0.5f),
                        Math.round((vheight - dheight) * 0.5f));
            } else if (mScaleType == ScaleType.CENTER_CROP) {
                mDrawMatrix = mMatrix;

                float scale;
                float dx = 0, dy = 0;

                if (dwidth * vheight > vwidth * dheight) {
                    scale = (float) vheight / (float) dheight;
                    dx = (vwidth - dwidth * scale) * 0.5f;
                } else {
                    scale = (float) vwidth / (float) dwidth;
                    dy = (vheight - dheight * scale) * 0.5f;
                }

                mDrawMatrix.setScale(scale, scale);
                mDrawMatrix.postTranslate(Math.round(dx), Math.round(dy));
            } else if (mScaleType == ScaleType.CENTER_INSIDE) {
                mDrawMatrix = mMatrix;
                float scale;
                float dx;
                float dy;

                if (dwidth <= vwidth && dheight <= vheight) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) vwidth / (float) dwidth,
                            (float) vheight / (float) dheight);
                }

                dx = Math.round((vwidth - dwidth * scale) * 0.5f);
                dy = Math.round((vheight - dheight * scale) * 0.5f);

                mDrawMatrix.setScale(scale, scale);
                mDrawMatrix.postTranslate(dx, dy);
            } else { //FIT_CENTER
                // Generate the required transform.
                mTempSrc.set(0, 0, dwidth, dheight);
                mTempDst.set(0, 0, vwidth, vheight);

                mDrawMatrix = mMatrix;
                mDrawMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER);
            }
        }
    }


    private void configureClipBounds() {
        if (mDrawable != null) {
            mClipPath.reset();
            RectF rect = new RectF(mPaddingLeft, mPaddingTop, mRight - mLeft - mPaddingRight, mBottom - mTop - mPaddingBottom);
            if (mRoundCorner == ROUND_CIRCLE) {
                float halfWidth = (mRight - mLeft - mPaddingLeft - mPaddingRight) / 2f;
                float halfHeight = (mBottom - mTop - mPaddingBottom - mPaddingTop) / 2f;
                float radius = Math.min(halfWidth, halfHeight);
                mClipPath.addCircle(halfWidth + mPaddingLeft, halfHeight + mPaddingTop, radius, Path.Direction.CW);

            } else if (mRoundCorner > 0) {
                mClipPath.addRoundRect(rect, mRoundCorner, mRoundCorner, Path.Direction.CW);
            } else {
                mClipPath.addRect(rect, Path.Direction.CW);
            }
        }
    }

    private void drawOnCachedBitmap() {
//        mCachedBitmap.recycle();
//        //recreate bitmap if needed
//        if(mCachedBitmap.getWidth() < mWidth || mCachedBitmap.getHeight() < mHeight){
//            mCachedBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
//        }
//
//        Canvas canvas = new Canvas(mCachedBitmap);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        RectF rect = new RectF(0, 0, mWidth, mHeight);
//        Shader bitmapShader = new BitmapShader(mCachedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        paint.setColor(0xFF000000);
//        paint.setShader(bitmapShader);
//        canvas.drawRoundRect(rect, mRoundCorner, mRoundCorner, paint);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDrawable == null) {
            return;
        }
        if (mDrawableWidth == 0 || mDrawableHeight == 0) {
            return;
        }

        //NQH: this action maybe slow down the draw action
        boolean saveCanvas = needToSaveCanvas();
        if (saveCanvas)
            canvas.save();


        //anti alias if needed
        if (needAntiAlias())
            canvas.drawPath(mClipPath, mAntiAliasPaint);

        //clip drawing region
        if (saveCanvas)
            canvas.clipPath(mClipPath);

        //translate if needed
        if (mPaddingLeft > 0 || mPaddingTop > 0)
            canvas.translate(mPaddingLeft, mPaddingTop);

        //draw canvas with matrix
        if (mDrawMatrix != null)
            canvas.concat(mDrawMatrix);
        mDrawable.draw(canvas);


        //NQH: this action will be slow down the draw action
        if (saveCanvas)
            canvas.restore();

    }

    private boolean needToSaveCanvas() {
        return mRoundCorner == ROUND_CIRCLE || mRoundCorner > 0 || mPaddingLeft > 0 ||  mPaddingTop > 0 || mPaddingRight > 0 || mPaddingBottom > 0;
    }

    private boolean needAntiAlias() {
        return mRoundCorner > 0 || mRoundCorner == ROUND_CIRCLE;
    }


}
