package com.astune.mcenter.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.utils.ID.activityStateID;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    protected FragmentManager pageManager;

    private FragmentTransaction transaction;

    private Fragment informationPage;

    private Fragment newDevicePage;

    private ImageView infoBtn;

    private ImageButton createDeviceBtn;

    private TextView titleText;

    private final CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);

        pageManager = MainActivity.this.getSupportFragmentManager();

        titleText = findViewById(R.id.title);

        infoBtn = findViewById(R.id.user_info_btn);

        createDeviceBtn = findViewById(R.id.create_btn);

        viewModel.deviceList.observe(this, devices -> {
            refreshDeviceCard();
            Log.i("Room", devices.toString());
        });

        createDeviceBtn.setOnClickListener(c->{
            Log.i("icon", "clicked");
            try {
                createBtnClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            infoBtn.setVisibility(View.INVISIBLE);
            createDeviceBtn.setVisibility(View.INVISIBLE);
        });

        //information_page btn
        infoBtn.setOnClickListener(c -> {
            Log.i("icon", "clicked");
            try {
                infoPageClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            infoBtn.setVisibility(View.INVISIBLE);
            createDeviceBtn.setVisibility(View.INVISIBLE);
        });
    }

    //add information page into main stage
    public void infoPageClicked() throws NoSuchMethodException {
        if (informationPage == null){
            informationPage = new InformationPage(
                    new Hook[]{
                            new Hook(this.getClass().getDeclaredMethod("pageExisted"),
                                    this,
                                    activityStateID.ON_PAUSE)
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
                    new Hook(this.getClass().getDeclaredMethod("pageExisted"),
                            this,
                            activityStateID.ON_PAUSE)
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

    public void pageExisted(){
        this.viewModel.refreshDeviceList();
        this.infoBtn.setVisibility(View.VISIBLE);
        this.createDeviceBtn.setVisibility(View.VISIBLE);
    }
}