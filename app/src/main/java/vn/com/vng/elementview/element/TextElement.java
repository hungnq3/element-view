package vn.com.vng.elementview.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;

import com.facebook.fbui.textlayoutbuilder.TextLayoutBuilder;

/**
 * Created by HungNQ on 30/08/2017.
 */

public class TextElement extends Element {

    //    public static final int DEFAULT_TEXT_COLOR = 0xff808080;
    public static final int DEFAULT_TEXT_COLOR = 0x8a000000;
    public static final int DEFAULT_TEXT_SIZE_IN_SP = 14;

    public TextElement(Context context) {
        this(context, WRAP_CONTENT, WRAP_CONTENT);
    }

    public TextElement(Context context, int widthDimension, int heightDimension) {
        super(context, widthDimension, heightDimension);
        init();
    }


    private TextLayoutBuilder mTextLayoutBuilder;
    private Layout mTextLayout;

    private TextPaint mBoundPaint = new TextPaint();

    //properties
    private CharSequence mText;
    private int mTextSize;
    private int mTextColor;
    private Layout.Alignment mAlignment;
    private int mMaxLines;
    private boolean mSingleLine;
    private TextUtils.TruncateAt mEllipsize;
    private Typeface mTypeFace;


    private void init() {

        //init default properties
        buildDefaultProperties();
        mBoundPaint.setStyle(Paint.Style.STROKE);

        //build LayoutBuilder with default properties
        mTextLayoutBuilder = new TextLayoutBuilder()
                .setShouldCacheLayout(true)
                .setText(mText)
                .setTextColor(mTextColor)
                .setAlignment(mAlignment)
                .setEllipsize(mEllipsize)
                .setMaxLines(mMaxLines)
                .setSingleLine(mSingleLine)
                .setTextSize(mTextSize)
                .setTypeface(mTypeFace);

    }

    private void buildDefaultProperties() {
        mText = "";
        mTextSize = (int) (DEFAULT_TEXT_SIZE_IN_SP * getContext().getResources().getDisplayMetrics().scaledDensity);
        mTextColor = DEFAULT_TEXT_COLOR;
        mMaxLines = Integer.MAX_VALUE;
        mSingleLine = false;
        mAlignment = Layout.Alignment.ALIGN_NORMAL;
        mEllipsize = null;
    }


    //----------------------Properties' getter & setter region---------------
    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        mText = makeSureTextValid(text);
        mTextLayoutBuilder.setText(mText);
    }

    private CharSequence makeSureTextValid(CharSequence text) {
        return text == null ? "" : text;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTextLayoutBuilder.setTextSize(mTextSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextLayoutBuilder.setTextColor(mTextColor);
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        mTextLayoutBuilder.setMaxLines(mMaxLines);
    }

    public boolean isSingleLine() {
        return mSingleLine;
    }

    public void setSingleLine(boolean singleLine) {
        mSingleLine = singleLine;
        mTextLayoutBuilder.setSingleLine(mSingleLine);
    }

    public Layout.Alignment getAlignment() {
        return mAlignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        mAlignment = alignment;
        mTextLayoutBuilder.setAlignment(mAlignment);
    }

    public TextUtils.TruncateAt getEllipsize() {
        return mEllipsize;
    }

    public void setEllipsize(TextUtils.TruncateAt ellipsize) {
        mEllipsize = ellipsize;
        mTextLayoutBuilder.setEllipsize(mEllipsize);
    }

    public Typeface getTypeFace() {
        return mTypeFace;
    }

    public void setTypeFace(Typeface typeFace) {
        mTypeFace = typeFace;
        mTextLayoutBuilder.setTypeface(mTypeFace);
    }

    //-----------------endregion--------------------------------------------


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specWidth = getMeasureSpecSize(widthMeasureSpec);
        int freeSpaceWidth = specWidth - mPaddingLeft - mPaddingRight;
        if (freeSpaceWidth < 0)
            freeSpaceWidth = 0;

        //build a layout to calculate text width and height
//        mTextLayout = new StaticLayout(mText, mTextPaint, freeSpaceWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
        mTextLayout = buildTextLayout(freeSpaceWidth);

        int textWidth;
        int textHeight;

        if (mTextLayout.getLineCount() <= 1) {
            textWidth = (int) mTextLayout.getLineWidth(0);
        } else
            textWidth = freeSpaceWidth;

        textHeight = mTextLayout.getLineBottom(mTextLayout.getLineCount() - 1) - mTextLayout.getLineTop(0);


        int desireWidth = reconcileSize(textWidth + mPaddingLeft + mPaddingRight, widthMeasureSpec);
        int desireHeight = reconcileSize(textHeight + mPaddingTop + mPaddingBottom, heightMeasureSpec);

        //rebuild static layout if needed
        if (desireWidth != specWidth) {
            mTextLayout = buildTextLayout(textWidth);
        }
        setMeasuredDimension(resolveDimensionSpec(desireWidth, widthMeasureSpec), resolveDimensionSpec(desireHeight, heightMeasureSpec));
    }


    //

    /**
     * build a {@link Layout} based on text width and element's properties set
     *
     * @return {@link Layout}
     */
    private Layout buildTextLayout(int width) {
        return mTextLayoutBuilder
                .setWidth(width)
                .build();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        if (mPaddingLeft > 0 || mPaddingTop > 0)
            canvas.translate(mPaddingLeft, mPaddingTop);
        if (mPaddingRight > 0 || mPaddingLeft > 0 || mPaddingBottom > 0 || mPaddingTop > 0)
            canvas.clipRect(0, 0, mWidth - mPaddingRight - mPaddingLeft, mHeight - mPaddingBottom - mPaddingTop);
//        canvas.drawRect(0, 0, mWidth- mPaddingRight - mPaddingLeft, mHeight - mPaddingBottom - mPaddingTop, mBoundPaint);
        if (mTextLayout != null)
            mTextLayout.draw(canvas);
        canvas.restore();
    }

}
