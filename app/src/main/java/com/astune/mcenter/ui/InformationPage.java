package com.astune.mcenter.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.AnimationUtils;
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

    private InformationPageViewModel mViewModel;

    private boolean enteredFromSetting = false;

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
        mViewModel = (InformationPageViewModel) viewModel;
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
            this.getParentFragmentManager().popBackStack();
        });

        //setting activity event
        layout.settingBtn.setOnClickListener(c->{
            Intent intent = new Intent(parent, SettingActivity.class);
            enteredFromSetting = true;
            startActivityForResult(intent, 0);
        });

        setUIData();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        layout = null;
    }

    public void setUIData(){
        try {
            Map<String, String> dataMap = PropertiesUtil.getProperty(
                parent.getFilesDir() + Environment.SETTING_PROPERTIES,
                Properties.EMAIL
            );
            layout.emailInfo.setText(String.format("Email: \n%s", dataMap.get(Properties.EMAIL)));

            String nu = null != getArguments().getString("deviceNum")? getArguments().getString("deviceNum"): "";
            layout.deviceNumInfo.setText(String.format("You have %s devices", nu));

            layout.avatarInfo.setImageBitmap(mViewModel.getAvatar(this.requireContext()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        if(resultCode == 0){
            this.getParentFragmentManager().popBackStack();
            doOnStateHooks(ActivityState.ON_PAUSE);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            layout.infoBackground.setRenderEffect(null);
        }

        if (!enteredFromSetting) layout.blurFilter.startAnimation(AnimationUtils.loadAnimation(parent, R.anim.slide_out));

    }
}