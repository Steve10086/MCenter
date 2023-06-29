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
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Hook;
import com.astune.mcenter.utils.ID.activityStateID;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    protected FragmentManager pageManager;

    private FragmentTransaction transaction;

    private Fragment informationPage;

    private Fragment newDevicePage;

    private ImageView infoBtn;

    private ImageButton createDeviceBtn;

    private TextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);

        pageManager = MainActivity.this.getSupportFragmentManager();

        titleText = findViewById(R.id.title);

        infoBtn = findViewById(R.id.user_info_btn);

        createDeviceBtn = findViewById(R.id.create_btn);



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

    @Override
    protected void onResume(){
        super.onResume();

        Log.i("Main", "resumed");
        //refreshDeviceCard();
    }

    //add information page into main stage
    public void infoPageClicked() throws NoSuchMethodException {
        if (informationPage == null){
            informationPage = new Information_page(
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
            newDevicePage = new device_creation_page(new Hook[]{
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
        List<Device> deviceList = viewModel.refreshDeviceData();
    }

    public void pageExisted(){
        this.infoBtn.setVisibility(View.VISIBLE);
        this.createDeviceBtn.setVisibility(View.VISIBLE);
    }
}