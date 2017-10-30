package com.example.blath.around.activities;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blath.around.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout detailButton = (LinearLayout) findViewById(R.id.detailButton);
        final LinearLayout lsb1 = (LinearLayout) findViewById(R.id.lsb1);
        final LinearLayout controlView = (LinearLayout) findViewById(R.id.controlView);
        final LinearLayout lsb2 = (LinearLayout) findViewById(R.id.lsb2);
        final TextView detailText = (TextView) findViewById(R.id.detail_text);

        detailButton.setOnClickListener(new View.OnClickListener() {
            ImageView caret = (ImageView) findViewById(R.id.caret);
            @Override
            public void onClick(View v) {
                if(View.GONE == controlView.getVisibility()){
                    caret.animate().rotation(180).start();
                    detailText.setText("Hide Details");
                    controlView.setVisibility(View.VISIBLE);
//                    expand(controlView, 1000, 250);
                }else{
//                    collapse(controlView, 1000, 0);
                    controlView.setVisibility(View.GONE);
                    caret.animate().rotation(360).start();
                    detailText.setText("Show Details");
                }
            }
        });
     }

    public static void expand(final View v, int duration, int targetHeight) {

        int prevHeight  = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void collapse(final View v, int duration, int targetHeight) {
        int prevHeight  = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
}
