package com.astune.mcenter.ui.customered;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import com.astune.mcenter.R;


public class ClippedBackgroundView extends View {
    private View sourceView;

    private Bitmap newBackground;

    private final int[] location = new int[2];

    private int statusBarHeight;


    public ClippedBackgroundView(Context context) {
        super(context);
    }

    public ClippedBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int id = context.obtainStyledAttributes(attrs, R.styleable.ClippedBackgroundView).
                getResourceId(R.styleable.ClippedBackgroundView_source_view, 0);
        sourceView = ((Activity)context).
                findViewById(id);
        init();
    }

    public ClippedBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClippedBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        if (null != sourceView) {
            newBackground = Bitmap.createBitmap(sourceView.getWidth(), sourceView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(newBackground);
            sourceView.draw(c);
            // Restore the canvas state
        }
    }


    public void setSourceView(View sourceView) {
        this.sourceView = sourceView;
        init();
    }


    {        // discount the height of status bar to fix the up shift of background
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = 0;
        if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the parent view's background onto the canvas
        getLocationOnScreen(location);

        int x = -location[0];
        int y = -location[1] + statusBarHeight;

        canvas.drawBitmap(newBackground, x, y, null);
    }
}
