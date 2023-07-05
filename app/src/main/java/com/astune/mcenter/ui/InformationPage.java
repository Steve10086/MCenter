package com.astune.mcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.FragmentInformationPageBinding;
import com.astune.mcenter.object.HookedFragment;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.utils.PropertiesUtil;
import com.astune.mcenter.utils.enums.Environment;
import com.astune.mcenter.utils.enums.Properties;

import java.io.IOException;
import java.util.Map;

public class InformationPage extends HookedFragment {
    private FragmentInformationPageBinding layout;

    private InformationPageViewModel mViewModel;

    public InformationPage() {super();}
    public InformationPage(Hook[] hooks, Class<InformationPageViewModel> informationPageViewModelClass) {
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

        Animation animation = AnimationUtils.loadAnimation(parent, R.anim.slide_in);
        layout.infoBackground.startAnimation(animation);
        //info page exist event
        layout.blurFilter.setOnClickListener(c->{
            this.getParentFragmentManager().popBackStack();
        });

        layout.settingBtn.setOnClickListener(c->{
            Intent intent = new Intent(parent, SettingActivity.class);
            startActivityForResult(intent, 0);
        });

        setUIData();


        //loading data
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
            this.onPause();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i("infoPage", "paused");
    }

}