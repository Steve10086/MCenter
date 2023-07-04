package com.astune.mcenter.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.astune.mcenter.R;
import com.astune.mcenter.databinding.ActivitySettingBinding;
import com.astune.mcenter.utils.enums.Properties;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {
    private SettingActivityViewModel viewModel;

    private ActivitySettingBinding layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        setContentView(layout.getRoot());
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SettingActivityViewModel.class);
        layout.avatarImg.setImageBitmap(viewModel.getAvatar(this));
        layout.setViewModel(viewModel);
        layout.setLifecycleOwner(this);
        viewModel.getData(this);

        viewModel.getSettingMap().observe(this, map ->{
            Log.i("setting", map.get(Properties.EMAIL));
        });


        layout.settingBackground.setOnClickListener(c ->{
            if (null != getCurrentFocus()){
                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        layout.setAvatarBtn.setOnClickListener(c->{
            Log.i("File selector", "opened");
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onPause(){
        try {
            viewModel.saveData(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            try {

                viewModel.setAvatar(resultData.getData(), this);
                layout.avatarImg.setImageBitmap(viewModel.getAvatar(this));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onPasswordClick(){
        Dialog setPassWord = new Dialog(this);
        
    }
}