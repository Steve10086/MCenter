package com.astune.mcenter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.object.Room.Device;
import com.astune.mcenter.object.Room.databaseBuilder;
import com.astune.mcenter.utils.Hook;
import com.astune.mcenter.utils.ID;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    private FragmentManager infoPageManager;

    private FragmentTransaction transaction;

    private Fragment informationPage;

    private ImageView info_btn;

    ImageButton create_device_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseBuilder.init(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);


        info_btn = findViewById(R.id.user_info_btn);

        create_device_btn = findViewById(R.id.create_btn);

        create_device_btn.setOnClickListener(c->{
            Log.i("icon", "clicked");
            infoPageManager.popBackStack();
        });

        //information_page btn
        info_btn.setOnClickListener(c -> {
            Log.i("icon", "clicked");
            try {
                infoPageClicked();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            info_btn.setVisibility(View.INVISIBLE);
            create_device_btn.setVisibility(View.INVISIBLE);
        });


        ScrollView cardList = findViewById(R.id.main_scene_scroll);


        cardList.setOnClickListener(c -> {

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
                            new Hook(this.getClass().getDeclaredMethod("showInfoIcon"),
                                    this,
                                    ID.ON_PAUSE),
                            new Hook(this.getClass().getDeclaredMethod("existInfoPage"),
                                    this,
                                    ID.ON_START)
                    }
                    );
            infoPageManager = MainActivity.this.getSupportFragmentManager();
        }

        transaction = infoPageManager.beginTransaction();
        transaction.replace(R.id.information_container, informationPage);
        transaction.addToBackStack(null);
        transaction.commit();

        //entry animation
        /*Animation slide = new TranslateAnimation(
                informationPage.getView().getX()-informationPage.getView().getWidth(),
                informationPage.getView().getY(),
                informationPage.getView().getX(),
                informationPage.getView().getY());*/
    }

    private void refreshDeviceCard(){
        List<Device> deviceList = viewModel.refreshDeviceData();
    }

    public void showInfoIcon(){
        this.info_btn.setVisibility(View.VISIBLE);
    }

    public void existInfoPage(){
        this.infoPageManager.popBackStack();
    }


}