package vn.com.vng.elementview.element;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import vn.com.vng.elementview.element.Element;


/**
 * Created by HungNQ on 31/08/2017.
 */

public class ElementView extends View {


    private Element mRootElement;


    public ElementView(Context context) {
        super(context);
    }

    public Element getRootElement() {
        return mRootElement;
    }

    public void setRootElement(Element rootElement) {
        mRootElement = rootElement;

        //init layout params
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if(layoutParams == null)
            layoutParams = new ViewGroup.LayoutParams(rootElement.getWidthDimension(), rootElement.getHeightDimension());
        else{
            layoutParams.width = rootElement.getWidthDimension();
            layoutParams.height = rootElement.getHeightDimension();
        }
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRootElement != null) {
            mRootElement.measure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mRootElement.getMeasureWidth(), mRootElement.getMeasureHeight());
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mRootElement != null)
            mRootElement.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRootElement != null)
            mRootElement.draw(canvas);
    }


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return (mRootElement != null && mRootElement.onTouchEvent(event)) || super.onTouchEvent(event);
    }



}
