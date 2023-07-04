package com.astune.mcenter.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.ActivityMainBinding;
import com.astune.mcenter.object.Hook;
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
        layout = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(layout.getRoot());

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);

        pageManager = MainActivity.this.getSupportFragmentManager();

        viewModel.deviceList.observe(this, devices -> {
            refreshDeviceCard();
            Log.i("Room", devices.toString());
        });


        layout.mainTitleBar.createBtn.setOnClickListener(c->{
            Log.i("icon", "clicked");
            try {
                createBtnClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            layout.mainTitleBar.userInfoBtn.setVisibility(View.INVISIBLE);
            layout.mainTitleBar.createBtn.setVisibility(View.INVISIBLE);
        });

        //information_page btn
        layout.mainTitleBar.userInfoBtn.setOnClickListener(c -> {
            Log.i("icon", "clicked");
            try {
                infoPageClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            layout.mainTitleBar.userInfoBtn.setVisibility(View.INVISIBLE);
            layout.mainTitleBar.createBtn.setVisibility(View.INVISIBLE);
        });
    }

    //add information page into main stage
    public void infoPageClicked() throws NoSuchMethodException {
        if (informationPage == null){
            informationPage = new InformationPage(
                    new Hook[]{
                            new Hook(this.getClass().getDeclaredMethod("infoPageExisted"),
                                    this,
                                    ActivityState.ON_PAUSE)
                    }
                    );
        }
        transaction = pageManager.beginTransaction();
        transaction.replace(R.id.information_container, informationPage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createBtnClicked() throws NoSuchMethodException {
        if (newDevicePage == null){
            newDevicePage = new DeviceCreationPage(new Hook[]{
                    new Hook(this.getClass().getDeclaredMethod("creatingPageExisted"),
                            this,
                            ActivityState.ON_PAUSE)
            });
            pageManager = MainActivity.this.getSupportFragmentManager();
        }

        transaction = pageManager.beginTransaction();
        transaction.replace(R.id.information_container, newDevicePage);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void refreshDeviceCard(){

    }

    public void infoPageExisted(){
        this.layout.mainTitleBar.userInfoBtn.setVisibility(View.VISIBLE);
        this.layout.mainTitleBar.createBtn.setVisibility(View.VISIBLE);
    }

    public void creatingPageExisted(){
        this.viewModel.refreshDeviceList();
        this.layout.mainTitleBar.userInfoBtn.setVisibility(View.VISIBLE);
        this.layout.mainTitleBar.createBtn.setVisibility(View.VISIBLE);
    }
}