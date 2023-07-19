package com.astune.mcenter.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.FragmentLinkPageBinding;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.object.Link.Link;
import com.astune.mcenter.object.Room.WebLink;
import com.astune.mcenter.ui.customered.HookedFragment;
import com.astune.mcenter.utils.PopupMenuUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.ArrayList;
import java.util.List;

public class LinkPage extends HookedFragment {

    private LinkPageViewModel mViewModel;

    private FragmentLinkPageBinding layout;

    private int touchX, touchY = 0;

    public static LinkPage newInstance() {
        return new LinkPage();
    }

    public LinkPage(){super();}

    public LinkPage(Hook[] hooks){super(hooks);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = FragmentLinkPageBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewModel = (LinkPageViewModel) viewModel;
    }

    @Override
    public void onStart(){
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            layout.linkBackground.setRenderEffect(RenderEffect.createBlurEffect(25F, 25F, Shader.TileMode.CLAMP));
        }
        layout.linkBackground.setSourceView(parent.findViewById(R.id.main_background_image));
        layout.linkBackground.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Animator animator = AnimatorInflater.loadAnimator(parent, R.animator.slide_in);
                ((ValueAnimator)animator).addUpdateListener((c)->{ //update view as translation
                    layout.linkBackground.invalidate();
                });

                ((ValueAnimator)animator).setFloatValues(layout.linkPageContainer.getWidth(), 0);
                animator.setTarget(layout.linkPageContainer);
                animator.start();

                //destroy the observer after finish
                layout.linkBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        List<Link> testLink = new ArrayList<>();
        for (int i = 0; i < 25; i++){
            testLink.add(new WebLink(i, 0, "test" + i, "123.456.789"));
        }

        layout.linkCardList.setCard(testLink);
        layout.linkCardList.setOnclickListener(c ->{
            Log.i("linkPage", "Link" + c.getName() + "clicked");
            return null;
        });

        layout.linkReturnBtn.setOnClickListener(c ->{
            Log.i("linkPage", "exist event");
            getParentFragmentManager().popBackStack();
        });

        layout.linkPageForeground.setOnTouchListener((view, motionEvent) ->{
            touchX = (int) motionEvent.getX();
            touchY = (int) motionEvent.getY();
            return false;
        });

        layout.linkCardList.setOnLongClickListener((Function1<Link, Unit>) link ->{
            PopupMenu menu = new PopupMenu(parent, layout.linkReturnBtn);
            menu.getMenuInflater().inflate(R.menu.card_list_popup_menu, menu.getMenu());
            PopupMenuUtil.showMenuOnPosition(menu, touchX, touchY);
            menu.setOnMenuItemClickListener(item ->{
                Log.i("linkPage", item.getTitle().toString().equals("delete") ? "delete":"edit");
                return false;
            });
            return null;
        });
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim){
        if (!enter){
            Animator animator = AnimatorInflater.loadAnimator(parent, R.animator.slide_in);
            ((ValueAnimator)animator).addUpdateListener((u)->{ //update view as translation
                layout.linkBackground.invalidate();
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    layout = null;
                }
            });

            ((ValueAnimator)animator).setFloatValues(0, layout.linkPageContainer.getWidth());
            animator.setTarget(layout.linkPageContainer);
            animator.setInterpolator(new DecelerateInterpolator());
            return animator;
        }
        return super.onCreateAnimator(transit, true, nextAnim);
    }

}