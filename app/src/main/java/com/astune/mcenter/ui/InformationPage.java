package com.astune.mcenter.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.object.HookedFragment;
import com.astune.mcenter.object.Hook;
import com.hoko.blur.HokoBlur;

public class InformationPage extends HookedFragment {

    private InformationPageViewModel mViewModel;

    private ConstraintLayout infoBackground;

    public static InformationPage newInstance() {
        return new InformationPage(null);
    }

    public InformationPage(Hook[] hooks) {
        super(hooks);
    }

    public InformationPage() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(InformationPageViewModel.class);

        if(!mViewModel.isCreated()){
            //blur background by catching screen drawing cache
            View mainView = parent.getWindow().getDecorView();
            View background = parent.findViewById(R.id.background);
            mainView.setDrawingCacheEnabled(true);
            mViewModel.setBackPic(Bitmap.createBitmap(mainView.getDrawingCache(),
                    0, (mainView.getHeight() - background.getHeight()) / 2, //fix the different between view height and real height
                    background.getWidth(), background.getHeight()));
            mainView.setDrawingCacheEnabled(false);
            mViewModel.Created(true);
        }


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    //entry animation
        infoBackground = parent.findViewById(R.id.blur_filter);
        infoBackground.setBackground(new BitmapDrawable(HokoBlur.with(this.requireContext()).radius(5).blur(mViewModel.getBackPic())));

        ImageButton settingBtn = parent.findViewById(R.id.setting_btn);

        Animation animation = AnimationUtils.loadAnimation(parent, R.anim.slide_in);
        parent.findViewById(R.id.info_background).startAnimation(animation);


        //info page exist event
        infoBackground.setOnClickListener(c->{
            this.getParentFragmentManager().popBackStack();
        });

        settingBtn.setOnClickListener(c->{
            Intent intent = new Intent(parent, SettingActivity.class);
            startActivity(intent);
        });

    //loading data
    }

    private void loadInfoFromModel(){

    }

}