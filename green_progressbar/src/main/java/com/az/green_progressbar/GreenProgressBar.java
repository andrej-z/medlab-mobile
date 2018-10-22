package com.az.green_progressbar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class GreenProgressBar extends ProgressBar {
    public GreenProgressBar(Context context) {
        super(context);
        setupProgressBar(context);
    }

    public GreenProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupProgressBar(context);
    }

    public GreenProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupProgressBar(context);
    }

    private void setupProgressBar(Context context) {
        setIndeterminateDrawable(new SmoothProgressDrawable.Builder(context).interpolator(new FastOutSlowInInterpolator())
                .color(Color.parseColor("#009966"))
                .build());

    }

}
