package com.astune.mcenter.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;

public class SettingActivity extends AppCompatActivity {
    private SettingActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SettingActivityViewModel.class);

        viewModel.getData(getFilesDir().getAbsolutePath());

    }


}