package com.example.spotlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by brucetoo on 15/4/15.
 */
public class MaskActivity extends Activity {
    private MaskView maskView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask);

        final float locationX = getIntent().getIntExtra("locationX", 0);
        final float locationY = getIntent().getIntExtra("locationY", 0);

        maskView = (MaskView) findViewById(R.id.spotlight);
        maskView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                maskView.getViewTreeObserver().removeOnPreDrawListener(this);
                maskView.startAnimate(new float[]{locationX, locationY});
                final RelativeLayout view = (RelativeLayout) findViewById(R.id.content);
                maskView.setOnRadiusOver(new MaskView.OnRadiusOver() {
                    @Override
                    public void onRadiuOverListener(int targetId) {
                        Toast.makeText(MaskActivity.this, "Animation Over", Toast.LENGTH_SHORT).show();
                        maskView.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        view.getChildAt(0).setFocusable(true);
                        view.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MaskActivity.this, "bottom text clicked", Toast.LENGTH_SHORT).show();
                            }
                        });

                        view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
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
}
