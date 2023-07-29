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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.FragmentLinkPageBinding;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.object.Link.Link;
import com.astune.mcenter.object.Link.NewLink;
import com.astune.mcenter.ui.customered.HookedFragment;
import com.astune.mcenter.utils.PopupMenuUtil;
import com.astune.mcenter.utils.enums.LinkType;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class LinkPage extends HookedFragment {

    private LinkPageViewModel viewModel;

    private FragmentLinkPageBinding layout;

    private int touchX, touchY = 0;

    private FragmentManager pageManager;


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
        viewModel = getViewModel(LinkPageViewModel.class);
        viewModel.setId(getArguments().getInt("id"));
    }

    @Override
    public void onStart(){
        super.onStart();

        //set affect on background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            layout.linkBackground.setRenderEffect(RenderEffect.createBlurEffect(25F, 25F, Shader.TileMode.CLAMP));
        }
        layout.linkBackground.setSourceView(parent.findViewById(R.id.main_background_image));

        // entering animation
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

        //bind grid with cardList
        viewModel.getLinkList().observe(this, cardList ->{
            layout.linkCardList.setCard(cardList);
            layout.linkCardList.addCard(new NewLink(getArguments().getInt("id"), ""));
        });

        //set onclickListeners
        //link start event
        layout.linkCardList.setOnclickListener(link ->{
            Log.i("linkPage", "Link" + link.getName() + "clicked");
            startFromCard(link);
            return null;
        });
        //goBack
        layout.linkReturnBtn.setOnClickListener(c ->{
            Log.i("linkPage", "exist event");
            getParentFragmentManager().popBackStack();
        });
        //get touching position
        layout.linkPageForeground.setOnTouchListener((view, motionEvent) ->{
            touchX = (int) motionEvent.getX();
            touchY = (int) motionEvent.getY();
            return false;
        });
        //start menu on click position
        layout.linkCardList.setOnLongClickListener((Function1<Link, Unit>) link ->{
            PopupMenu menu = new PopupMenu(parent, layout.linkReturnBtn);
            menu.getMenuInflater().inflate(R.menu.card_list_popup_menu, menu.getMenu());
            PopupMenuUtil.showMenuOnPosition(menu, touchX, touchY);
            menu.setOnMenuItemClickListener(item ->{
                Log.i("linkPage", item.getTitle().toString().equals(getResources().getString(R.string.delete)) ? getResources().getString(R.string.delete) : getResources().getString(R.string.edit));
                if(item.getTitle().toString().equals(getResources().getString(R.string.delete))) viewModel.deleteLink(link) ;
                return false;
            });
            return null;
        });

        pageManager = getParentFragmentManager();
        assert getArguments() != null;
        viewModel.refreshLinkTable();
    }

    @Override
    public void onPause(){
        super.onPause();
        viewModel.finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        viewModel.refreshLinkTable();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        layout = null;
    }

    // exiting animation
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

    //start different fragment basing on the card clicked
    private void startFromCard(Link link){
        FragmentTransaction transaction;
        Bundle bundle;
        switch (link.getType()){
            case WEB_LINK:
                WebLinkPage webLinkPage = new WebLinkPage(touchX, touchY);
                bundle = new Bundle(getArguments());
                bundle.putString("address", getArguments().getString("ip") + ":" + link.getInfo());
                webLinkPage.setArguments(bundle);
                transaction = pageManager.beginTransaction();
                transaction.replace(R.id.link_page_container, webLinkPage);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case SSH_LINK:
                break;
            case NEW_LINK:
                LinkInsertionPage linkInsertionPage = new LinkInsertionPage(touchX, touchY);
                bundle = new Bundle(getArguments());
                linkInsertionPage.setArguments(bundle);
                transaction = pageManager.beginTransaction();
                transaction.replace(R.id.link_page_container, linkInsertionPage);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                throw new IllegalArgumentException("unavailable linktype");
        }
    }

}