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

/**
 * A view showing the information under it contained by sourceView as background,  keep calling invalidate() to refresh the background when view is moving
 * Overwrites onDraw() method, draw the content under it using canvas as bitmap, then added as background
 **/
public class ClippedBackgroundView extends View {
    private View sourceView;

    private Bitmap newBackground = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

    private final int[] location = new int[2];

    private int statusBarHeight;


    public ClippedBackgroundView(Context context) {
        super(context);
    }

    public ClippedBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClippedBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClippedBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    //store the background as a whole bitmap in background
    private void init(){
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


    {// discount the height of status bar to fix the up shift of background
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = 0;
        if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);
    }


    // catching the real position on Screen, cut background on size to fit the view
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
