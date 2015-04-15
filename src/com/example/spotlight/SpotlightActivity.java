package com.example.spotlight;

import android.content.Intent;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.spotlight.SpotlightView.AnimationSetupCallback;

import android.os.Bundle;
import android.view.View;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;

public class SpotlightActivity extends Activity {

    private final String TAG = getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.spotlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpotlightActivity.this, SpotActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.maskreveal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location[] = new int[2];
                v.getLocationInWindow(location);
                location[0] = location[0] + v.getWidth() / 2;
                location[1] = location[1] - v.getHeight() / 2;
                Intent intent = new Intent(SpotlightActivity.this, MaskActivity.class);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
//		final SpotlightView2 spotlightView2 = (SpotlightView2) findViewById(R.id.spotlight);
//		final RelativeLayout view  = (RelativeLayout) findViewById(R.id.content);
//		spotlightView2.setOnRadiusOver(new SpotlightView2.OnRadiusOver() {
//			@Override
//			public void onRadiuOverListener(int targetId) {
//				Toast.makeText(SpotlightActivity.this,"Animation Over",Toast.LENGTH_SHORT).show();
//				spotlightView2.setVisibility(View.GONE);
//				view.setVisibility(View.VISIBLE);
//				Log.d(TAG, ((TextView) view.getChildAt(0)).getText() + "");
//				view.getChildAt(0).setFocusable(true);
//				view.getChildAt(0).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Toast.makeText(SpotlightActivity.this,"bottom text clicked",Toast.LENGTH_SHORT).show();
//					}
//				});
//
//				view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Toast.makeText(SpotlightActivity.this,"up text clicked",Toast.LENGTH_SHORT).show();
//					}
//				});
//
//			}
//		});


//		SpotlightView spotlight = (SpotlightView) findViewById(R.id.spotlight);
//		spotlight.setAnimationSetupCallback(new AnimationSetupCallback() {
//			@Override
//			public void onSetupAnimation(SpotlightView spotlight) {
//				//attachtoWindow的时候调用
//				createAnimation(spotlight);
//			}
//		});
    }

    private void createAnimation(final SpotlightView spotlight) {
        View top = findViewById(R.id.textView1);
        View bottom = findViewById(R.id.textView2);

        final float textHeight = bottom.getBottom() - top.getTop();
        final float startX = top.getLeft();
        final float startY = top.getTop() + textHeight / 2.0f;
        final float endX = Math.max(top.getRight(), bottom.getRight());

        spotlight.setMaskX(endX);
        spotlight.setMaskY(startY);

        spotlight.animate().alpha(1.0f).withLayer().withEndAction(new Runnable() {
            @Override
            public void run() {
//				ObjectAnimator moveLeft = ObjectAnimator.ofFloat(spotlight, "maskX", endX, startX);
//				moveLeft.setDuration(2000);

                float startScale = spotlight.computeMaskScale(textHeight);
                ObjectAnimator scaleUp = ObjectAnimator.ofFloat(spotlight, "maskScale", 0, spotlight.getHeight() + spotlight.getWidth());
                scaleUp.setDuration(2000);

//				ObjectAnimator moveCenter = ObjectAnimator.ofFloat(spotlight, "maskX", spotlight.getWidth() / 2.0f);
//				moveCenter.setDuration(1000);
//
//				ObjectAnimator moveUp = ObjectAnimator.ofFloat(spotlight, "maskY", spotlight.getHeight() / 2.0f);
//				moveUp.setDuration(1000);

//				ObjectAnimator superScale = ObjectAnimator.ofFloat(spotlight, "maskScale",
//						spotlight.computeMaskScale(Math.max(spotlight.getHeight()*2, spotlight.getWidth()) * 1.7f));
//				superScale.setDuration(2000);

                AnimatorSet set = new AnimatorSet();
//				set.play(moveLeft).with(scaleUp);
//				set.play(moveCenter).after(scaleUp);
//				set.play(moveUp).after(scaleUp);
                set.play(scaleUp);
                set.start();

                set.addListener(new AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
//						findViewById(R.id.content).setVisibility(View.VISIBLE);
//						findViewById(R.id.spotlight).setVisibility(View.GONE);
//						getWindow().setBackgroundDrawable(null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });
            }
        });
    }
}
