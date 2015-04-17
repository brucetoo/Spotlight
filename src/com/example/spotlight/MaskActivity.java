package com.example.spotlight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by brucetoo on 15/4/15.
 * MaskActivity activity切换时的动画
 * 实现方式：
 * 1.首先时要修改系统默认切换activity的动画，在style中配置theme
 *   <style name="TransparentTheme" parent="AppBaseTheme">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsTranslucent">true</item>
     </style>
   2.在切换的目标activity 布局中 attrs.xml 加上 属性并制定 target ID号
   接下来看代码....
 */
public class MaskActivity extends Activity {
    private MaskView maskView;
    private RelativeLayout targetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);

        final float locationX = getIntent().getIntExtra("locationX", 0);
        final float locationY = getIntent().getIntExtra("locationY", 0);

        //maskView 必须有一个自定义属性 target 来纪录目标界面
        //maskView 其实就相当于 target 视图的一个snap
        maskView = (MaskView) findViewById(R.id.spotlight);
        maskView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                maskView.getViewTreeObserver().removeOnPreDrawListener(this);
                //开始执行动画
                maskView.startEnterAnimate(new float[]{locationX, locationY});
                targetView = (RelativeLayout) findViewById(R.id.content);
                maskView.setOnEnterEndListener(new MaskView.onEnterEndListener() {
                    @Override
                    public void onEnterEnd(int targetId) {
                        Toast.makeText(MaskActivity.this, "Animation Over", Toast.LENGTH_SHORT).show();
                        //执行动画完后隐藏掉动画view，显示目标 view
                        maskView.setVisibility(View.GONE);
                        targetView.setVisibility(View.VISIBLE);
                        targetView.getChildAt(0).setFocusable(true);
                        targetView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MaskActivity.this, "bottom text clicked", Toast.LENGTH_SHORT).show();
                            }
                        });

                        targetView.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MaskActivity.this, "up text clicked", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        //按系统返回键显示动画view，隐藏目标 view
        maskView.setVisibility(View.VISIBLE);
        targetView.setVisibility(View.GONE);
        maskView.startExitAnimate(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                MaskActivity.super.onBackPressed();
                overridePendingTransition(0, 0);
            }
        });

    }
}
