package com.astune.mcenter.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.ActivityInfo;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.ActivityMainBinding;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.utils.enums.ActivityState;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    protected FragmentManager pageManager;

    private FragmentTransaction transaction;

    private Fragment informationPage;

    private Fragment newDevicePage;

    private ActivityMainBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        layout = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(layout.getRoot());

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);

        viewModel.refreshDeviceList();

        pageManager = MainActivity.this.getSupportFragmentManager();

        viewModel.deviceList.observe(this, devices -> {
            layout.cardList.setCard(devices);
            Log.i("Room", devices.toString());
        });

        layout.mainTitleBar.createBtn.setOnClickListener(c->{
            try {
                createBtnClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        //information_page btn
        layout.mainTitleBar.userInfoBtn.setOnClickListener(c -> {
            try {
                infoPageClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        });

        layout.cardList.setOnclickListener((device) ->{
            Log.i("CardList", device.toString() + "clicked");
            try {
                deviceCardClicked(device);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            return null;
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        viewModel.updateOnline();
    }

    public void deviceCardClicked(Device device) throws NoSuchMethodException {
        viewModel.title.setValue(device.getName());
        Fragment linkPage = new LinkPage(
                new Hook[]{
                        new Hook(this.getClass().getDeclaredMethod("creatingPageExisted"),
                                this,
                                ActivityState.ON_PAUSE),
                        new Hook(this.getClass().getDeclaredMethod("creatingPageEntered"),
                                this,
                                ActivityState.ON_START)
                }
        );
        Bundle bundle = new Bundle(device.toBundle());
        bundle.putString("viewModelClass", LinkPageViewModel.class.getName());
        linkPage.setArguments(device.toBundle());
    }

    //add information page into main stage
    public void infoPageClicked() throws NoSuchMethodException {
        if (informationPage == null){
            informationPage = new InformationPage(
                    new Hook[]{
                            new Hook(this.getClass().getDeclaredMethod("infoPageExisted"),
                                    this,
                                    ActivityState.ON_PAUSE),
                            new Hook(this.getClass().getDeclaredMethod("infoPageEntered"),
                                    this,
                                    ActivityState.ON_START)
                    }
                    );
            Bundle bundle = new Bundle();
            bundle.putString("viewModelClass", InformationPageViewModel.class.getName());
            informationPage.setArguments(bundle);
        }
        transaction = pageManager.beginTransaction();
        transaction.add(R.id.information_container, informationPage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createBtnClicked() throws NoSuchMethodException {
        if (newDevicePage == null){
            newDevicePage = new DeviceCreationPage(
                    new Hook[]{
                            new Hook(this.getClass().getDeclaredMethod("creatingPageExisted"),
                                    this,
                                    ActivityState.ON_PAUSE),
                            new Hook(this.getClass().getDeclaredMethod("creatingPageEntered"),
                                    this,
                                    ActivityState.ON_START)
                    }
            );
            Bundle bundle = new Bundle();
            bundle.putString("viewModelClass", DeviceCreationPageViewModel.class.getName());
            newDevicePage.setArguments(bundle);
            pageManager = MainActivity.this.getSupportFragmentManager();
        }

        transaction = pageManager.beginTransaction();
        transaction.replace(R.id.information_container, newDevicePage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void infoPageEntered(){
        Log.i("MainAct", "Info entered");
        layout.mainTitleBar.userInfoBtn.setVisibility(View.INVISIBLE);
        layout.mainTitleBar.createBtn.setVisibility(View.INVISIBLE);
        layout.cardList.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_list_slide_out));
    }

    public void infoPageExisted(){
        Log.i("MainAct", "Info existed");
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.card_list_slide_in);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.mainTitleBar.userInfoBtn.setVisibility(View.VISIBLE);
                layout.mainTitleBar.createBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layout.cardList.startAnimation(animation);
    }

    public void creatingPageExisted(){
        viewModel.refreshDeviceList();
        layout.mainTitleBar.userInfoBtn.setVisibility(View.VISIBLE);
        layout.mainTitleBar.createBtn.setVisibility(View.VISIBLE);
    }

    public void creatingPageEntered(){
        layout.mainTitleBar.userInfoBtn.setVisibility(View.INVISIBLE);
        layout.mainTitleBar.createBtn.setVisibility(View.INVISIBLE);
    }
}