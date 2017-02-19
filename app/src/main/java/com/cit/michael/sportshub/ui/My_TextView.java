package com.cit.michael.sportshub.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by micha on 13/02/2017.
 */

public class My_TextView extends TextView {

    public My_TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public My_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public My_TextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Light.ttf");
            setTypeface(tf);
        }
    }

}
