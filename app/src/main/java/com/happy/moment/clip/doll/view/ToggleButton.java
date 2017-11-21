package com.happy.moment.clip.doll.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Devin on 2017/11/20 23:49
 * E-mail:971060378@qq.com
 */

public class ToggleButton extends View {

    private Bitmap slideBg;//滑块的背景图
    private Bitmap switchOn;//滑动开关开的背景图
    private Bitmap switchOff;//滑动开关关的背景图

    private ToggleState state = ToggleState.OPEN;//滑动开关的状态，默认为OPEN；
    //手指触摸在view上的坐标，这个是相对于view的坐标
    private int currentX;
    //是否在滑动
    private boolean isSliding = true;

    /**
     * 如果自定的控件需要在Java中实例化，重写该构造方法
     *
     * @param context
     */
    public ToggleButton(Context context) {
        super(context);
    }

    /**
     * 如果自定义控件需要在xml文件使用，重写该构造方法
     *
     * @param context
     * @param attrs
     */
    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置滑块的背景图片
     *
     * @param slide_bg
     */
    public void setSildeBackgroundResource(int slide_bg) {
        slideBg = BitmapFactory.decodeResource(getResources(), slide_bg);
    }

    /**
     * 设置滑动开关开的背景图
     *
     * @param switch_on
     */
    public void setSwitchOnBackgroundResource(int switch_on) {
        switchOn = BitmapFactory.decodeResource(getResources(), switch_on);
    }

    /**
     * 设置滑动开关关的背景图
     *
     * @param switch_off
     */
    public void setSwitchOffBackgroundResource(int switch_off) {
        switchOff = BitmapFactory.decodeResource(getResources(), switch_off);
    }

    public void setToggleState(ToggleState toggleState) {
        this.state = toggleState;
    }

    public enum ToggleState {
        OPEN, CLOSE
    }

    /**
     * 绘制view的宽与高，以滑动开关的宽高绘制
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchOn.getWidth(), switchOn.getHeight());
    }

    /**
     * 画view
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int left = currentX - slideBg.getWidth() / 2;
        // (left > (switchOn.getWidth() / 2)) ? ((state = ToggleState.OPEN)) : ((state = ToggleState.CLOSE));
        //当触摸点超过整view的一半时，就切换开关背景；

        super.onDraw(canvas);
        /**
         * (@NonNull Bitmap bitmap, float left, float top, @Nullable Paint paint)
         * left:图片左边的x坐标
         * top：图片顶部的y坐标
         */
        if (isSliding) {
            if (state == ToggleState.OPEN) {
                //绘制开关图片
                canvas.drawBitmap(switchOn, 0, 0, null);
                //绘制滑块图片
                if (left > (switchOn.getWidth() - slideBg.getWidth()))
                    left = switchOn.getWidth() - slideBg.getWidth();
                canvas.drawBitmap(slideBg, left, 0, null);
            } else {
                //绘制开关图片
                canvas.drawBitmap(switchOff, 0, 0, null);
                //绘制滑块图片
                if (left < 0)
                    left = 0;
                canvas.drawBitmap(slideBg, left, 0, null);
            }
        } else {
            if (state == ToggleState.OPEN) {
                //绘制开关图片
                canvas.drawBitmap(switchOn, 0, 0, null);

                canvas.drawBitmap(slideBg, switchOn.getWidth() - slideBg.getWidth(), 0, null);
            } else {
                //绘制开关图片
                canvas.drawBitmap(switchOff, 0, 0, null);

                canvas.drawBitmap(slideBg, 0, 0, null);
            }
        }

    }

    /**
     * 定义滑动
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSliding = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                break;
        }
        //在滑动的过程中状态改变也进行监听，触摸结束后也监听状态改变，未改变就无需其他动作
        if (currentX > switchOn.getWidth() / 2) {
            if (state != ToggleState.OPEN) {
                state = ToggleState.OPEN;
                if (stateChangeListeren != null) {
                    stateChangeListeren.onToggleStateChangeListeren(state);
                }
            }
        } else {
            //CLOSE
            if (state != ToggleState.CLOSE) {
                state = ToggleState.CLOSE;
                if (stateChangeListeren != null) {
                    stateChangeListeren.onToggleStateChangeListeren(state);
                }
            }
        }
        //调用此方法，间接调用OnD
        invalidate();
        return true;
    }

    /**
     * 开放状态改变接口
     */
    private OnToggleStateChangeListeren stateChangeListeren;

    public void setOnStateChangeListeren(OnToggleStateChangeListeren stateChangeListeren) {
        this.stateChangeListeren = stateChangeListeren;
    }

    public interface OnToggleStateChangeListeren {
        void onToggleStateChangeListeren(ToggleState state);
    }
}
