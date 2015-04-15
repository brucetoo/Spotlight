package com.example.spotlight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import java.util.Objects;

public class MaskView extends View {

    private Paint mPiant = new Paint();
    private float[] mLocation = new float[2];
    private int mCurrentRadius;
    private int mTargetId = -1;
    private Bitmap targetBitmap;

    public OnRadiusOver getOnRadiusOver() {
        return onRadiusOver;
    }

    public void setOnRadiusOver(OnRadiusOver onRadiusOver) {
        this.onRadiusOver = onRadiusOver;
    }

    private OnRadiusOver onRadiusOver;

    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpotlightView, 0, 0);
        try {
            mTargetId = a.getResourceId(R.styleable.SpotlightView_target, 0);
        } catch (Exception e) {
        } finally {
            a.recycle();
        }
    }

    public void setMCurrentRadius(int mCurrentRadius) {
        this.mCurrentRadius = mCurrentRadius;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                createShader();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void createShader() {
        //获取目标视图,draw a bitmap
        View target = getRootView().findViewById(mTargetId);
        targetBitmap = Bitmap.createBitmap(target.getWidth(), target.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(targetBitmap);
        target.draw(c);

        //用背景图来渲染
        Shader shader = new BitmapShader(targetBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPiant.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPiant.setAntiAlias(true);
        mPiant.setColor(Color.WHITE);
        if (mLocation != null)
            canvas.drawCircle(mLocation[0], mLocation[1], mCurrentRadius, mPiant);
    }

    public void startAnimate(float[] location){
        mLocation = location;
        animateRadius();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mLocation[0] = event.getX();
//        mLocation[1] = event.getY();
//        animateRadius();
//        return true;
//    }

    private void animateRadius() {

        ObjectAnimator radiusUp = ObjectAnimator.ofInt(this, "mCurrentRadius", 0, Math.max(getWidth(),getHeight()));
        radiusUp.setDuration(800);
        radiusUp.start();
        radiusUp.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(onRadiusOver != null){
                    onRadiusOver.onRadiuOverListener(mTargetId);
                }
            }
        });
    }

    public interface OnRadiusOver{
       void onRadiuOverListener(int targetId);
    }
}
