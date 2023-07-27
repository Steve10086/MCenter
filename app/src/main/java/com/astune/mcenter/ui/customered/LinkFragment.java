package com.astune.mcenter.ui.customered;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import androidx.fragment.app.Fragment;
import com.astune.mcenter.R;

public class LinkFragment extends Fragment {
    private int pivotX = 0;
    private int pivotY = 0;

    public LinkFragment() {super();}

    public LinkFragment(int x, int y) {
        super();
        pivotX = x;
        pivotY = y;
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim){
        if(enter){
            Animation animation = new ScaleAnimation(0, 1.05f, 0, 1.05f, Animation.ABSOLUTE, pivotX, Animation.ABSOLUTE, pivotY);
            animation.setDuration(200);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            return animation;
        }else{
            return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        }
    }
}
