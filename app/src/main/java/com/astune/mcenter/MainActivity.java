package com.astune.mcenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.object.Device;
import com.astune.mcenter.object.Room.AppDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);

        ImageView info_btn = findViewById(R.id.user_info_btn);



        //information_page btn
        info_btn.setOnClickListener(c -> {
            //add information page into main stage
            Fragment informationPage = new Information_page();
            FragmentTransaction transaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.information_container, informationPage);
            transaction.addToBackStack(null);

            transaction.commit();
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.i("Main", "resumed");
        //refreshDeviceCard();
    }


    private void refreshDeviceCard(){
        List<Device> deviceList = viewModel.refreshDeviceData();

    }
}