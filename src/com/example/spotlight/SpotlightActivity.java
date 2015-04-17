package com.example.spotlight;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
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
                //纪录点击的点
                location[0] = location[0] + v.getWidth() / 2;
                location[1] = location[1] - v.getHeight();
                //将点击的点传递到下一个activity
                Intent intent = new Intent(SpotlightActivity.this, MaskActivity.class);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);
                startActivity(intent);
                //屏蔽系统默认activity动画
                overridePendingTransition(0, 0);
            }
        });
    }

}
