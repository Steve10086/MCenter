package com.astune.mcenter.ui;

import android.animation.*;
import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.FragmentInformationPageBinding;
import com.astune.mcenter.ui.customered.HookedFragment;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.utils.PropertiesUtil;
import com.astune.mcenter.utils.enums.ActivityState;
import com.astune.mcenter.utils.enums.Environment;
import com.astune.mcenter.utils.enums.Properties;

import java.io.IOException;
import java.util.Map;

public class InformationPage extends HookedFragment {
    private FragmentInformationPageBinding layout;

    private InformationPageViewModel viewModel;

    private boolean enteredFromSetting = false;

    private final Object existLock = new Object();

    private boolean isExisting = false;

    public InformationPage() {super();}
    public InformationPage(Hook[] hooks) {
        super(hooks);
    }

    public static InformationPage newInstance() {
        return new InformationPage();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel(InformationPageViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = FragmentInformationPageBinding.inflate(inflater, container, false);
        return layout.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        layout.infoBackground.setSourceView(parent.findViewById(R.id.main_background_image));

        //if supported add blur effect to information pad
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            layout.infoBackground.setRenderEffect(RenderEffect.createBlurEffect(25F, 25F, Shader.TileMode.CLAMP));
        }

        //set animation if not popping back from setting activity
        if (!enteredFromSetting) {

        //using observer to get correct start x value of slide in animation
            layout.infoBackground.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

                            //using an object animator allows update background when translating
                            Animator animator = AnimatorInflater.loadAnimator(parent, R.animator.slide_in);
                            ((ValueAnimator)animator).addUpdateListener((c)->{ //update view as translation
                                layout.infoBackground.invalidate();
                            });

                            ((ValueAnimator)animator).setFloatValues(-layout.blurFilter.getWidth(), 0);
                            animator.setTarget(layout.blurFilter);
                            animator.start();

                            //destroy the observer after finish
                            layout.infoBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        } else enteredFromSetting = false;

        //info page exist event
        layout.blurFilter.setOnClickListener(c->{
            layout.blurFilter.setClickable(false);
            getParentFragmentManager().popBackStack();
        });

        //setting activity event
        layout.settingBtn.setOnClickListener(c->{
            Intent intent = new Intent(parent, SettingActivity.class);
            enteredFromSetting = true;
            disableNextHook(ActivityState.ON_PAUSE);
            disableNextHook(ActivityState.ON_START);
            startActivity(intent);
        });

        setUIData();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        Log.i("info", "view destroyed");
    }

    public void setUIData(){
        try {
            Map<String, String> dataMap = PropertiesUtil.getProperty(
                parent.getFilesDir() + Environment.SETTING_PROPERTIES,
                Properties.EMAIL
            );
            layout.emailInfo.setText(String.format("Email: \n%s", dataMap.get(Properties.EMAIL)));

            String nu = null != getArguments().getString("deviceNum")? getArguments().getString("deviceNum") : "";
            layout.deviceNumInfo.setText(String.format("You have %s devices", nu));

            layout.avatarInfo.setImageBitmap(viewModel.getAvatar(this.requireContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim){
        if (!enteredFromSetting && !enter){
            //using an object animator allows update background when translating
                Animator animator = AnimatorInflater.loadAnimator(parent, R.animator.slide_in);
                ((ValueAnimator) animator).addUpdateListener((var) -> { //update view as translation
                    try {
                        layout.infoBackground.invalidate();
                    }catch (NullPointerException e){
                        Log.e("infoUIAnimator", "unExpected " + e.getMessage());
                    }
                });

                ((ValueAnimator) animator).setFloatValues(0, -layout.blurFilter.getWidth());

                animator.setTarget(layout.blurFilter);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        super.onAnimationEnd(animator);
                        isExisting = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            layout.infoBackground.setRenderEffect(null);
                        }
                        //Log.i("info", "animation end");
                        layout = null;
                    }

                });
                doOnStateHooks(ActivityState.ON_PAUSE);
                disableNextHook(ActivityState.ON_PAUSE);
                return animator;
        }
        return super.onCreateAnimator(transit, enter, nextAnim);
    }
}