package com.astune.mcenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.astune.mcenter.ui.customered.ClippedBackgroundView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
    public class CustomViewTest {
        private ClippedBackgroundView customView;
        private Bitmap bitmap;

        @Before
        public void setUp() {
            // Create a custom parent view with a red background
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            FrameLayout parentView = new FrameLayout(context);
            parentView.layout(0, 0, 100, 100);
            parentView.setBackgroundColor(Color.RED);

            // Create a Bitmap to capture the canvas output
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

            // Create the custom view and set the parent view as the source view
            customView = new ClippedBackgroundView(context);
            customView.setSourceView(parentView);
            parentView.addView(customView);
        }

        @Test
        public void testOnDraw() {
            // Draw the custom view onto the bitmap
            customView.draw(new Canvas(bitmap));

            // Check that the top left pixel is red
            assertThat(bitmap.getPixel(0, 0), is(Color.RED));
        }
    }

