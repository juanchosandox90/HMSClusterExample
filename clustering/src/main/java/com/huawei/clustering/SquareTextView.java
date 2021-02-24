package com.huawei.clustering;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Cluster Icon
 */

class SquareTextView extends AppCompatTextView {

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        //noinspection SuspiciousNameCombination
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}
